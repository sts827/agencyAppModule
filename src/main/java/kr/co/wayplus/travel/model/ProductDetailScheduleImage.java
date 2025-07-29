package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class ProductDetailScheduleImage extends CommonDataSet {
    private int detailImageId;
    private int detailId;
    private String contentType;
    private String uploadPath;
    private String uploadFilename;
    private String fileExtension;
    private int fileSize;
    private String fileMimetype;
    private String originFilename;
    private List<MultipartFile> attach;
}
