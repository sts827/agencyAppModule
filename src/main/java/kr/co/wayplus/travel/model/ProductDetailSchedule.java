package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class ProductDetailSchedule extends CommonDataSet {
    private int productTourId
                , productMenuId
                , detailImageId
                , detailId
                , detailSequence
                , tourspotId
                , tsId;
    private String detailCategory
                    , detailTitle
                    , address
                    , dayText
                    , attachImage
                    , uploadPath
                    , uploadFilename
                    , fileExtension
                    , fileSize
                    , fileMimetype
                    , originFilename
                    , detailNote
                    , timeText
                    , transportTime
                    , transportWay
                    , detailType
                    , tsTitle
                    , tsAppend
                    , registerType
                    , stayOption
                    , stayMinimumHeadCount
                    , stayMaximumHeadCount;
    private String[] attachImageNums;
    private List<ImageNumList> imageNumList;
    @Getter
    @Setter
    public static class ImageNumList {
        private int detailImageId
                , detailId;
        private String uploadPath
                , uploadFilename;
    }

    public void parseAndSetAttachImageNums() {
        this.attachImageNums = this.attachImage.split(",");
    }
}
