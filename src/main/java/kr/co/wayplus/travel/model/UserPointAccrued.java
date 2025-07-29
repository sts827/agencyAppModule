package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointAccrued {
    private int id;
    private String userEmail;
    private String accruedDate;
    private String accruedCode;
    private String accruedType;
    private String expireDate;
    private int pointAccrued;
    private String createId;
    private String createDate;
    private String cancelCode;
    private String cancelId;
    private String cancelDate;

    private String userName;
}
