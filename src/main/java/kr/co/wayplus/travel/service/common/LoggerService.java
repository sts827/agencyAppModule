package kr.co.wayplus.travel.service.common;

import com.sun.mail.util.MailLogger;
import kr.co.wayplus.travel.mapper.common.LoggerMapper;
import kr.co.wayplus.travel.model.LogMailSend;
import kr.co.wayplus.travel.model.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final LoggerMapper loggerMapper;

    public LoggerService(LoggerMapper loggerMapper) {
        this.loggerMapper = loggerMapper;
    }

    public int writeMailingLog(LogMailSend mailSend) {
        loggerMapper.insertLogMailSend(mailSend);
        return mailSend.getId();
    }

    public void updateMailingLogResult(LogMailSend mailSend){
        loggerMapper.updateMailingLogResult(mailSend);
    }

}
