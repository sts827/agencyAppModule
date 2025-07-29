package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingMessage extends CommonDataSet {
    private int id;
    private String alarmType;
    private String apiCorp;
    private String apiType;
    private String apiUseTime;
    private String apiQueueType;
    private String useYn;
}
