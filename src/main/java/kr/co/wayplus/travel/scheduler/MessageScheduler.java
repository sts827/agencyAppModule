package kr.co.wayplus.travel.scheduler;

import kr.co.wayplus.travel.service.common.MessageSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageScheduler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static String ACTIVE_PROFILE;

    private final MessageSenderService messageSenderService;

    public MessageScheduler(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
        ACTIVE_PROFILE = System.getProperty("spring.profiles.active");
    }


    /**
     * 메시지 발송 검사
     */
    @Scheduled(cron="0 */5 * * * *")
    public void sendCheck(){
        logger.debug("Send Message Scheduled Start");
        messageSenderService.popMessageQueueSendMessage();
        logger.debug("Send Message Scheduled End");
    }
}
