package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAttemptLog {
    private int logId;
    private String userEmail;
    private String attemptIp;
    private String attemptAgent;
    private String attemptReferer;
    private String attemptTime;
    private String errorCode;
    private String errorMessage;
}
