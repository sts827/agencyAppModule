package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductAttachFile {
    private int imageId;
    private int contentId;
    private String contentType;
    private String uploadPath;
    private String uploadFilename;
    private String fileExtension;
    private int fileSize;
    private String fileMimetype;
    private String originFilename;
    private String uploadId;
    private String uploadDate;
}
