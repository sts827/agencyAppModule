package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductInfo {
    private int productTourId;
    private int productMenuId;
    private int productCategoryId;
    private int productStipulation;
    private int productRegulation;
    private int productViewCount;
    private int productFavoriteCount;
    private int productSortOrder;
    private int productSale;
    private int productNormal;
    private String productPrice;
    private String productTitle;
    private String productSubtitle;
    private String productTag;
    private String productLabel;
    private String productDescription;
    private String productDescriptionType;
    private String productShowStartDate;
    private String productShowEndDate;
    private String productReservStartDate;
    private String productReservEndDate;
    private String productTourStartDate;
    private String productTourEndDate;
    private String productThumbnail;
    private String productImages;
    private String productIncludeItem;
    private String productExcludeItem;
    private String productNeedItem;
    private String productOptionItem;
    private String productNotice;
    private String productUseYn;
    private String productAmenity;
    private String policyInventory;
    private String menuName;
    private String createId;
    private String createDate;
    private String lastUpdateId;
    private String lastUpdateDate;
    private String deleteYn;
    private String deleteId;
    private String deleteDate;
    private String productStatus;
    private String[] images;
    private String isPopular;
    private String regacyYn;
    private String productSerial;
    private String url;
    private String categoryTitle;
    private String subCategoryTitle;
    private String registerType;
    private String carFuelType;
    private String carModelYear;
    private String carCapacity;
    private String carInsurance;
    private String carOption;
    private String productTourRange;
    private String menuType;
    private String menuSubType;
    private String mainExposeType;
    private String menuUrl;
    private int    upperMenuId;

    private String productSegments;
    private String productAddress;
    private String productLink;
    private String productLocation;
    private String productOption1;
    private String productOption2;
    private String productOption3;

    private int orderCount;
    private String orderDate;
    private String optionPack;

    public ProductInfo() {
        this.productSale = 0;
        this.productNormal = 0;
    }

    private String priceSetType;
    private String priceSetDate;
    private String upperMenuUrl;
    private String langType;

    public ProductInfo addOrderCount(int orderCount) {
    	this.orderCount = orderCount;
    	return this;
    }
    public ProductInfo addProductTourId(int productTourid) {
    	this.productTourId = productTourid;
    	return this;
    }

    public ProductInfo addOderDate(String pickupDate) {
    	this.orderDate = pickupDate;
    	return this;
    }

    public ProductInfo addOptionPack(String packOption) {
    	this.optionPack = packOption;
    	return this;
    }

}
