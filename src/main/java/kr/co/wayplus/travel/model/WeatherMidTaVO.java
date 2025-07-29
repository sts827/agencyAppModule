package kr.co.wayplus.travel.model;

// 중기기온조회
public class WeatherMidTaVO {
	private static final String weatherType = "midTa";

	private	int midTiaId;		// 중기기온 정보 ID (PK)

	private	int cityId;		 	// 관측지 ID (FK - weather_kma_cheongju.cityformidta)
	private	String city;		// 관측지명

	private String tmFc; 		// 발표일자

	private String taMin3; 		// 3일 후 예상최저기온(℃)
	private String taMin3Low; 	// 3일 후 예상최저기온 하한 범위
	private String taMin3High; 	// 3일 후 예상최저기온 상한 범위
	private String taMax3; 		// 3일 후 예상최고기온(℃)
	private String taMax3Low; 	// 3일 후 예상최고기온 하한 범위
	private String taMax3High; 	// 3일 후 예상최고기온 상한 범위

	private String taMin4; 		// 4일 후 예상최저기온(℃)
	private String taMin4Low; 	// 4일 후 예상최저기온 하한 범위
	private String taMin4High; 	// 4일 후 예상최저기온 상한 범위
	private String taMax4; 		// 4일 후 예상최고기온(℃)
	private String taMax4Low; 	// 4일 후 예상최고기온 하한 범위
	private String taMax4High; 	// 4일 후 예상최고기온 상한 범위

	private String taMin5; 		// 5일 후 예상최저기온(℃)
	private String taMin5Low; 	// 5일 후 예상최저기온 하한 범위
	private String taMin5High; 	// 5일 후 예상최저기온 상한 범위
	private String taMax5; 		// 5일 후 예상최고기온(℃)
	private String taMax5Low; 	// 5일 후 예상최고기온 하한 범위
	private String taMax5High; 	// 5일 후 예상최고기온 상한 범위

	private String taMin6; 		// 6일 후 예상최저기온(℃)
	private String taMin6Low; 	// 6일 후 예상최저기온 하한 범위
	private String taMin6High; 	// 6일 후 예상최저기온 상한 범위
	private String taMax6; 		// 6일 후 예상최고기온(℃)
	private String taMax6Low; 	// 6일 후 예상최고기온 하한 범위
	private String taMax6High; 	// 6일 후 예상최고기온 상한 범위

	private String taMin7; 		// 7일 후 예상최저기온(℃)
	private String taMin7Low; 	// 7일 후 예상최저기온 하한 범위
	private String taMin7High; 	// 7일 후 예상최저기온 상한 범위
	private String taMax7; 		// 7일 후 예상최고기온(℃)
	private String taMax7Low; 	// 7일 후 예상최고기온 하한 범위
	private String taMax7High; 	// 7일 후 예상최고기온 상한 범위

	private String taMin8; 		// 8일 후 예상최저기온(℃)
	private String taMin8Low; 	// 8일 후 예상최저기온 하한 범위
	private String taMin8High; 	// 8일 후 예상최저기온 상한 범위
	private String taMax8; 		// 8일 후 예상최고기온(℃)
	private String taMax8Low; 	// 8일 후 예상최고기온 하한 범위
	private String taMax8High; 	// 8일 후 예상최고기온 상한 범위

	private String taMin9; 		// 9일 후 예상최저기온(℃)
	private String taMin9Low; 	// 9일 후 예상최저기온 하한 범위
	private String taMin9High; 	// 9일 후 예상최저기온 상한 범위
	private String taMax9; 		// 9일 후 예상최고기온(℃)
	private String taMax9Low; 	// 9일 후 예상최고기온 하한 범위
	private String taMax9High; 	// 9일 후 예상최고기온 상한 범위

	private String taMin10; 	// 10일 후 예상최저기온(℃)
	private String taMin10Low; 	// 10일 후 예상최저기온 하한 범위
	private String taMin10High; // 10일 후 예상최저기온 상한 범위
	private String taMax10; 	// 10일 후 예상최고기온(℃)
	private String taMax10Low; 	// 10일 후 예상최고기온 하한 범위
	private String taMax10High; // 10일 후 예상최고기온 상한 범위

