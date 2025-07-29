package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPointRecord extends CommonDataSet {
    private int id;
    private String userEmail;
    private int accruedId;
    private String accruedType;
    private String accruedDate;
    private String calcAccruedDate;
    private String expireDate;
    private int pointRemain;
    private int pointAccrued;
    private int pointUsed;
    private int pointExpired;
    private String deleteCode;

    private String userName;
}
