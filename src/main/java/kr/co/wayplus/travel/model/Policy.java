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
public class Policy extends CommonDataSet {
	private int rownum;
	private Integer id;	//약관 ID
	private String title;	//약관 명
	private String content;	//약관 내용
	private String policyType;	//약관 종류(개인정보, 마케팅,이용약관,등..)
	private String policySubType;	//약관 서브종류 (웰니스캠프용)
	private String policyTypeTitle;	//약관 종류(개인정보, 마케팅,이용약관,등..)
	private String policyVersion = "";	//약관 버전
	private String useYn;	//사용 여부

	// 추가된 컬럼
	private String startDate;

}
