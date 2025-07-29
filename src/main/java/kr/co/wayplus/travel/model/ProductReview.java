package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductReview extends CommonDataSet {

    private int reviewId;
    private String productTourId;
    private String reviewTitle;
    private String reviewDesc;
    private String reviewStar;
    private String productTitle;
    private String useYn;
    private String reviewTag;
    private String productTag;
    private String profileImage;
    private String userName;
    private String[] images;
    private String uploadPath;
    private String uploadFilename;
    private String langType;
}
