package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingCompanyInfo extends CommonDataSet {
    private int id;
    private String siteName;
    private String companyName;
    private String companyOwner;
    private String companyRegistrationNo;
    private String companyMailOrderRegistrationNo;
    private String companyTourismRegistrationNo;
    private String companyTell;
    private String companyFax;
    private String companyEmail;
    private String companyCsTime;
    private String companyCsTell;
    private String companyAddressZipcode;
    private String companyAddressJibun;
    private String companyAddressRoad;
    private String companyAddressDetail;
    private double companyLatitude;
    private double companyLongitude;

}
