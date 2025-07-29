package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentCarOption extends CommonDataSet {
    private int productTourId
                , priceId
                , priceOptionId
                , priceSale
                , priceNormal
                , productPrice
                , optionSettingId
                , copiedPriceOptionId
                , optionOrder;
    private String startDate
                    , endDate
                    , priceSetType
                    , priceSetDate
                    , aloneYn
                    , priceSequence
                    , optionSequence
                    , optionType
                    , optionName
                    , optionDesc
                    , maxQuantity
                    , optionGroupCode
                    , useYn
                    , addBasicOptionId;

}