package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ProductPostJson {
    // 상품 일정
    private List<ProductDetailSchedule> detailScheduleList;
    // 상품 옵션
    private ProductPriceOption priceOptionList;
    // 상품 기본 정보
    private ProductInfo productInfo;
    // 상품 상세 정보
    private ProductTemplate productTemplate;

    private List<ProductTourLink> productTourLinkList;

}