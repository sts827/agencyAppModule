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
public class BoardSetting extends CommonDataSet {
	private int rownum;
	private Integer id;	//게시판 아이디
	private String url ="";	//게시판 접속 URL
	private String typeCode;	//게시판 유형 board / faq / album / event
	private String title;	//게시판명
	private String subtitle;	//게시판 부제
	private String thumbnailYn;	//썸네일 사용여부
	private String bannerLargeYn;	//큰 배너 사용 여부
	private String bannerSmallYn;	//작은 배너 사용 여부
	private String categoryYn;	//카테고리 사용 여부
	private String applyCodeYn;	//접수코드 사용여부
	private String secretYn;	//비밀글 사용여부
	private String guestYn;	//비회원 사용여부
	private String commentYn;	//댓글 기능 사용 여부
	private String tagYn;	//태그 기능 사용 여부
	private String favoriteYn;	//게시글 추천 수 기능 사용 여부
	private String scrapYn;	//게시물 스크랩 기능 사용 여부
	private String seriesYn;	//엮인글 기능 사용 여부
	private Integer defaultPageSize;	//기본 페이지 사이즈
	private Integer contentStoreDays;	//콘텐츠 저장 일수 (0:무제한)
	private String useYn;	//게시판 사용 여부
	private String fixYn;	//고정 기능 사용 여부
	private Integer attachFileSize;	//0 무제한, 1이상 개수만큼 제한
	private String startDate;	//게시판 사용 시작일
	private String expireDate;	//게시판 사용 만료일

}
