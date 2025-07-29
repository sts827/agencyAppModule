package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingMailServer extends CommonDataSet {
    private int id;
    private String alarmType;
    private String smtpEmail;
    private String smtpUsername;
    private String smtpServer;
    private int smtpPort;
    private String smtpAuthorizeRequired;
    private String smtpSecureType;
    private String smtpLoginId;
    private String smtpLoginPw;
    private String smtpUseYn;
}
