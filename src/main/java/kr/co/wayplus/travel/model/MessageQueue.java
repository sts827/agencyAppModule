package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageQueue extends CommonDataSet {
    private int id;
    private String messageType;
    private String queueType;
    private String subject;
    private String content;
    private String contentType;
    private String toName;
    private String toAddress;
    private String sendRequestTime;
    private String sendRequestUserid;
    private String sendApplyTime;
    private String sendStatusCode;
    private String sendResult;
    private String sendResultTime;
    private String sendResultMessage;
}
