package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSubscribeServiceInfo extends CommonDataSet {
	//빈스용
	private Integer id;
	private Long productTourId; //상품번호
	private String userEmail;	//사용자ID(이메일ID)
	private String userGrade;	//고객 등급
	private Long generalPrice;	//원래 금액
	private Long subscribePrice;	//구독 혜택으로 인한 할인 금액

	//virtual
	private String productTitle;	//상품이름
}
