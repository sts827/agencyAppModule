package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTemplate {
    private int templateId;
    private int productTourId;
    private String productStatus;
    private String title;
    private String subtitle;
    private String templateType;
    private String templateHtml;
    private String templateThumbnail;
    private String createId;
    private String createDate;

    private String deleteId;
    private String deleteDate;
    private String deleteYn;

    private String lastUpdateId;
    private String lastUpdateDate;

    private String version;
    private String imageByte;
    private String uploadFilename;
}
