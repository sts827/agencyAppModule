package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTourImages {
    private int imageId;
    private int productTourId;
    private String serviceType;
    private String uploadPath;
    private String uploadFilename;
    private String fileExtension;
    private long fileSize;
    private String fileMimetype;
    private String originFilename;
    private String createId;
    private String createDate;
    private String createTime;
    private String lastUpdateId;
    private String lastUpdateDate;
    private String deleteYn;
    private String deleteId;
    private String deleteDate;

    public ProductTourImages(){
        this.imageId=0;
        this.productTourId=0;
        this.fileSize=0;
    }

}
