package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardComment extends CommonDataSet {
    private int id;
    private int boardId;
    private int contentId;
    private int tabIndex;
    private int upperId;
    private String note;
    private int warningId;
    private String blindYn;
    private String blindId;
    private String blindDate;
    private String applyCode;
    private String secretYn;
    private String createName;

    public BoardComment(){
        this.id = 0;
        this.boardId = 0;
        this.contentId = 0;
        this.tabIndex = 0;
        this.upperId = 0;
        this.warningId = 0;
    }
}
