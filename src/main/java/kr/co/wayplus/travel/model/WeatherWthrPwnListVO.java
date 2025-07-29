package kr.co.wayplus.travel.model;

// 기상 예비특보목록 조회
public class WeatherWthrPwnListVO {
	private static final String weatherType = "twthrPwnList";

	private int wthrPwnListId; 	// 기상 특보목록 ID (PK)
	private int cityId; 		// 관측지 정보 ID (FK)

	private int stnId;			// 지점 ID

	private String fromTmFc; 	// 발표일자 (from - 년월일)
	private String toTmFc; 		// 발표일자 (to - 년월일)

	private String title; 		// 제목
	private int tmSeq; 			// 발표번호(월별)
	private String tmFc; 		// 발표시각 (년월일시분)

	public int getWthrPwnListId() {
		return wthrPwnListId;
	}
	public void setWthrPwnListId(int wthrPwnListId) {
		this.wthrPwnListId = wthrPwnListId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTmSeq() {
		return tmSeq;
	}
	public void setTmSeq(int tmSeq) {
		this.tmSeq = tmSeq;
	}
	public String getTmFc() {
		return tmFc;
	}
	public void setTmFc(String tmFc) {
		this.tmFc = tmFc;
	}
	public static String getWeathertype() {
		return weatherType;
	}

	@Override
	public String toString() {
		return "WeatherWthrPwnListVO [wthrPwnListId=" + wthrPwnListId + ", cityId=" + cityId + ", stnId=" + stnId
				+ ", fromTmFc=" + fromTmFc + ", toTmFc=" + toTmFc + ", title=" + title + ", tmSeq=" + tmSeq + ", tmFc="
				+ tmFc + "]";
	}


}
