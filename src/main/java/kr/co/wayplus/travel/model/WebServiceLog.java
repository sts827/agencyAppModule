package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebServiceLog {
    private int logId;
    private String userToken;
    private String userEmail;
    private String referer;
    private String requestUri;
    private String requestTime;
    private String requestHost;
    private String requestAgent;
    private int responseStatus;
    private String sessionId;
    private String tracking;
}
