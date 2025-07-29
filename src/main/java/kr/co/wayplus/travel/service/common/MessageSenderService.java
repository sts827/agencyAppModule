package kr.co.wayplus.travel.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.wayplus.travel.mapper.common.MessageSenderMapper;
import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.util.LoggerUtil;
import kr.co.wayplus.travel.util.NCloudApiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageSenderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageSenderMapper messageSenderMapper;
    private final NCloudApiUtil nCloudApiUtil;
    private final LoggerService loggerService;


    public MessageSenderService(MessageSenderMapper messageSenderMapper, NCloudApiUtil nCloudApiUtil, LoggerService loggerService) {
        this.messageSenderMapper = messageSenderMapper;
        this.nCloudApiUtil = nCloudApiUtil;
        this.loggerService = loggerService;
    }

    public void sendMailFromSet(String alarmType, int formatId) throws Exception{
        logger.debug("메일 발송 시작");
        SettingMailServer mailServer = messageSenderMapper.selectMailServer(alarmType);
        if(mailServer == null) throw new RuntimeException("SMTP 서버 정보를 찾을 수 없습니다.");

        SettingMailFormat mailFormat = messageSenderMapper.selectMailFormat(formatId);
        if(mailFormat == null) throw new RuntimeException("발신 메일 양식을 찾을 수 없습니다.");

        ArrayList<SettingMailReceiver> mailReceivers = messageSenderMapper.selectMailReceiverList(mailServer.getId());

        if(!mailReceivers.isEmpty()) {
            LoginUser user = new LoginUser();
            if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null){
                user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", mailServer.getSmtpServer());
            properties.put("mail.smtp.port", mailServer.getSmtpPort());
            properties.put("mail.smtp.auth", mailServer.getSmtpAuthorizeRequired().equals("Y") ? true:false);
            properties.put("mail.smtp.starttls.enable", mailServer.getSmtpSecureType().equals("TLS") ? true:false);
            if(mailServer.getSmtpSecureType().equals("TLS")
                || mailServer.getSmtpSecureType().equals("SSL")) {
                properties.put("mail.smtp.starttls.trust", mailServer.getSmtpServer());
            }

            for (SettingMailReceiver receiver : mailReceivers) {
                logger.debug("Receiver Add : " + receiver.getEmail());
                Session mailSession = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailServer.getSmtpLoginId(), mailServer.getSmtpLoginPw());
                    }
                });

                Message message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailServer.getSmtpEmail(), mailServer.getSmtpUsername(), "UTF-8"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver.getEmail(), receiver.getUsername()));
                message.setSubject(mailFormat.getTitle());
                message.setContent(mailFormat.getMailContent(), "text/html; charset=utf-8");

                LogMailSend logMailSend = new LogMailSend(mailServer.getSmtpEmail(), receiver.getEmail(), LoggerUtil.getPropertiesAsString(properties), formatId, mailFormat.getTitle(), mailFormat.getMailContent(), "text/html; charset=utf-8", user.getUserEmail());
                logMailSend.setId(loggerService.writeMailingLog(logMailSend));
                try {
                    Transport.send(message);
                    logMailSend.setSendResult("SUCCESS");
                    loggerService.updateMailingLogResult(logMailSend);
                }catch (Exception e){
                    logger.error("Mail Send Error : " + e.getMessage());
                    logMailSend.setSendResult("ERROR");
                    logMailSend.setSendResultMessage(e.getMessage());
                    loggerService.updateMailingLogResult(logMailSend);
                }
            }

            logger.debug("메일 발송 완료");
        }else{
            logger.debug("메일 수신자 목록이 없습니다.");
        }
        
        logger.debug("메일 발송 종료");
    }

    public void pushMessageQueueFromSet(String alarmType, int formatId) {
        logger.debug("메시지 발송 QUEUE 시작");
        SettingMessage messageSet = messageSenderMapper.selectMessageSetting(alarmType);
        if(messageSet == null) throw new RuntimeException("메시지 발송 설정 정보를 찾을 수 없습니다.");

        SettingMessageFormat messageFormat = messageSenderMapper.selectMessageFormat(formatId);
        if(messageFormat == null) throw new RuntimeException("발신 메시지 양식을 찾을 수 없습니다.");

        ArrayList<SettingMessageReceiver> messageReceivers = messageSenderMapper.selectMessageReceiverList(messageSet.getId());

        if(!messageReceivers.isEmpty()) {
            LoginUser user = new LoginUser();
            if(SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null){
                user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

            String startTime, endTime, nowTime, tempTime[];
            MessageQueue message = new MessageQueue();
            message.setMessageType(messageSet.getApiType());
            message.setContentType(messageFormat.getTemplateCode());
            message.setSubject(messageFormat.getTitle());
            message.setContent(messageFormat.getContent());
            message.setSendRequestUserid(user.getUserEmail());
            message.setCreateId(user.getUserEmail());
            message.setSendStatusCode("A");

            if(messageSet.getApiUseTime() != null) {
                tempTime = messageSet.getApiUseTime().split("-");
                startTime = tempTime[0];
                endTime = tempTime[1];

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                nowTime = timeFormat.format(new Date());

                try{
                    Date startDate = timeFormat.parse(startTime);
                    Date nowDate = timeFormat.parse(nowTime);
                    Date endDate = timeFormat.parse(endTime);
                    if(startDate.before(nowDate) && endDate.after(nowDate)){
                        logger.debug("메시지 설정시간 내 발송입니다.");
                        message.setQueueType("normal");

                    }else{
                        logger.debug("메시지 설정시간 외 발송입니다.");
                        SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd " + startTime + ":00");
                        Calendar cal = GregorianCalendar.getInstance();
                        if(endDate.before(nowDate)){
                            //발송 시간 초과 당일의 경우 익일 시작시 발송
                            cal.add(Calendar.DATE, 1);
                        }
                        message.setQueueType(messageSet.getApiQueueType());
                        message.setSendApplyTime(startFormat.format(cal.getTime()));
                    }
                }catch (Exception e){
                    logger.error("메시지 발송 설정시간 오류입니다.");
                    logger.error(e.getMessage());
                    message.setQueueType("normal");
                }
            }else{
                message.setQueueType("normal");
            }

            if(message.getQueueType().equals("no")){
                logger.debug("메시지 시간외 발송이 꺼져있습니다.");
            }else{
                for (SettingMessageReceiver receiver : messageReceivers) {
                    logger.debug("Receiver Add Queue : " + receiver.getMobile().replaceAll("-", ""));
                    message.setToName(receiver.getUsername());
                    message.setToAddress(receiver.getMobile().replaceAll("-", ""));
                    if(message.getMessageType().equals("ncloud.BizMessage")){
                        message.setContent(convertBizMessageVariable(messageFormat.getTemplateCode(), message.getContent()));
                    }

                    messageSenderMapper.insertMessageQueue(message);
                }
                logger.debug("메시지 발송 QUEUE 등록 완료");
            }
        }else{
            logger.debug("메시지 수신자 목록이 없습니다.");
        }
        logger.debug("메시지 발송 QUEUE 종료");
    }

    public void popMessageQueueSendMessage() {
        logger.debug("Message Send START");
        int sendPendingCount = messageSenderMapper.selectPendingMessageQueueCount("ncloud.BizMessage");
        if(sendPendingCount > 0){
            ArrayList<MessageQueue> messageQueues = messageSenderMapper.selectPendingMessageQueue("ncloud.BizMessage");
            ArrayList<MessageQueue> summaryMessage = new ArrayList<>();
            ArrayList<MessageQueue> normalMessage = new ArrayList<>();
            HashMap<String, ArrayList<MessageQueue>> messageHashMap = new HashMap<>();
            for (MessageQueue message : messageQueues) {
                switch (message.getQueueType()){
                    case "normal":
                        normalMessage.add(message);
                        break;
                    case "summary":
                        summaryMessage.add(message);
                        break;
                }
            }

            if(summaryMessage.size() > 0){
                logger.debug("요약 메시지 발송 처리");
                String apiUrl = "https://sens.apigw.ntruss.com/sms/v2";
                nCloudApiUtil.setApiBaseUrlAndFormat(apiUrl, "JSON");
                ArrayList<MessageGroupCount> groupCount = messageSenderMapper.selectPendingMessageQueueGroupCount("ncloud.BizMessage");

                for (MessageGroupCount counter: groupCount) {
                    try{
                        NCloudSmsMessageContainer messageContainer = new NCloudSmsMessageContainer();
                        NCloudSmsMessage message = new NCloudSmsMessage();
                        message.setTo(counter.getToAddress());
                        messageContainer.setMessages(Collections.singletonList(message));
                        messageContainer.setType("SMS");
                        messageContainer.setContent("COMM");
                        messageContainer.setFrom("0647570110");
                        messageContainer.setContent(String.format("시간외 설정된 메시지가 %s건 있습니다.", counter.getCnt()));
                        NCloudSmsMessageResult result = nCloudApiUtil.sendSmsMessage(messageContainer);
                        logger.debug(String.format("전달결과: [ID]: %s, 결과[%s] %s", result.getRequestId(), result.getStatusCode(), result.getStatusName()));
                        HashMap<String, Object> paramMap = new HashMap<>();
                        paramMap.put("messageType", "ncloud.BizMessage");
                        paramMap.put("to", counter.getToAddress());
                        paramMap.put("statusCode", "FIN");
                        paramMap.put("resultTime", result.getRequestTime());
                        paramMap.put("result", result.getStatusCode());
                        paramMap.put("resultMessage", result.getStatusName());
                        paramMap.put("lastUpdateId", "sendMessageQueueNCloudBizMessage");
                        messageSenderMapper.updatePendingSummaryMessageSendComplete(paramMap);
                    } catch (JsonProcessingException | URISyntaxException e) {
                        logger.error("Message Send Error : " + counter.getToAddress());
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }else{
                logger.debug("일반 메시지 발송 처리");
                String apiUrl = "https://sens.apigw.ntruss.com/alimtalk/v2";
                nCloudApiUtil.setApiBaseUrlAndFormat(apiUrl, "JSON");
                for(MessageQueue bizMessage : normalMessage){
                    try {
                        NCloudBizMessageContainer messageContainer = new NCloudBizMessageContainer();
                        NCloudBizMessage ncloudBizMessage = new NCloudBizMessage();
                        ncloudBizMessage.setTo(bizMessage.getToAddress());
                        ncloudBizMessage.setContent(bizMessage.getContent());
                        ncloudBizMessage.setUseSmsFailover(true);
                        messageContainer.setPlusFriendId("@wayplus");
                        messageContainer.setTemplateCode(bizMessage.getContentType());
                        messageContainer.setMessages(Collections.singletonList(ncloudBizMessage));
                        NCloudBizMessageResult result = nCloudApiUtil.sendBizMessage(messageContainer);
                        logger.debug(String.format("전달결과: [ID]: %s, 결과[%s] %s", result.getRequestId(), result.getStatusCode(), result.getMessages()));
                        HashMap<String, Object> paramMap = new HashMap<>();
                        paramMap.put("id", bizMessage.getId());
                        paramMap.put("messageType", "ncloud.BizMessage");
                        paramMap.put("to", bizMessage.getToAddress());
                        paramMap.put("statusCode", "FIN");
                        paramMap.put("resultTime", result.getRequestTime());
                        paramMap.put("result", result.getStatusCode());
                        paramMap.put("resultMessage", LoggerUtil.getObjectListToString(result.getMessages()));
                        paramMap.put("lastUpdateId", "sendMessageQueueNCloudBizMessage");
                        messageSenderMapper.updatePendingNormalMessageSendComplete(paramMap);
                    } catch (JsonProcessingException | URISyntaxException | RuntimeException e) {
                        logger.error("Message Send Error : " + bizMessage.getToAddress());
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }

        }else{
            logger.debug("발송 대기중인 메시지가 없습니다.");
        }
        logger.debug("Message Send END");
    }

    private String convertBizMessageVariable(String templateCode, String content) {
        switch (templateCode){
            case "AUTH0001":
                String randomCode = generateRandomNumberString(6);
                content = content.replaceAll("#\\{번호\\}", randomCode);
                logger.debug(String.format("TemplateCode: %s , Replace Random Code Number : %s", templateCode, randomCode));
            break;
        }
        return content;
    }

    private String generateRandomNumberString(int digit) {
        String result = "000000";
        int random = (int) (Math.random() * Math.pow(10, digit));
        return StringUtils.leftPad(String.valueOf(random), digit);
    }


}
