package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryContent {
	private Long id;	//고유번호
	private String userEmail;	//제목
	private Integer categoryId;	//카테고리 아이디
	private String title;	//제목
	private String productSerial;
	private String content;	//내용
	private String contentSecretNumber;
	private String comment;	//답변내용
	private String commentDate;	//답변시간
	private String applyCode;	//접수상태코드(0:접수, 1:접수확인,2:답변완료)
	private String consultingMethod;	//상담방법(전화,이메일,문자,카카오톡,등)
	private String tags;	//관련 태그
	private String secretYn;	//비밀글
	private String reservationYn;	//예약구분
	private String reservationCode;	//예약구분
	private String cancelYn;	//예약구분
	private String cancelCode;	//예약구분
	private String groupJson;	//단체종류
	private String airJson;	//항공
	private String vehicleJson;	//차량
	private String berthJson;	//숙소
	private String travelStartingPoint;	//일정_출발지
	private String travelScheduleJson;	//여행_스케줄(json)
	private String airTypeRequest;	//항공 종류 요청(0 : 왕복, 1 : 출발만, 2 : 복귀만, 3 : 요청안함)
	private String airScheduleJson;	//항공_스케줄(json)
	private String vehicleType;	//차량_종류
	private Integer vehicleCount;	//차량_대수
	private String berthType;	//숙소_종류
	private String customerName;	//고객 이름(비회원 직접입력, 회원 user 테이블)
	private String customerPass;	//고객 비밀번호(비회원 전용)
	private String customerPhone;	//고객 연락처(비회원 직접입력, 회원 user 테이블)
	private String customerEmail;	//고객 이메일(비회원 직접입력, 회원 user 테이블)
	private String createId;	//생성자
	private String createDate;	//생성일시
	private String createDateFormat;
	private String lastUpdateId;	//최종수정자
	private String lastUpdateDate;	//최종수정일시
	private String deleteYn;	//삭제여부
	private String deleteId;	//삭제자
	private String priceOptionJson;

	/*실제 없는 컬럼(join,가상컬럼)*/
	private Long rownum;	//고유번호
	private String customerNameM;	//고객 이름(비회원 직접입력, 회원 user 테이블)
	private String productThumbnail; //
	private String productTitle;
	private String categoryName;	//카테고리 아이디
	private String applyCodeName;	//접수상태코드이름
	private String reservationCodeName;
	private String cancelCodeName;
    private String commentTime;

	//	추가된 필드
	private String customerType;

    private String userName;
    private String userMobile;
}
