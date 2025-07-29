package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductTourLink extends CommonDataSet {
	private Integer linkId;	//상품 구성 고유번호
	private String productSerial;	//링크 등록 상품 번호 (FK)
	private Integer linkSequence;	//정렬 순번
	private String linkCategory;	//상품 종류 구분
	private String linkTitle;	//일정 내용
	private String linkNote;	//요약 설명
	private String linkAddress;	//주소 정보
	private String linkProductSerial;	//연결된 상품 시리얼
	private String linkPlaceId;	//연결된 장소 아이디

	private String statusType;
	private String tsTitle;
	private String productTitle;
	private ProductInfo linkProductInfo;
	private PlaceSpot linkPlaceInfo;
}
