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
public class SettingAwardsContents extends CommonDataSet {
    private int rownum;
    private int pagenum;
    private int id;
    private String regDate;
    private String type;
    private String title;
    private String originFilename;
    private String useYn;

    private String  uploadFilename;
}
