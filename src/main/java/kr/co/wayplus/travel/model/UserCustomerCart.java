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
public class UserCustomerCart extends CommonDataSet {
    private int rownum;

    private String userEmail;	//사용자ID(이메일ID)
    private String productSerial;	//상품번호
    private Integer productCount;	//장바구니 상품 개수
    private String intoTime;	//장바구니 일시

    private String productTitle;
    private String productThumbnail;
    private int productPrice;


	public UserCustomerCart addProductSerial(String productSerial) {
		this.productSerial = productSerial;
		return this;
	}

	public UserCustomerCart addProductCount(Integer productCount) {
		this.productCount = productCount;
		return this;
	}

	public UserCustomerCart addUserEmail(String username) {
		this.userEmail = username;
		return this;
	}


}
