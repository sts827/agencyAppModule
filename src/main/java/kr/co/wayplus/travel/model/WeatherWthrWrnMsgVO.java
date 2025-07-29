package kr.co.wayplus.travel.model;

// 기상 특보통보문 조회
public class WeatherWthrWrnMsgVO {

	private int wthrWrnMsgId; 	// 기상 특보통보문 ID (PK)

	private int cityId; 		// 관측지 정보 ID (FK)
	private	String city;	 	// 예보지점명

	private int stnId;			// 지점 ID

	private String fromTmFc; 	// 발표일자 (from - 년월일)
	private String toTmFc; 		// 발표일자 (to - 년월일)

	private String tmFc; 		// 발표시각 (년월일시분)
	private int tmSeq; 			// 발표번호(월별)

	private String warFc; 		// 특보발표코드 : 발표 (1로 고정)

	private String t1; 			// 제목
	private String t2; 			// 해당구역
	private String t3; 			// 발효시각
	private String t4; 			// 내용
	private String t5; 			// 특보발효현황시각
	private String t6; 			// 특보발효현황내용
	private String t7; 			// 예비특보

	private String other;		// 참고사항

	public int getWthrWrnMsgId() {
		return wthrWrnMsgId;
	}

	public void setWthrWrnMsgId(int wthrWrnMsgId) {
		this.wthrWrnMsgId = wthrWrnMsgId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getStnId() {
		return stnId;
	}

	public void setStnId(int stnId) {
		this.stnId = stnId;
	}

	public String getFromTmFc() {
		return fromTmFc;
	}

	public void setFromTmFc(String fromTmFc) {
		this.fromTmFc = fromTmFc;
	}

	public String getToTmFc() {
		return toTmFc;
	}

	public void setToTmFc(String toTmFc) {
		this.toTmFc = toTmFc;
	}

	public String getTmFc() {
		return tmFc;
	}

	public void setTmFc(String tmFc) {
		this.tmFc = tmFc;
	}

	public int getTmSeq() {
		return tmSeq;
	}

	public void setTmSeq(int tmSeq) {
		this.tmSeq = tmSeq;
	}

	public String getWarFc() {
		return warFc;
	}

	public void setWarFc(String warFc) {
		this.warFc = warFc;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public String getT3() {
		return t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getT4() {
		return t4;
	}

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public String getT5() {
		return t5;
	}

	public void setT5(String t5) {
		this.t5 = t5;
	}

	public String getT6() {
		return t6;
	}

	public void setT6(String t6) {
		this.t6 = t6;
	}

	public String getT7() {
		return t7;
	}

	public void setT7(String t7) {
		this.t7 = t7;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "WeatherWthrWrnMsgVO [wthrWrnMsgId=" + wthrWrnMsgId + ", cityId=" + cityId + ", city=" + city
				+ ", stnId=" + stnId + ", fromTmFc=" + fromTmFc + ", toTmFc=" + toTmFc + ", tmFc=" + tmFc + ", tmSeq="
				+ tmSeq + ", warFc=" + warFc + ", t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + ", t4=" + t4 + ", t5=" + t5
				+ ", t6=" + t6 + ", t7=" + t7 + ", other=" + other + "]";
	}



}
