package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTemplateFile {
    private int fileId;
    private int templateId;
    private String uploadPath;
    private String uploadFilename;
    private String fileExtension;
    private int fileSize;
    private String fileMimetype;
    private String originFilename;
    private String createId;
    private String createDate;
    private String deleteYn;

    public ProductTemplateFile(){
        this.fileId=0;
        this.templateId=0;
        this.fileSize=0;
    }
}
