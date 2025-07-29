package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCustomerInfo {
    private int id;
    private String userEmail;
    private String customerGrade;
    private String customerNote;
    private String createType;
    private String createId;
    private String createDate;
}
