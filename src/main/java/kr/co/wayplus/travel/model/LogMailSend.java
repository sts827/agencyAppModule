package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogMailSend {
    private int id;
    private String fromEmail;
    private String toEmail;
    private String mailProperties;
    private int mailFormatId;
    private String mailSubject;
    private String mailContent;
    private String mailContentType;
    private String sendRequestTime;
    private String sendRequestUserid;
    private String sendResult;
    private String sendResultTime;
    private String sendResultMessage;


    public LogMailSend() { }
    public LogMailSend (String fromEmail, String toEmail,
                        String mailProperties,
                        int mailFormatId,  String mailSubject,
                        String mailContent, String mailContentType,
                        String sendRequestUserid){
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.mailProperties = mailProperties;
        this.mailFormatId = mailFormatId;
        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
        this.mailContentType = mailContentType;
        this.sendRequestUserid = sendRequestUserid;
    }

}
