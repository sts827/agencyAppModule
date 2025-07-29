package kr.co.wayplus.travel.model;

// 중기육상예보
public class WeatherMidLandFcstVO {
	private	int midLandFcstId;	// 중기육상예보 ID (PK)

	private	int cityId;		 	// 관측지 ID (FK - weather_kma_cheongju.cityformidlandfcst)
	private	String city;		// 관측지명

	private	String tmFc; 		// 발표일자

	private	int rnSt3Am; 		// 3일 후 오전 강수 확률
	private	int rnSt3Pm; 		// 3일 후 오후 강수 확률
	private	int rnSt4Am; 		// 4일 후 오전 강수 확률
	private	int rnSt4Pm; 		// 4일 후 오후 강수 확률
	private	int rnSt5Am; 		// 5일 후 오전 강수 확률
	private	int rnSt5Pm; 		// 6일 후 오후 강수 확률
	private	int rnSt6Am; 		// 7일 후 오전 강수 확률
	private	int rnSt6Pm; 		// 8일 후 오후 강수 확률
	private	int rnSt7Am; 		// 7일 후 오전 강수 확률
	private	int rnSt7Pm; 		// 7일 후 오후 강수 확률
	private	int rnSt8; 			// 8일 후 강수 확률
	private	int rnSt9; 			// 9일 후 강수 확률
	private	int rnSt10; 		// 10일 후 강수 확률

	private	String wf3Am; 		// 3일 후 오전 날씨예보
	private	String wf3AmImage;
	private	String wf3Pm; 		// 3일 후 오후 날씨예보
	private	String wf3PmImage;
	private	String wf4Am; 		// 4일 후 오전 날씨예보
	private	String wf4AmImage;
	private	String wf4Pm; 		// 4일 후 오후 날씨예보
	private	String wf4PmImage;
	private	String wf5Am; 		// 5일 후 오전 날씨예보
	private	String wf5AmImage;
	private	String wf5Pm; 		// 5일 후 오후 날씨예보
	private	String wf5PmImage;
	private	String wf6Am; 		// 6일 후 오전 날씨예보
	private	String wf6AmImage;
	private	String wf6Pm; 		// 6일 후 오후 날씨예보
	private	String wf6PmImage;
	private	String wf7Am; 		// 7일 후 오전 날씨예보
	private	String wf7AmImage;
	private	String wf7Pm; 		// 7일 후 오후 날씨예보
	private	String wf7PmImage;
	private	String wf8; 		// 8일 후 날씨예보
	private	String wf8Image;
	private	String wf9; 		// 9일 후 날씨예보
	private	String wf9Image;
	private	String wf10; 		// 10일 후 날씨예보
	private	String wf10Image;