	public int getMidTiaId() {
		return midTiaId;
	}
	public void setMidTiaId(int midTiaId) {
		this.midTiaId = midTiaId;
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
	public String getTmFc() {
		return tmFc;
	}
	public void setTmFc(String tmFc) {
		this.tmFc = tmFc;
	}
	public String getTaMin3() {
		return taMin3;
	}
	public void setTaMin3(String taMin3) {
		this.taMin3 = taMin3;
	}
	public String getTaMin3Low() {
		return taMin3Low;
	}
	public void setTaMin3Low(String taMin3Low) {
		this.taMin3Low = taMin3Low;
	}
	public String getTaMin3High() {
		return taMin3High;
	}
	public void setTaMin3High(String taMin3High) {
		this.taMin3High = taMin3High;
	}
	public String getTaMax3() {
		return taMax3;
	}
	public void setTaMax3(String taMax3) {
		this.taMax3 = taMax3;
	}
	public String getTaMax3Low() {
		return taMax3Low;
	}
	public void setTaMax3Low(String taMax3Low) {
		this.taMax3Low = taMax3Low;
	}
	public String getTaMax3High() {
		return taMax3High;
	}
	public void setTaMax3High(String taMax3High) {
		this.taMax3High = taMax3High;
	}
	public String getTaMin4() {
		return taMin4;
	}
	public void setTaMin4(String taMin4) {
		this.taMin4 = taMin4;
	}
	public String getTaMin4Low() {
		return taMin4Low;
	}
	public void setTaMin4Low(String taMin4Low) {
		this.taMin4Low = taMin4Low;
	}
	public String getTaMin4High() {
		return taMin4High;
	}
	public void setTaMin4High(String taMin4High) {
		this.taMin4High = taMin4High;
	}
	public String getTaMax4() {
		return taMax4;
	}
	public void setTaMax4(String taMax4) {
		this.taMax4 = taMax4;
	}
	public String getTaMax4Low() {
		return taMax4Low;
	}
	public void setTaMax4Low(String taMax4Low) {
		this.taMax4Low = taMax4Low;
	}
	public String getTaMax4High() {
		return taMax4High;
	}
	public void setTaMax4High(String taMax4High) {
		this.taMax4High = taMax4High;
	}
	public String getTaMin5() {
		return taMin5;
	}
	public void setTaMin5(String taMin5) {
		this.taMin5 = taMin5;
	}
	public String getTaMin5Low() {
		return taMin5Low;
	}
	public void setTaMin5Low(String taMin5Low) {
		this.taMin5Low = taMin5Low;
	}
	public String getTaMin5High() {
		return taMin5High;
	}
	public void setTaMin5High(String taMin5High) {
		this.taMin5High = taMin5High;
	}
	public String getTaMax5() {
		return taMax5;
	}
	public void setTaMax5(String taMax5) {
		this.taMax5 = taMax5;
	}
	public String getTaMax5Low() {
		return taMax5Low;
	}
	public void setTaMax5Low(String taMax5Low) {
		this.taMax5Low = taMax5Low;
	}
	public String getTaMax5High() {
		return taMax5High;
	}
	public void setTaMax5High(String taMax5High) {
		this.taMax5High = taMax5High;
	}
	public String getTaMin6() {
		return taMin6;
	}
	public void setTaMin6(String taMin6) {
		this.taMin6 = taMin6;
	}
	public String getTaMin6Low() {
		return taMin6Low;
	}
	public void setTaMin6Low(String taMin6Low) {
		this.taMin6Low = taMin6Low;
	}
	public String getTaMin6High() {
		return taMin6High;
	}
	public void setTaMin6High(String taMin6High) {
		this.taMin6High = taMin6High;
	}
	public String getTaMax6() {
		return taMax6;
	}
	public void setTaMax6(String taMax6) {
		this.taMax6 = taMax6;
	}
	public String getTaMax6Low() {
		return taMax6Low;
	}
	public void setTaMax6Low(String taMax6Low) {
		this.taMax6Low = taMax6Low;
	}
	public String getTaMax6High() {
		return taMax6High;
	}
	public void setTaMax6High(String taMax6High) {
		this.taMax6High = taMax6High;
	}
	public String getTaMin7() {
		return taMin7;
	}
	public void setTaMin7(String taMin7) {
		this.taMin7 = taMin7;
	}
	public String getTaMin7Low() {
		return taMin7Low;
	}
	public void setTaMin7Low(String taMin7Low) {
		this.taMin7Low = taMin7Low;
	}
	public String getTaMin7High() {
		return taMin7High;
	}
	public void setTaMin7High(String taMin7High) {
		this.taMin7High = taMin7High;
	}
	public String getTaMax7() {
		return taMax7;
	}
	public void setTaMax7(String taMax7) {
		this.taMax7 = taMax7;
	}
	public String getTaMax7Low() {
		return taMax7Low;
	}
	public void setTaMax7Low(String taMax7Low) {
		this.taMax7Low = taMax7Low;
	}
	public String getTaMax7High() {
		return taMax7High;
	}
	public void setTaMax7High(String taMax7High) {
		this.taMax7High = taMax7High;
	}
	public String getTaMin8() {
		return taMin8;
	}
	public void setTaMin8(String taMin8) {
		this.taMin8 = taMin8;
	}
	public String getTaMin8Low() {
		return taMin8Low;
	}
	public void setTaMin8Low(String taMin8Low) {
		this.taMin8Low = taMin8Low;
	}
	public String getTaMin8High() {
		return taMin8High;
	}
	public void setTaMin8High(String taMin8High) {
		this.taMin8High = taMin8High;
	}
	public String getTaMax8() {
		return taMax8;
	}
	public void setTaMax8(String taMax8) {
		this.taMax8 = taMax8;
	}
	public String getTaMax8Low() {
		return taMax8Low;
	}
	public void setTaMax8Low(String taMax8Low) {
		this.taMax8Low = taMax8Low;
	}
	public String getTaMax8High() {
		return taMax8High;
	}
	public void setTaMax8High(String taMax8High) {
		this.taMax8High = taMax8High;
	}
	public String getTaMin9() {
		return taMin9;
	}
	public void setTaMin9(String taMin9) {
		this.taMin9 = taMin9;
	}
	public String getTaMin9Low() {
		return taMin9Low;
	}
	public void setTaMin9Low(String taMin9Low) {
		this.taMin9Low = taMin9Low;
	}
	public String getTaMin9High() {
		return taMin9High;
	}
	public void setTaMin9High(String taMin9High) {
		this.taMin9High = taMin9High;
	}
	public String getTaMax9() {
		return taMax9;
	}
	public void setTaMax9(String taMax9) {
		this.taMax9 = taMax9;
	}
	public String getTaMax9Low() {
		return taMax9Low;
	}
	public void setTaMax9Low(String taMax9Low) {
		this.taMax9Low = taMax9Low;
	}
	public String getTaMax9High() {
		return taMax9High;
	}
	public void setTaMax9High(String taMax9High) {
		this.taMax9High = taMax9High;
	}
	public String getTaMin10() {
		return taMin10;
	}
	public void setTaMin10(String taMin10) {
		this.taMin10 = taMin10;
	}
	public String getTaMin10Low() {
		return taMin10Low;
	}
	public void setTaMin10Low(String taMin10Low) {
		this.taMin10Low = taMin10Low;
	}
	public String getTaMin10High() {
		return taMin10High;
	}
	public void setTaMin10High(String taMin10High) {
		this.taMin10High = taMin10High;
	}
	public String getTaMax10() {
		return taMax10;
	}
	public void setTaMax10(String taMax10) {
		this.taMax10 = taMax10;
	}
	public String getTaMax10Low() {
		return taMax10Low;
	}
	public void setTaMax10Low(String taMax10Low) {
		this.taMax10Low = taMax10Low;
	}
	public String getTaMax10High() {
		return taMax10High;
	}
	public void setTaMax10High(String taMax10High) {
		this.taMax10High = taMax10High;
	}
	public static String getWeathertype() {
		return weatherType;
	}

	@Override
	public String toString() {
		return "WeatherMidTaVO [midTiaId=" + midTiaId + ", cityId=" + cityId + ", city=" + city + ", tmFc=" + tmFc
				+ ", taMin3=" + taMin3 + ", taMin3Low=" + taMin3Low + ", taMin3High=" + taMin3High + ", taMax3="
				+ taMax3 + ", taMax3Low=" + taMax3Low + ", taMax3High=" + taMax3High + ", taMin4=" + taMin4
				+ ", taMin4Low=" + taMin4Low + ", taMin4High=" + taMin4High + ", taMax4=" + taMax4 + ", taMax4Low="
				+ taMax4Low + ", taMax4High=" + taMax4High + ", taMin5=" + taMin5 + ", taMin5Low=" + taMin5Low
				+ ", taMin5High=" + taMin5High + ", taMax5=" + taMax5 + ", taMax5Low=" + taMax5Low + ", taMax5High="
				+ taMax5High + ", taMin6=" + taMin6 + ", taMin6Low=" + taMin6Low + ", taMin6High=" + taMin6High
				+ ", taMax6=" + taMax6 + ", taMax6Low=" + taMax6Low + ", taMax6High=" + taMax6High + ", taMin7="
				+ taMin7 + ", taMin7Low=" + taMin7Low + ", taMin7High=" + taMin7High + ", taMax7=" + taMax7
				+ ", taMax7Low=" + taMax7Low + ", taMax7High=" + taMax7High + ", taMin8=" + taMin8 + ", taMin8Low="
				+ taMin8Low + ", taMin8High=" + taMin8High + ", taMax8=" + taMax8 + ", taMax8Low=" + taMax8Low
				+ ", taMax8High=" + taMax8High + ", taMin9=" + taMin9 + ", taMin9Low=" + taMin9Low + ", taMin9High="
				+ taMin9High + ", taMax9=" + taMax9 + ", taMax9Low=" + taMax9Low + ", taMax9High=" + taMax9High
				+ ", taMin10=" + taMin10 + ", taMin10Low=" + taMin10Low + ", taMin10High=" + taMin10High + ", taMax10="
				+ taMax10 + ", taMax10Low=" + taMax10Low + ", taMax10High=" + taMax10High + "]";
	}
}
