package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardContents extends CommonDataSet {
    private int rownum;
    private int id;
    private int productTourId;
    private int boardId;
    private String boardUrl;
    private String boardName;
    private int categoryId;
    private String categoryName;
    private String title;
    private String content;
    private String applyCode;
    private int seriesId;
    private String seriesName;
    private String tags;
    private String thumbnailUrl;
    private int favoriteCount;
    private int scrapCount;
    private int commentCount;
    private int viewCount;
    private int attachmentCount;
    private int attachFileCount;
    private String useYn;
    private String fixYn;
    private String secretYn;
    private String guestName;
    private String guestPass;
    private String startDate;
    private String expireDate;

    private String comment;
    private String commentTime;

    private String userName;

    private String userType;


    public BoardContents(){
        this.rownum = 0;
        this.id = 0;
        this.boardId = 0;
        this.categoryId = 0;
        this.seriesId = 0;
        this.favoriteCount = 0;
        this.scrapCount = 0;
        this.commentCount = 0;
        this.viewCount = 0;
        this.attachmentCount = 0;
    }
}
