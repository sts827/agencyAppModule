package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCustomerPayment extends CommonDataSet {
	private Integer id;
	private Long reservationId;	//예약관리ID
	private String userEmail;	//사용자ID(이메일ID)
	private String payMoid;	//거래번호
	private String payDate;	//결제 일자
	private String payDivision;	//결제 구분항목(발생,입금,정정)
	private String payMethod;	//결제방법
	private String payComment;	//결제 내용
	private Long payAmount;	//결제 금액
	private String mid;	//상점ID(pg결제)
	private String tid;	//거래ID(pg결제)
	private String statusCode;	//결재상태
	private String statusName;	//결제상태명

	public UserCustomerPayment addReservationId(Long reservationId) {
		this.reservationId = reservationId;
		return this;
	}
	public UserCustomerPayment addPayMoid(String payMoid) {
		this.payMoid = payMoid;
		return this;
	}
	public UserCustomerPayment addUserEmail(String userEmail) {
		this.userEmail = userEmail;
		return this;
	}
	public UserCustomerPayment addPayDivision(String payDivision) {
		this.payDivision = payDivision;
		return this;
	}
	public UserCustomerPayment addPayComment(String payComment) {
		this.payComment = payComment;
		return this;
	}
	public UserCustomerPayment addPayAmount(Long payAmount) {
		this.payAmount = payAmount;
		return this;
	}
	public UserCustomerPayment addCreateId(String createId) {
		this.setCreateId(createId);
		return this;
	}
}
