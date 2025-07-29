package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainBannerImage extends CommonDataSet {
    private int id;
    private int menuId;
    private String bannerType;

    private int imageIdPc;
    private String imageUrlPc;
    private String imageFitPc;
    private String visibleYnPc;

    private int imageIdMobile;
    private String imageUrlMobile;
    private String imageFitMobile;
    private String visibleYnMobile;

    private String imageTextTop;
    private String imageTextTopFont;
    private String imageTextTopColor;
    private int imageTextTopX;
    private int imageTextTopY;

    private String imageTextMid;
    private String imageTextMidFont;
    private String imageTextMidColor;
    private int imageTextMidX;
    private int imageTextMidY;

    private String imageTextBot;
    private String imageTextBotFont;
    private String imageTextBotColor;
    private int imageTextBotX;
    private int imageTextBotY;

    private String linkUrl;
    private String linkTarget;
    private String startDate;
    private String expireDate;
    private String useYn;
    private int orderNumber;

    private String langType;
    private String textBg;
}
