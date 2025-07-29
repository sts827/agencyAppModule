package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainNoticePopup extends CommonDataSet {
    private int id;
    private String timeKey;
    private String noticeType;
    private String contentType;
    private int imageIdPc;
    private String imageUrlPc;
    private String imageBackgroundPc;
    private String imageTextPc;
    private String visibleYnPc;

    private int imageIdMobile;
    private String imageUrlMobile;
    private String imageBackgroundMobile;
    private String imageTextMobile;
    private String visibleYnMobile;

    private String titleText;
    private String noticeText;
    private String linkUrl;
    private String linkTarget;

    private String oneDayYn;
    private String oneWeekYn;
    private String startDate;
    private String expireDate;
    private int orderNumber;
    private String useYn;

    private String visibleCode;
}
