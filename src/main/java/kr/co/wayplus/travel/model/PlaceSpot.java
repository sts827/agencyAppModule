package kr.co.wayplus.travel.model;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceSpot {
	private int rownum;
	private Integer tsId;
	private Integer tsStnid;	//사용 STN_ID
	private String areaType;	//관광지 명
	private String placeType;	//관광지 전화번호
	private String useYn;	//관광지 전화번호
	private String tsTitle;	//관광지 명
	private String tsTel1;	//관광지 전화번호
	private String tsManager;	//담당자
	private String tsTel2;	//담당자 전화번호
	private String tsZipcode;	//우편번호
	private String tsRoad;	//도로명 주소
	private String tsJibun;	//지번 주소
	private String tsDetail;	//상세 주소
	private String tsExtra;	//주소 법정동
	private String tsLocationCode;	//지역구분코드
	private Float tsLatitude;	//위도
	private Float tsLongitude;	//경도
	private String tsCompanynum;	//사업자번호
	private Integer tsEmployee;	//직원수
	private String tsHomepage;	//홈페이지
	private Integer tsInside;	//실내비중
	private String tsSummary;	//개요
	private String tsHelp;	//문의 및 안내
	private Date tsBirthday;	//개장일
	private String tsHoliday;	//휴무일
	private String tsExiting;	//체험안내
	private Integer tsExitingInwon;	//체험가능인원
	private Time tsStart;	//개장 시간
	private Time tsFinish;	//패장 시간
	private Integer tsPersonnel;	//수용인원
	private String tsUseday;	//체험이용시기
	private String tsUsetime;	//체험이용시간
	private String tsInfo;	//상세정보
	private String tsYoutubeCode;	//유투브영상코드
	private String tsAppend;	//관광지사진
	private String tsWriteId;	//최초등록자아이디
	private String tsUdateId;	//마지막수정아이디
	private String tsWritdate;	//등록일
	private Float tsMale;	//남성
	private Float tsFemale;	//여성
	private Float ts10s;	//10대
	private Float ts20s;	//20대
	private Float ts30s;	//30대
	private Float ts40s;	//40대
	private Float ts50s;	//50대
	private Float ts60s;	//60대
	private Float tsRecreation;	//목적 휴양 자연
	private Float tsEpicurism;	//목적 맛집 탐방
	private Float tsExperience;	//목적 체험 활동
	private Float tsCulture;	//목적 문화, 예술, 축제
	private Float tsLeports;	//목적 레포츠
	private Float tsOther;	//목적 기타
	private Float tsAlone;	//동행 단독
	private Float tsFriend;	//동행 친구
	private Float tsCouple;	//동행 연인
	private Float tsParents;	//동행 부모
	private Float tsChild;	//동행 아동
	private Float tsGroup;	//동행 단체
	private Integer tsCategory1;	//카테고리 자연
	private Integer tsCategory2;	//카테고리 문화/예술/역사
	private Integer tsCategory3;	//카테고리 레포츠
	private Integer tsCategory4;	//카테고리 음식
	private Integer tsCategory5;	//카테고리 공연/행사
	private String tsPets;	//반려동물 입장 가능 여부 Y/N
	private String tsParking;	//주차 가능 여부 Y/N
	private String tsDisabled;	//장애인 여부 Y/N
	private String tsNursing;	//수유실 여부 Y/N
	private String tsStroller;	//유모자 입장 가능 여부 Y/N
	private String tsCredit;	//신용카드 사용 가능 여부 Y/N
	private Float tsWeatherRain;	//우천시 추천지수
	private Float tsWeatherSnow;	//눈올때 추천비율
	private Float tsWeatherFog;	//안개낄때 추천비율
	private Float tsWeatherGale;	//강풍일때 추천비율
	private Float tsSeasonSpring;	//봄일때 추천비율
	private Float tsSeasonSummer;	//여름일때 추천비율
	private Float tsSeasonAutumn;	//가을일때 추천비율
	private Float tsSeasonWinter;	//겨울일때 추천비율
	private Date tsEditdate;	//최종 수정일
	private String tsEnrolChk;
	private Integer tsEvid;	//EV패스 연계아이디
	private String tsDeleteYn;
	private String contentId;

	private Integer gisMapLevel;

    private String strTsBirthday;	//개장일
    private String strTsStart;	//개장 시간
    private String strTsFinish;	//패장 시간

	private String golfStrokes;
	private String golfLevel;
	private String groundType;
	private String par;
	private String groundLength;
	private String playHole;
	private String amenities;

    private double distance;

    private boolean isFavorite;
}
