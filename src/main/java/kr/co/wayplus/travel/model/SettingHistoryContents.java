package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingHistoryContents extends CommonDataSet {
    private int rownum;
    private int pagenum;
    private int hidden;
    private String id;
    private String historyDate;
    private String year;
    private String month;
    private String content;
    private String useYn;

    private String comment;
    private String commentTime;

    private String userName;
}
