package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCustomerCounsel extends CommonDataSet {
    private Integer id;
    private String userEmail;
    private String userName;
    private String userMobile;
    private String requestDate;
    private String requestCategory;
    private String requestSubcategory;
    private String requestName;
    private String requestRelation;
    private String requestTel;
    private String requestText;
    private String responseDate;
    private String responseName;
    private String responseText;
    private String responseCategory;
    private String responseSubcategory;

    private String responseCategoryName;
}
