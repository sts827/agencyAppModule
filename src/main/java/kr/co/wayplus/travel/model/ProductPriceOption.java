package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductPriceOption extends CommonDataSet {
    private int rownum
                , productTourId
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
                    , maxCapacity
                    , maxQuantity
                    , optionGroupCode
                    , useYn
                    , addBasicOptionId;
    private List<BasicPriceList> basicPriceList;
    private List<FixPriceList> fixPriceList;
    private Map<Integer, List<DayList>> dayList;
    private List<Integer> priceIdList;
    private List<Integer> deleteBasicPriceOptionList;

    @ToString
    @Getter
    @Setter
    public static class BasicPriceList extends CommonDataSet {
        private int priceOptionId
                    , priceId
                    , productTourId
                    , priceSequence
                    , optionSequence
                    , priceSale
                    , priceNormal
                    , optionSettingId
                    , copiedPriceOptionId;
        private String priceSetDate
                    , optionName
                    , optionDesc
                    , maxCapacity
                    , maxQuantity
                    , priceSetType
                    , aloneYn
                    , useYn
                    , optionGroupCode;
    }
    @ToString
    @Getter
    @Setter
    public static class DayList extends CommonDataSet {
        private int priceOptionId
                    , optionSettingId
                    , priceId
                    , productTourId
                    , priceSequence
                    , priceSale
                    , priceNormal
                    , copiedPriceOptionId;
        private String priceSetDate
                        , optionName
                        , maxCapacity
                        , maxQuantity
                        , priceSetType
                        , aloneYn
                        , optionGroupCode
                        , productPrice
                        , useYn
                        , deleteYn;
    }

    @ToString
    @Getter
    @Setter
    public static class FixPriceList extends CommonDataSet {
        private int priceOptionId
                , priceId
                , productTourId
                , priceSequence
                , optionSequence
                , priceSale
                , priceNormal
                , optionSettingId
                , copiedPriceOptionId;
        private String priceSetDate
                , productPrice
                , optionName
                , optionDesc
                , maxCapacity
                , maxQuantity
                , priceSetType
                , aloneYn
                , optionGroupCode
                , useYn
                , addBasicOptionId;
    }
}