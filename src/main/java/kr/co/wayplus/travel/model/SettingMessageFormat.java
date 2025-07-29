package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingMessageFormat extends CommonDataSet {
    private int id;
    private int messageId;
    private String templateCode;
    private String title;
    private String content;
    private String startDate;
    private String expireDate;
    private String useYn;
}
