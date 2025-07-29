package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingMailReceiver extends CommonDataSet {
    private int id;
    private int serverId;
    private String email;
    private String username;
    private String startDate;
    private String expireDate;
    private String useYn;
}
