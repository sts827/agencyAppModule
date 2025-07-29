package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NCloudBizMessageFailOver {
    private String type;
    private String from;
    private String subject;
    private String content;
}
