package kr.co.wayplus.travel.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//단기예보
public class WeatherVilageFcstVO {

	private	int ultraSrtFcstId;	// 단기예보 ID (PK)

	private	int cityId;			// 관측지 ID (FK - weather_kma_cheongju.cityforvilagefcst)
	private	String city;	 	// 예보지점명
	private	String dong;		// 예보지점

	private	String logoImageNm; //
	private	String description; //

	private	String baseDate;	// 발표일
	private	String baseTime;	// 발표시간

	private	String fcstDate;	// 관측일
	private	String fcstTime;	// 관측시간
	private	Date fdate;	// 관측시간
	private	String datef1;	// 관측시간
	private	String datef2;	// 관측시간
	private	String dayOfWeek;	// 관측시간
	private	String timef1;	// 관측시간
	private	String timef2;	// 관측시간

	private	String nx;			// nx
	private	String ny;			// ny

	private	int pop; 			// 강수확률
	private	int pty; 			// 강수형태
	private	String pcp; 		// 1시간 강수량 (강수없음 : 0)
	private	int reh; 			// 습도
	private	String sno; 		// 1시간 신적설 (적설없음 : 0)
	private	int sky; 			// 하늘상태

	private	double tmp; 		// 1시간 기온
	private	double tmn; 		// 일 최저기온
	private	double tmx; 		// 일 최고기온
	private	double windChillTmp;// 체감온도

	private	double uuu; 		// 풍속(동서성분)
	private	double vvv; 		// 풍속(남북성분)
	private	double wav; 		// 파고

	private	double vec; 		// 풍향
	private	String vecEngStr; 	// 풍향 (영문)
	private	String vecStr; 		// 풍향 (한글)

	private	double wsd;			// 풍속

	private	String weatherType;
	private	String weatherImage;
	private	String weatherNormalImage;
/*
	public int getUltraSrtFcstId() {
		return ultraSrtFcstId;
	}
	public void setUltraSrtFcstId(int ultraSrtFcstId) {
		this.ultraSrtFcstId = ultraSrtFcstId;
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
	public String getDong() {
		return dong;
	}
	public void setDong(String dong) {
		this.dong = dong;
	}
	public String getLogoImageNm() {
		return logoImageNm;
	}
	public void setLogoImageNm(String logoImageNm) {
		this.logoImageNm = logoImageNm;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public String getBaseTime() {
		return baseTime;
	}
	public void setBaseTime(String baseTime) {
		this.baseTime = baseTime;
	}
	public String getFcstDate() {
		return fcstDate;
	}
	public void setFcstDate(String fcstDate) {
		this.fcstDate = fcstDate;
	}
	public String getFcstTime() {
		return fcstTime;
	}
	public void setFcstTime(String fcstTime) {
		this.fcstTime = fcstTime;
	}
	public String getNx() {
		return nx;
	}
	public void setNx(String nx) {
		this.nx = nx;
	}
	public String getNy() {
		return ny;
	}
	public void setNy(String ny) {
		this.ny = ny;
	}
	public int getPop() {
		return pop;
	}
	public void setPop(int pop) {
		this.pop = pop;
	}
	public int getPty() {
		return pty;
	}
	public void setPty(int pty) {
		this.pty = pty;
	}
	public String getPcp() {
		return pcp;
	}
	public void setPcp(String pcp) {
		this.pcp = pcp;
	}
	public int getReh() {
		return reh;
	}
	public void setReh(int reh) {
		this.reh = reh;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public int getSky() {
		return sky;
	}
	public void setSky(int sky) {
		this.sky = sky;
	}
	public double getTmp() {
		return tmp;
	}
	public void setTmp(double tmp) {
		this.tmp = tmp;
	}
	public double getTmn() {
		return tmn;
	}
	public void setTmn(double tmn) {
		this.tmn = tmn;
	}
	public double getTmx() {
		return tmx;
	}
	public void setTmx(double tmx) {
		this.tmx = tmx;
	}
	public double getWindChillTmp() {
		return windChillTmp;
	}
	public void setWindChillTmp(double windChillTmp) {
		this.windChillTmp = windChillTmp;
	}
	public double getUuu() {
		return uuu;
	}
	public void setUuu(double uuu) {
		this.uuu = uuu;
	}
	public double getVvv() {
		return vvv;
	}
	public void setVvv(double vvv) {
		this.vvv = vvv;
	}
	public double getWav() {
		return wav;
	}
	public void setWav(double wav) {
		this.wav = wav;
	}
	public double getVec() {
		return vec;
	}
	public void setVec(double vec) {
		this.vec = vec;
	}
	public String getVecEngStr() {
		return vecEngStr;
	}
	public void setVecEngStr(String vecEngStr) {
		this.vecEngStr = vecEngStr;
	}
	public String getVecStr() {
		return vecStr;
	}
	public void setVecStr(String vecStr) {
		this.vecStr = vecStr;
	}
	public double getWsd() {
		return wsd;
	}
	public void setWsd(double wsd) {
		this.wsd = wsd;
	}
	public String getWeatherType() {
		return weatherType;
	}
	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}
	public String getWeatherImage() {
		return weatherImage;
	}
	public void setWeatherImage(String weatherImage) {
		this.weatherImage = weatherImage;
	}

	public String getWeatherNormalImage() {
		return weatherNormalImage;
	}
	public void setWeatherNormalImage(String weatherNormalImage) {
		this.weatherNormalImage = weatherNormalImage;
	}

	@Override
	public String toString() {
		return "WeatherVilageFcstVO [vilageFcstId=" + ultraSrtFcstId + ", cityId=" + cityId + ", city=" + city + ", dong="
				+ dong + ", logoImageNm=" + logoImageNm + ", description=" + description + ", baseDate=" + baseDate
				+ ", baseTime=" + baseTime + ", fcstDate=" + fcstDate + ", fcstTime=" + fcstTime + ", nx=" + nx
				+ ", ny=" + ny + ", pop=" + pop + ", pty=" + pty + ", pcp=" + pcp + ", reh=" + reh + ", sno=" + sno
				+ ", sky=" + sky + ", tmp=" + tmp + ", tmn=" + tmn + ", tmx=" + tmx + ", windChillTmp=" + windChillTmp
				+ ", uuu=" + uuu + ", vvv=" + vvv + ", wav=" + wav + ", vec=" + vec + ", vecEngStr=" + vecEngStr
				+ ", vecStr=" + vecStr + ", wsd=" + wsd + ", weatherType=" + weatherType + ", weatherImage="
				+ weatherImage + ", weatherNormalImage=" + weatherNormalImage + "]";
	}
*/
}