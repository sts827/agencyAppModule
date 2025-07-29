package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingMailFormat extends CommonDataSet {
    private int id;
    private int serverId;
    private String title;
    private String mailContent;
    private String startDate;
    private String expireDate;
    private String useYn;
}
