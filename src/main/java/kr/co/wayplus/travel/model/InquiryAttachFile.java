package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonFileInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryAttachFile extends CommonFileInfo {
    private Long inquiryId;
    private String uploadFilename;
    private String originFilename;
}
