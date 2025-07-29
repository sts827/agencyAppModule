package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCustomerOrderList extends CommonDataSet {
    private int rownum;

    private Long reservationId;	//판매 상품 순번
    private Integer payProductSeq;	//판매 상품 순번
    private String userEmail;	//사용자ID(이메일ID)
    private String payMoid;	//거래 주문번호
    private String productSerial;	//상품 시리얼
    private Integer productTourId;	//상품번호
    private Integer priceOptionId;	//상품번호
    private String productName;	//상품이름
    private Integer productPrice;	//상품판매금액
    private String orderDate;	//상품판매금액
    private String optionPack;	//상품판매금액
    private String optionTime;	//상품판매금액
    private Integer productCount;	//상품 개수
    private Double productAmt;	//상품판매금액
    private Double productDeliveryAmt;	//상품 배달비 금액
    private String productStatus;
    private Integer cancelProductCount;	//취소 상품 개수

    private String productTitle;
    private String productThumbnail;
    private String menuUrl;

    public UserCustomerOrderList addUserEmail(String userEmail) {
    	this.userEmail = userEmail;
    	return this;
    }
    public UserCustomerOrderList addReservationId(Long id) {
    	this.reservationId = id;
		return null;
	}
    public UserCustomerOrderList addPayMoid(String payMoid) {
    	this.payMoid = payMoid;
    	return this;
    }
    public UserCustomerOrderList addOptionPack(String optionPack) {
    	this.optionPack = optionPack;
    	return this;
    }
    public UserCustomerOrderList addOptionTime(String optionTime) {
    	this.optionTime = optionTime;
    	return this;
    }
    public UserCustomerOrderList addOrderDate(String orderDate) {
    	this.orderDate = orderDate;
    	return this;
    }
    public UserCustomerOrderList addProductSerial(String productSerial) {
    	this.productSerial = productSerial;
    	return this;
    }
    public UserCustomerOrderList addProductName(String productName) {
    	this.productName = productName;
    	return this;
    }
    public UserCustomerOrderList addProductTourId(Integer productTourId) {
    	this.productTourId = productTourId;
    	return this;
    }
    public UserCustomerOrderList addPriceOptionId(Integer priceOptionId) {
    	this.priceOptionId = productTourId;
		return this;
	}
    public UserCustomerOrderList addProductPrice(Integer productPrice) {
    	this.productPrice = productPrice;
    	return this;
    }
    public UserCustomerOrderList addProductCount(Integer productCount) {
    	this.productCount = productCount;
    	return this;
    }
    public UserCustomerOrderList addCreateId(String createId) {
    	setCreateId(createId);
    	return this;
    }
    public UserCustomerOrderList addLastUpdateID(String userEmail) {
    	setLastUpdateId(userEmail);
    	return this;
    }
    public UserCustomerOrderList prdProductAmt() {

    	if(this.productPrice != null && this.productCount != null) {
    		this.productAmt =  Double.valueOf(productPrice * productCount);
    	}

    	return this;
    }
}
