package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointSet extends CommonDataSet {
    private int id;
    private String accruedCode;
    private String accruedReason;
    private String accruedType;
    private int accruedPoint;
    private String expiredDay;
    private String expiredDayType;
    private String startDate;
    private String expireDate;
    private String useYn;
}
