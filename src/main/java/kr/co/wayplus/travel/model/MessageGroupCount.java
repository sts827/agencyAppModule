package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageGroupCount {
    private String messageType;
    private String queueType;
    private String toAddress;
    private String sendStatusCode;
    private int cnt;
}
