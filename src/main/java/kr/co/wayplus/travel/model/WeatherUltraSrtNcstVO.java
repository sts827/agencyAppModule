package kr.co.wayplus.travel.model;

// 초단기 실황
public class WeatherUltraSrtNcstVO {
	int weatherId; 	// 초단기 실황 ID (PK)

	int cityId;		// 관측지 ID (FK - weather_kma_cheongju.ultrasrtncst)

	String baseDate;// 발표일
	String baseTime;// 발표시간

	double pty;		// 강수형태
	double reh;		// 습도
	double rn1;		// 1시간 강수량
	double t1h;		// 기온
	double uuu;		// 동서바람성분
	double vec;		// 풍향
	double vvv;		// 남북바람성분
	double wsd;		// 풍속

	public int getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(int weatherId) {
		this.weatherId = weatherId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
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
	public double getPty() {
		return pty;
	}
	public void setPty(double pty) {
		this.pty = pty;
	}
	public double getReh() {
		return reh;
	}
	public void setReh(double reh) {
		this.reh = reh;
	}
	public double getRn1() {
		return rn1;
	}
	public void setRn1(double rn1) {
		this.rn1 = rn1;
	}
	public double getT1h() {
		return t1h;
	}
	public void setT1h(double t1h) {
		this.t1h = t1h;
	}
	public double getUuu() {
		return uuu;
	}
	public void setUuu(double uuu) {
		this.uuu = uuu;
	}
	public double getVec() {
		return vec;
	}
	public void setVec(double vec) {
		this.vec = vec;
	}
	public double getVvv() {
		return vvv;
	}
	public void setVvv(double vvv) {
		this.vvv = vvv;
	}
	public double getWsd() {
		return wsd;
	}
	public void setWsd(double wsd) {
		this.wsd = wsd;
	}

	@Override
	public String toString() {
		return "UltraSrtNcstVO [weatherId=" + weatherId + ", cityId=" + cityId + ", baseDate=" + baseDate
				+ ", baseTime=" + baseTime + ", pty=" + pty + ", reh=" + reh + ", rn1=" + rn1 + ", t1h=" + t1h
				+ ", uuu=" + uuu + ", vec=" + vec + ", vvv=" + vvv + ", wsd=" + wsd + "]";
	}
}
