package kr.co.wayplus.travel.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reservation extends CommonDataSet {
	private Long id;	//고유번호
	private String userEmail;	//사용자ID(이메일ID)
	private String userName;	//고객 이름(비회원 직접입력, 회원 user 테이블)
	private String userMobile;	//고객 연락처(비회원 직접입력, 회원 user 테이블)
	private String productSerial;	//상품번호(없을수 있음)
	private Long productTourId;	//상품고유번호
	private Integer priceOptionId;
	private String comment;	//코멘트
	private String applyCode;	//접수상태코드(0:접수, 1:접수확인,2:답변완료)
	private String reservationCode;	//예약상태코드
	private String cancelYn;	//취소구분
	private String cancelCode;	//취소코드
	private String groupJson;	//단체
	private String berthType;	//숙소_종류
	private String berthJson;	//숙소
	private String travelScheduleJson;	//여행_스케줄(json)
	private String airTypeRequest;	//항공 종류 요청(0 : 왕복, 1 : 출발만, 2 : 복귀만, 3 : 요청안함)
	private String airScheduleJson;	//항공_스케줄(json)
	private String vehicleType;	//차량_종류
	private Integer vehicleCount;	//차량_대수
	private String vehicleJson;	//차량
	private Long totalAmount;	//총가격

	// 추가되는 부분
	private String productThumbnail; //
	private String priceOptionJson;
	private String optionName;
	private String customerType;

	private String orderType;
	private String payMoid;
	private String payMethod;
	//virtual column
	private int rownum;	//고유번호
	private String productTitle;
	private int productCount;
	private String travelScheduleDt;
	private String reservationCodeName;
	private String userJoinType;

	private long amtT;
	private long amtG;
	private long amtA;
	private long amtD;
	private long amtB;

	private ArrayList<UserCustomerOrderList> orderList;

	public Reservation() {
		reservationCode = "0";
		userName = "";
		productTitle = "";
	}

	public Reservation addOrderType(String orderType) {
		this.orderType = orderType;
		return this;
	}
	public Reservation addCreateId(String userEmail) {
    	//this.userEmail = userEmail;
    	setCreateId(userEmail);
    	return this;
    }
	public Reservation addPayMoid(String moid) {
    	this.payMoid = moid;
    	return this;
    }
}
