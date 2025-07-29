package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserSession {
    private int seq;
    private String userEmail;
    private String loginSession;
    private String loginIp;
    private String loginAgent;
    private String loginReferer;
    private String loginTime;
    private String logoutTime;
    private String logoutType;
}
