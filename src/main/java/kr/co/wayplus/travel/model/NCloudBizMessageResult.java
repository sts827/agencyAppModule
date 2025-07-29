package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NCloudBizMessageResult {
    private String requestId;
    private String requestTime;
    private String statusCode;
    private List<Object> messages;
}
