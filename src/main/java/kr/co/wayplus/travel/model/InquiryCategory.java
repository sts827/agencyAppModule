package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryCategory {
	private Integer id;	//고유번호
	private String title;	//카테고리명
	private String note;	//카테고리 설명
	private int orderNum;	//정렬순서
	private String useYn;	//사용여부
	private String reservationYn;	//예약여부
	private String reservationSwitchYn;	//예약전환여부
	private String groupYn;	//그룹여부
	private String deleteYn;	//삭제여부
	private String startDate = "";	//사용 시작일시
	private String expireDate= ""; 	//사용 종료일시
	private String createId;	//생성자
	private String createDate= "";	//생성일시
	private String lastUpdateId;	//최종수정자
	private String lastUpdateDate= "";	//최종수정일시

	public InquiryCategory addId(Integer _Id) {
		this.id = _Id;
		return this;
	}

	public InquiryCategory addOrderNum(Integer _orderNum) {
		this.id = _orderNum;
		return this;
	}


}
