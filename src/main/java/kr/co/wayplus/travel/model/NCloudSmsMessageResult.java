package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NCloudSmsMessageResult {
    private String requestId;
    private String requestTime;
    private String statusCode;
    private String statusName;
}
