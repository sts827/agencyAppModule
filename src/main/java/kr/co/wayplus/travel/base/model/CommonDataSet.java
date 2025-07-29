package kr.co.wayplus.travel.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Database 공통 Creator 정보
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonDataSet {
    private String createId;	//생성자
    private String createDate;	//생성일시
    private String createDateFormat;	//생성일시
    private String lastUpdateId;	//최종수정자
    private String lastUpdateDate;	//최종수정일시
    private String deleteYn;	//삭제 여부
    private String deleteId;	//삭제자
    private String deleteDate;	//삭제일시
}
