package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointUsed extends CommonDataSet {
    private int id;
    private String userEmail;
    private String usedDate;
    private String usedType;
    private int pointUsed;
    private String detailRecord;
    private String createId;
    private String createDate;
    private String cancelCode;
    private String cancelId;
    private String cancelDate;

    private String userName;
}
