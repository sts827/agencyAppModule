package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonFileInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardAttachFile extends CommonFileInfo {
    private int boardId;
    private int contentId;
}