	public int getMidLandFcstId() {
		return midLandFcstId;
	}
	public void setMidLandFcstId(int midLandFcstId) {
		this.midLandFcstId = midLandFcstId;
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
	public int getRnSt3Am() {
		return rnSt3Am;
	}
	public void setRnSt3Am(int rnSt3Am) {
		this.rnSt3Am = rnSt3Am;
	}
	public int getRnSt3Pm() {
		return rnSt3Pm;
	}
	public void setRnSt3Pm(int rnSt3Pm) {
		this.rnSt3Pm = rnSt3Pm;
	}
	public int getRnSt4Am() {
		return rnSt4Am;
	}
	public void setRnSt4Am(int rnSt4Am) {
		this.rnSt4Am = rnSt4Am;
	}
	public int getRnSt4Pm() {
		return rnSt4Pm;
	}
	public void setRnSt4Pm(int rnSt4Pm) {
		this.rnSt4Pm = rnSt4Pm;
	}
	public int getRnSt5Am() {
		return rnSt5Am;
	}
	public void setRnSt5Am(int rnSt5Am) {
		this.rnSt5Am = rnSt5Am;
	}
	public int getRnSt5Pm() {
		return rnSt5Pm;
	}
	public void setRnSt5Pm(int rnSt5Pm) {
		this.rnSt5Pm = rnSt5Pm;
	}
	public int getRnSt6Am() {
		return rnSt6Am;
	}
	public void setRnSt6Am(int rnSt6Am) {
		this.rnSt6Am = rnSt6Am;
	}
	public int getRnSt6Pm() {
		return rnSt6Pm;
	}
	public void setRnSt6Pm(int rnSt6Pm) {
		this.rnSt6Pm = rnSt6Pm;
	}
	public int getRnSt7Am() {
		return rnSt7Am;
	}
	public void setRnSt7Am(int rnSt7Am) {
		this.rnSt7Am = rnSt7Am;
	}
	public int getRnSt7Pm() {
		return rnSt7Pm;
	}
	public void setRnSt7Pm(int rnSt7Pm) {
		this.rnSt7Pm = rnSt7Pm;
	}
	public int getRnSt8() {
		return rnSt8;
	}
	public void setRnSt8(int rnSt8) {
		this.rnSt8 = rnSt8;
	}
	public int getRnSt9() {
		return rnSt9;
	}
	public void setRnSt9(int rnSt9) {
		this.rnSt9 = rnSt9;
	}
	public int getRnSt10() {
		return rnSt10;
	}
	public void setRnSt10(int rnSt10) {
		this.rnSt10 = rnSt10;
	}
	public String getWf3Am() {
		return wf3Am;
	}
	public void setWf3Am(String wf3Am) {
		this.wf3Am = wf3Am;
	}
	public String getWf3AmImage() {
		return wf3AmImage;
	}
	public void setWf3AmImage(String wf3AmImage) {
		this.wf3AmImage = wf3AmImage;
	}
	public String getWf3Pm() {
		return wf3Pm;
	}
	public void setWf3Pm(String wf3Pm) {
		this.wf3Pm = wf3Pm;
	}
	public String getWf3PmImage() {
		return wf3PmImage;
	}
	public void setWf3PmImage(String wf3PmImage) {
		this.wf3PmImage = wf3PmImage;
	}
	public String getWf4Am() {
		return wf4Am;
	}
	public void setWf4Am(String wf4Am) {
		this.wf4Am = wf4Am;
	}
	public String getWf4AmImage() {
		return wf4AmImage;
	}
	public void setWf4AmImage(String wf4AmImage) {
		this.wf4AmImage = wf4AmImage;
	}
	public String getWf4Pm() {
		return wf4Pm;
	}
	public void setWf4Pm(String wf4Pm) {
		this.wf4Pm = wf4Pm;
	}
	public String getWf4PmImage() {
		return wf4PmImage;
	}
	public void setWf4PmImage(String wf4PmImage) {
		this.wf4PmImage = wf4PmImage;
	}
	public String getWf5Am() {
		return wf5Am;
	}
	public void setWf5Am(String wf5Am) {
		this.wf5Am = wf5Am;
	}
	public String getWf5AmImage() {
		return wf5AmImage;
	}
	public void setWf5AmImage(String wf5AmImage) {
		this.wf5AmImage = wf5AmImage;
	}
	public String getWf5Pm() {
		return wf5Pm;
	}
	public void setWf5Pm(String wf5Pm) {
		this.wf5Pm = wf5Pm;
	}
	public String getWf5PmImage() {
		return wf5PmImage;
	}
	public void setWf5PmImage(String wf5PmImage) {
		this.wf5PmImage = wf5PmImage;
	}
	public String getWf6Am() {
		return wf6Am;
	}
	public void setWf6Am(String wf6Am) {
		this.wf6Am = wf6Am;
	}
	public String getWf6AmImage() {
		return wf6AmImage;
	}
	public void setWf6AmImage(String wf6AmImage) {
		this.wf6AmImage = wf6AmImage;
	}
	public String getWf6Pm() {
		return wf6Pm;
	}
	public void setWf6Pm(String wf6Pm) {
		this.wf6Pm = wf6Pm;
	}
	public String getWf6PmImage() {
		return wf6PmImage;
	}
	public void setWf6PmImage(String wf6PmImage) {
		this.wf6PmImage = wf6PmImage;
	}
	public String getWf7Am() {
		return wf7Am;
	}
	public void setWf7Am(String wf7Am) {
		this.wf7Am = wf7Am;
	}
	public String getWf7AmImage() {
		return wf7AmImage;
	}
	public void setWf7AmImage(String wf7AmImage) {
		this.wf7AmImage = wf7AmImage;
	}
	public String getWf7Pm() {
		return wf7Pm;
	}
	public void setWf7Pm(String wf7Pm) {
		this.wf7Pm = wf7Pm;
	}
	public String getWf7PmImage() {
		return wf7PmImage;
	}
	public void setWf7PmImage(String wf7PmImage) {
		this.wf7PmImage = wf7PmImage;
	}
	public String getWf8() {
		return wf8;
	}
	public void setWf8(String wf8) {
		this.wf8 = wf8;
	}
	public String getWf8Image() {
		return wf8Image;
	}
	public void setWf8Image(String wf8Image) {
		this.wf8Image = wf8Image;
	}
	public String getWf9() {
		return wf9;
	}
	public void setWf9(String wf9) {
		this.wf9 = wf9;
	}
	public String getWf9Image() {
		return wf9Image;
	}
	public void setWf9Image(String wf9Image) {
		this.wf9Image = wf9Image;
	}
	public String getWf10() {
		return wf10;
	}
	public void setWf10(String wf10) {
		this.wf10 = wf10;
	}
	public String getWf10Image() {
		return wf10Image;
	}
	public void setWf10Image(String wf10Image) {
		this.wf10Image = wf10Image;
	}

	@Override
	public String toString() {
		return "WeatherMidLandFcstVO [midLandFcstId=" + midLandFcstId + ", cityId=" + cityId + ", city=" + city
				+ ", tmFc=" + tmFc + ", rnSt3Am=" + rnSt3Am + ", rnSt3Pm=" + rnSt3Pm + ", rnSt4Am=" + rnSt4Am
				+ ", rnSt4Pm=" + rnSt4Pm + ", rnSt5Am=" + rnSt5Am + ", rnSt5Pm=" + rnSt5Pm + ", rnSt6Am=" + rnSt6Am
				+ ", rnSt6Pm=" + rnSt6Pm + ", rnSt7Am=" + rnSt7Am + ", rnSt7Pm=" + rnSt7Pm + ", rnSt8=" + rnSt8
				+ ", rnSt9=" + rnSt9 + ", rnSt10=" + rnSt10 + ", wf3Am=" + wf3Am + ", wf3AmImage=" + wf3AmImage
				+ ", wf3Pm=" + wf3Pm + ", wf3PmImage=" + wf3PmImage + ", wf4Am=" + wf4Am + ", wf4AmImage=" + wf4AmImage
				+ ", wf4Pm=" + wf4Pm + ", wf4PmImage=" + wf4PmImage + ", wf5Am=" + wf5Am + ", wf5AmImage=" + wf5AmImage
				+ ", wf5Pm=" + wf5Pm + ", wf5PmImage=" + wf5PmImage + ", wf6Am=" + wf6Am + ", wf6AmImage=" + wf6AmImage
				+ ", wf6Pm=" + wf6Pm + ", wf6PmImage=" + wf6PmImage + ", wf7Am=" + wf7Am + ", wf7AmImage=" + wf7AmImage
				+ ", wf7Pm=" + wf7Pm + ", wf7PmImage=" + wf7PmImage + ", wf8=" + wf8 + ", wf8Image=" + wf8Image
				+ ", wf9=" + wf9 + ", wf9Image=" + wf9Image + ", wf10=" + wf10 + ", wf10Image=" + wf10Image + "]";
	}
}
