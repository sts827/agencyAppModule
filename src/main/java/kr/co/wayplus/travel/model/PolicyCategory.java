package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyCategory extends CommonDataSet {
	private int rownum;
	private Integer id;	//고유번호
	private String title;	//카테고리명
	private String note;	//카테고리 설명
	private int orderNum;	//정렬순서
	private String useYn;	//사용여부
	private String startDate;	//사용 시작일시
	private String expireDate;	//사용 종료일시


}
