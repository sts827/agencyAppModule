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
public class CodeItem extends CommonDataSet {
    private int rownum;
    private Integer lev;	//
    private String idpath;	//
    private String pid;	//
    private Integer ccnt;	//

    private String code;	//공통코드ID
    private String upperCode;	//공통코드 일반 ID
    private String name;	//공통코드 일반 이름
    private String codeDesc;	//공통코드 일반 설명
    private Integer codeDepth;	//공통코드 깊이
    private String codeAcronym;	//공통코드 약칭
    private Integer sort;	//코드 정렬순서
    private String useYn;	//사용 여부

}
