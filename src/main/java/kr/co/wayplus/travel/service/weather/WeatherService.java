package kr.co.wayplus.travel.service.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.weather.WeatherMapper;
import kr.co.wayplus.travel.model.WeatherMidLandFcstVO;
import kr.co.wayplus.travel.model.WeatherMidTaVO;
import kr.co.wayplus.travel.model.WeatherUltraSrtNcstVO;
import kr.co.wayplus.travel.model.WeatherVilageFcstVO;
import kr.co.wayplus.travel.model.WeatherWthrPwnListVO;
import kr.co.wayplus.travel.model.WeatherWthrWrnListVO;
import kr.co.wayplus.travel.model.WeatherWthrWrnMsgVO;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
public class WeatherService  {

    private final WeatherMapper weatherMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final static int TO_GRID = 0, TO_GPS = 1;

    @Autowired
    public WeatherService(WeatherMapper weatherMapper) {
        this.weatherMapper = weatherMapper;
    }

// // MidLandRegId로 현재 시각 중기육상예보 정보 가져오기
// 	public WeatherMidLandFcstVO getCurrentWeatherMidLandFcstIntoByMidLandRegId(String midLandRegId) {
// 		// fcstDate, fcstTime 셋팅
// 		Date date = new Date(System.currentTimeMillis());
// 		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
// 		String dateStr = formatter.format(date);
// 		HashMap<String, Object> tmFc = getBaseDateAndTimeForMid(dateStr);
//
// 		HashMap<String, Object> param = new HashMap<String, Object>();
// 		param.put("midLandRegId", midLandRegId);
// 		param.put("tmFc", tmFc.get("tmFcDate").toString()+tmFc.get("tmFcTime").toString());
//
// 		WeatherMidLandFcstVO weatherMidLandFcstInfo = weatherMapper.selectCurrentWeatherMidLandFcstIntoByMidLandRegId(param);
// 		if (weatherMidLandFcstInfo == null) { // 데이터가 없을 때 이전의 baseDate를 읽어온다
// 			SimpleDateFormat formatYmd = new SimpleDateFormat("yyyyMMdd");
// 			SimpleDateFormat formatH = new SimpleDateFormat("HH"); // 시
//
// 			Calendar cal = Calendar.getInstance();
// 			cal.setTime(date);
// 			cal.add(Calendar.HOUR, -1);
// 			String beforeYmd = formatYmd.format(cal.getTime());
// 			String beforeH = formatH.format(cal.getTime());
// 			String beforeDate = beforeYmd+beforeH+"0000"; // 년월일시분초
//
// 			HashMap<String, Object> beforeTmFc = getBaseDateAndTimeForMid(beforeDate);
//
// 			HashMap<String, Object> param2 = new HashMap<String, Object>();
// 			param2.put("midLandRegId", midLandRegId);
// 			param2.put("tmFc", beforeTmFc.get("tmFcDate").toString()+beforeTmFc.get("tmFcTime").toString());
//
// 			weatherMidLandFcstInfo = weatherMapper.selectCurrentWeatherMidLandFcstIntoByMidLandRegId(param2);
// 		}
//
// 		weatherMidLandFcstInfo.setWf3AmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf3Am()));
// 		weatherMidLandFcstInfo.setWf3PmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf3Pm()));
//
// 		weatherMidLandFcstInfo.setWf4AmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf4Am()));
// 		weatherMidLandFcstInfo.setWf4PmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf4Pm()));
//
// 		weatherMidLandFcstInfo.setWf5AmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf5Am()));
// 		weatherMidLandFcstInfo.setWf5PmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf5Pm()));
//
// 		weatherMidLandFcstInfo.setWf6AmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf6Am()));
// 		weatherMidLandFcstInfo.setWf6PmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf6Pm()));
//
// 		weatherMidLandFcstInfo.setWf7AmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf7Am()));
// 		weatherMidLandFcstInfo.setWf7PmImage(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf7Pm()));
//
// 		weatherMidLandFcstInfo.setWf8Image(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf8()));
// 		weatherMidLandFcstInfo.setWf9Image(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf9()));
// 		weatherMidLandFcstInfo.setWf10Image(convertWeatherInfoForMid(weatherMidLandFcstInfo.getWf10()));
//
// 		return weatherMidLandFcstInfo;
// 	}

 // 위도, 경도로 전체 단기예보 정보 가져오기
 	public ArrayList<WeatherVilageFcstVO> getWeatherVilageFcstInfoListIntoByLatLon(double lat, double lon) {
 		// baseDate, baseTime 셋팅 시작
 		Date date = new Date(System.currentTimeMillis());
 		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
 		SimpleDateFormat formatYmd = new SimpleDateFormat("yyyyMMdd");
 		SimpleDateFormat formatH = new SimpleDateFormat("HH"); // 시

 		HashMap<String, Object> param1 = getBaseDateAndTimeForVilageFcst(format.format(date));

 		// GPS to NXNY
 	    HashMap<String, Object> latLngHashMap = convertGRID_GPS(TO_GRID, lat, lon);

 		int old_nx = (Integer) latLngHashMap.get("nx");
 		int old_ny = (Integer) latLngHashMap.get("ny");


 		// nx, ny가 있는 지 확인 후 관측지 정보 ID, 관측지명, 변환된 nx, ny 정보 가져오기
 		HashMap<String, String> conversionNxNyHashMap = getConversionNxNyIntoByOldNxNyForVilageFcst("vilagefcst", lat, lon, old_nx, old_ny);

 		if( conversionNxNyHashMap.get("result") == "null" ) {
 			return null;
 		} else {
	 		int nx = Integer.parseInt(String.valueOf(conversionNxNyHashMap.get("nx")));
	 		int ny = Integer.parseInt(String.valueOf(conversionNxNyHashMap.get("ny")));
	 		int cityId = Integer.parseInt(String.valueOf(conversionNxNyHashMap.get("city_id")));

	 		param1.put("old_nx", nx);
	 		param1.put("old_ny", ny);
	 		param1.put("cityId", cityId);

	 		ArrayList<WeatherVilageFcstVO> weatherVilageFcstInfoList = weatherMapper.selectWeatherVilageFcstInfoListIntoByCityId(param1);
	 		if (Objects.isNull(weatherVilageFcstInfoList) || weatherVilageFcstInfoList.size() == 0) { // 데이터가 없을 때 한 시간 이전의 baseDate를 읽어온다
	 			Calendar cal = Calendar.getInstance();
	 			cal.setTime(date);
	 			cal.add(Calendar.HOUR, -1);
	 			String beforeYmd = formatYmd.format(cal.getTime());
	 			String beforeH = formatH.format(cal.getTime());
	 			String beforeDate = beforeYmd+beforeH+"0000"; // 년월일시분초

	 			HashMap<String, Object> param2 = getBaseDateAndTimeForVilageFcst(beforeDate);

	 			param2.put("old_nx", latLngHashMap.get("nx"));
	 			param2.put("old_ny", latLngHashMap.get("ny"));
	 			param2.put("cityId", cityId);

	 			weatherVilageFcstInfoList = weatherMapper.selectWeatherVilageFcstInfoListIntoByCityId(param2);
	 		}

	 		for (WeatherVilageFcstVO weatherVilageFcstInfo : weatherVilageFcstInfoList) {
	 			weatherVilageFcstInfo = convertWeatherInfoForVilageFcst(weatherVilageFcstInfo, "detail");
	 	    }
	 	    return weatherVilageFcstInfoList;
 		}

 	}

 // 중기예보 BaseDate 셋팅
// 	private HashMap<String, Object> getBaseDateAndTimeForMid(String dateStr) {
// 		HashMap<String, Object> param = new HashMap<String, Object>();
//
// 		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
// 		Date date;
// 		try {
// 			date = formatter.parse(dateStr);
//
// 			SimpleDateFormat formatYmd = new SimpleDateFormat("yyyyMMdd");
// 			SimpleDateFormat formatH = new SimpleDateFormat("HH"); // 시
//
// 			String tmFcDate = formatYmd.format(date);		// 년월일
// 			String tmFcTime = null; 			// 시
// 			String tmFcMinute = "00"; 	// 분
//
// 			int iTempHour = Integer.parseInt(formatH.format(date));
//
// 			//////
// 			if (iTempHour >= 0 &&  iTempHour <= 5) {  // 0시~5시까지는 전날 18시를 BaseTime으로 셋팅함
// 				Calendar cal = Calendar.getInstance();
// 				cal.setTime(date);
// 				cal.add(Calendar.DATE, -1);
// 				String beforeYmd = formatYmd.format(cal.getTime());
//
// 				tmFcDate = beforeYmd;
// 				tmFcTime = "18" + tmFcMinute;
// 			}
// 			else if (iTempHour >= 5 && iTempHour < 18) {
// 				tmFcTime = "06" + tmFcMinute;
// 			}
// 			else if (iTempHour >= 18 && iTempHour <= 23) {
// 				tmFcTime = "18" + tmFcMinute;
// 			}
//
// 			param.put("tmFcDate", tmFcDate);
// 			param.put("tmFcTime", tmFcTime);
//
// 		} catch (ParseException e) {
// 			// TODO Auto-generated catch block
// 			//e.printStackTrace();
// 		}
//
// 		return param;
// 	}

 // 중기예보 날씨 정보 표출용으로 셋팅
// 	private String convertWeatherInfoForMid (String weatherStr) {
// 		if (weatherStr.isEmpty()) {
// 			return "";
// 		}
//     	String weatherImage = "";
//
//     	if (weatherStr.equals("맑음")) {
//     		weatherImage = "NB01";
//     	}
//     	else if (weatherStr.equals("구름조금")) {
//     		weatherImage = "NB02";
//     	}
//     	else if (weatherStr.equals("구름많음")) {
//     		weatherImage = "NB03";
//     	}
//     	else if (weatherStr.equals("흐림")) {
//     		weatherImage = "NB04";
//     	}
//     	else if (weatherStr.equals("소나기")) {
//     		weatherImage = "NB07";
//     	}
//     	else if (weatherStr.equals("비") || weatherStr.equals("흐리고 비")) {
//     		weatherImage = "NB08";
//     	}
//     	else if (weatherStr.equals("가끔 비, 한때 비")) {
//     		weatherImage = "NB20";
//     	}
//     	else if (weatherStr.equals("눈")) {
//     		weatherImage = "NB11";
//     	}
//     	else if (weatherStr.equals("가끔 눈, 한때 눈")) {
//     		weatherImage = "NB21";
//     	}
//     	else if (weatherStr.equals("비 또는 눈")) {
//     		weatherImage = "NB12";
//     	}
//     	else if (weatherStr.equals("가끔 비 또는 눈") || weatherStr.equals("한때 비 또는 눈")) {
//     		weatherImage = "NB22";
//     	}
//     	else if (weatherStr.equals("눈 또는 비")) {
//     		weatherImage = "NB13";
//     	}
//     	else if (weatherStr.equals("가끔 눈 또는 비") || weatherStr.equals("한때 눈 또는 비")) {
//     		weatherImage = "NB23";
//     	}
//     	else if (weatherStr.equals("낙뢰")) {
//     		weatherImage = "NB14";
//     	}
//     	else if (weatherStr.equals("빗방울")) {
//     		weatherImage = "NB10";
//     	}
//     	else if (weatherStr.equals("연무")) {
//     		weatherImage = "NB18";
//     	}
//     	else if (weatherStr.equals("눈날림")) {
//     		weatherImage = "NB12";
//     	}
//     	else if (weatherStr.equals("안개")) {
//     		weatherImage = "NB15";
//     	}
//     	else if (weatherStr.equals("박무 (엷은 안개)")) {
//     		weatherImage = "NB17";
//     	}
//     	else if (weatherStr.equals("황사")) {
//     		weatherImage = "NB16";
//     	}
//
//     	return weatherImage;
// 	}

 // 단기예보 BaseDate 셋팅
 	private HashMap<String, Object> getBaseDateAndTimeForVilageFcst(String dateStr) {
 		HashMap<String, Object> param = new HashMap<String, Object>();

 		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
 		Date date;
 		try {
 			date = formatter.parse(dateStr);

 			SimpleDateFormat formatYmd = new SimpleDateFormat("yyyyMMdd");
 			SimpleDateFormat formatH = new SimpleDateFormat("HH"); // 시

 			String baseDate = formatYmd.format(date);	// 년월일
 			String baseTime = "";	// 시분

 			String baseMinute = "00";

 			int nowHour = Integer.valueOf(formatH.format(date)); // HH

 			if (nowHour >= 0 &&  nowHour <= 4) {
 				Calendar cal = Calendar.getInstance();
 				cal.setTime(date);
 				cal.add(Calendar.DATE, -1);
 				String beforeYmd = formatYmd.format(cal.getTime());

 				baseDate = beforeYmd;
 				baseTime = "23" + baseMinute;
 			}
 			else if (nowHour >= 5 && nowHour < 8) {
 				baseTime = "05" + baseMinute;
 			}
 			else if (nowHour >= 8 && nowHour < 11) {
 				baseTime = "08" + baseMinute;
 			}
 			else if (nowHour >= 11 && nowHour < 14) {
 				baseTime = "11" + baseMinute;
 			}
 			else if (nowHour >= 14 && nowHour < 17) {
 				baseTime = "14" + baseMinute;
 			}
 			else if (nowHour >= 17 && nowHour < 20) {
 				baseTime = "17" + baseMinute;
 			}
 			else if (nowHour >= 20 && nowHour < 23) {
 				baseTime = "20" + baseMinute;
 			}
 			else if (nowHour >= 23) {
 				baseTime = "23" + baseMinute;
 			}
 			// baseDate, baseTime 셋팅 종료

 			param.put("baseDate", baseDate);
 			param.put("baseTime", baseTime);

 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			//e.printStackTrace();
 		}

 		return param;

 	}

 	public HashMap<String, Object> convertGRID_GPS(int mode, double lat_X, double lng_Y ) {
	    double RE = 6371.00877; // 지구 반경(km)
	    double GRID = 5.0; // 격자 간격(km)
	    double SLAT1 = 30.0; // 투영 위도1(degree)
	    double SLAT2 = 60.0; // 투영 위도2(degree)
	    double OLON = 126.0; // 기준점 경도(degree)
	    double OLAT = 38.0; // 기준점 위도(degree)
	    double XO = 43; // 기준점 X좌표(GRID)
	    double YO = 136; // 기1준점 Y좌표(GRID)

	    //
	    // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
	    //
	    double DEGRAD = Math.PI / 180.0;
	    double RADDEG = 180.0 / Math.PI;

	    double re = RE / GRID;
	    double slat1 = SLAT1 * DEGRAD;
	    double slat2 = SLAT2 * DEGRAD;
	    double olon = OLON * DEGRAD;
	    double olat = OLAT * DEGRAD;

	    double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
	    double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	    sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
	    double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
	    ro = re * sf / Math.pow(ro, sn);

	    HashMap<String, Object> returnHashMap = new HashMap<String, Object>();

	    if (mode == TO_GRID) {
	        returnHashMap.put("lat", lat_X);
	        returnHashMap.put("lng", lng_Y);

	        double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
	        ra = re * sf / Math.pow(ra, sn);
	        double theta = lng_Y * DEGRAD - olon;
	        if (theta > Math.PI) theta -= 2.0 * Math.PI;
	        if (theta < -Math.PI) theta += 2.0 * Math.PI;
	        theta *= sn;

	        returnHashMap.put("nx", (int)Math.floor(ra * Math.sin(theta) + XO + 0.5));
	        returnHashMap.put("ny", (int)Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));
	    } else {
	        returnHashMap.put("nx", lat_X);
	        returnHashMap.put("ny", lng_Y);

	        double xn = lat_X - XO;
	        double yn = ro - lng_Y + YO;
	        double ra = Math.sqrt(xn * xn + yn * yn);
	        if (sn < 0.0) {
	            ra = -ra;
	        }
	        double alat = Math.pow((re * sf / ra), (1.0 / sn));
	        alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

	        double theta = 0.0;
	        if (Math.abs(xn) <= 0.0) {
	            theta = 0.0;
	        }
	        else {
	            if (Math.abs(yn) <= 0.0) {
	                theta = Math.PI * 0.5;
	                if (xn < 0.0) {
	                    theta = -theta;
	                }
	            }
	            else theta = Math.atan2(xn, yn);
	        }
	        double alon = theta / sn + olon;

	        returnHashMap.put("lat", alat * RADDEG);
	        returnHashMap.put("lng", alon * RADDEG);
	    }

	    return returnHashMap;
	}
 // nx, ny가 있는 지 확인 후 관측지 정보 ID, 관측지명, 변환된 nx, ny 정보 가져오기 (단기예보)
 	private HashMap<String, String> getConversionNxNyIntoByOldNxNyForVilageFcst(String mode, double lat, double lon, int old_nx, int old_ny) {
 		HashMap<String, String> param = new HashMap<String, String>();
 		param.put("old_nx", String.valueOf(old_nx));
 		param.put("old_ny", String.valueOf(old_ny));

 		HashMap<String, String> conversionNxNyHashMap = weatherMapper.selectConversionNxNyIntoByOldNxNy(param);

 		if( conversionNxNyHashMap == null) {
 			param.put("result", "null");
 			return param;
 		} else {

/*
 		if (mode.equals("wthrwrnmsg") || conversionNxNyHashMap == null) { // 2022.10.23 수정
 			// 기상청 내부 API를 이용하여 GPS 정보를 기상청 격자 nx, ny로 변환
 			conversionNxNyHashMap = getAndSaveNxNyForVilageFcst(mode, lat, lon, old_nx, old_ny);
 		}
*/

 			return conversionNxNyHashMap;
 		}
 	}
 	public Object jsonParse(URL url) {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		String result = null;
		try {
			result = bf.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		Object obj = null;
		try {
			JSONParser parser = new JSONParser(result);
			obj = parser;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}


		return obj;
	}

	public Object jsonParseForString(String str) {
		Object obj = null;
		try {
			JSONParser parser = new JSONParser( str );
			obj = parser;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}


		return obj;
	}

 // 기상청 내부 API를 이용하여 GPS 정보를 기상청 격자 nx, ny로 변환 후 insert (단기예보)
 	private HashMap<String, String> getAndSaveNxNyForVilageFcst(String mode, double lat, double lon, int nx, int ny) {

 		HashMap<String, String> nxnyHashMap = new HashMap<String, String>();

 		// 기상청 내부 API 호출 : https://www.weather.go.kr/w/rest/zone/find/dong.do?x=53&y=37&lat=36.841700000000000&lon=128.350910000000000&lang=kor
 		String baseURL = "https://www.weather.go.kr/w/rest/zone/find/dong.do"
 				+"?x="+nx+"&y="+ny+"&lat="+lat+"&lon="+lon;
 		try {
 			URL url = new URL(baseURL);

 			JSONArray jsonArray = (JSONArray)jsonParse(url);
 			//if (jsonArray.size() > 0) {
 				JSONObject jsonObject = (JSONObject) jsonArray.get(0);

 				nxnyHashMap.put("legalCode", (String) jsonObject.get("code"));
 				nxnyHashMap.put("legalCodeNm", (String) jsonObject.get("name"));
 				nxnyHashMap.put("shortName", (String) jsonObject.get("shortName"));

 				nxnyHashMap.put("old_nx", String.valueOf(nx));
 				nxnyHashMap.put("old_ny", String.valueOf(ny));
 				nxnyHashMap.put("url", baseURL);
 				nxnyHashMap.put("nx", (String) jsonObject.get("x"));
 				nxnyHashMap.put("ny", (String) jsonObject.get("y"));

 				// 컨버전 된 nx, ny로 cityId, dong 찾기
 				HashMap<String, String> cityInfoHashMap = weatherMapper.selectCityInfo(nxnyHashMap);

 				if (!mode.equals("wthrwrnmsg")) {
 					if (cityInfoHashMap != null) {
 						nxnyHashMap.put("city_id", String.valueOf(cityInfoHashMap.get("cityId")));
 						weatherMapper.insertConversionNxNy(nxnyHashMap);
 					}
 				}
 			//}
 		} catch (MalformedURLException e) {
 			// TODO Auto-generated catch block
 			//e.printStackTrace();
 		}

 		return nxnyHashMap;

 	}

 // 단기예보 날씨 정보 표출용으로 셋팅
 	private  WeatherVilageFcstVO convertWeatherInfoForVilageFcst (WeatherVilageFcstVO weatherVilageFcstInfo, String iconType) {
 		String ptyCode = String.valueOf(weatherVilageFcstInfo.getPty());
 		String skyCode = String.valueOf(weatherVilageFcstInfo.getSky());
     	String ptyCodeAndSkyCode = ptyCode+skyCode;
     	String weatherStr = "";
     	String weatherImage = "";
     	String weatherNormalImage = "";

 		if (iconType.equals("list")) {
 	    	// ptyCode -> 0 : 없음, 1 : 비, 2 : 비/눈, 3 : 눈, 4 : 소나기
 	    	// skyCode -> 1 : 맑음, 3 : 구름많음, 4 : 흐림
 			if (ptyCodeAndSkyCode.equals("00")) {
 	    		weatherStr = "맑음";
 	    		weatherImage = "sunny.svg";
 	    	}
 	    	if (ptyCodeAndSkyCode.equals("01")) {
 	    		weatherStr = "맑음";
 	    		weatherImage = "sunny.svg";
 	    	}
 	    	else if (ptyCodeAndSkyCode.equals("03")) {
 	    		weatherStr = "구름많음";
 	    		weatherImage = "cloudy.svg";
 	    	}
 	    	else if (ptyCodeAndSkyCode.equals("04")) {
 	    		weatherStr = "흐림";
 	    		weatherImage = "cloudy.svg";
 	    	}
 	    	else if (ptyCode.equals("0") && weatherVilageFcstInfo.getWsd() >= 14) {
 	    		weatherStr = "강풍";
 	    		weatherImage = "windy.svg";
 	    	}

 	    	if (ptyCode.equals("1")) {
 	    		weatherStr = "비";
 	    		weatherImage = "rainy.svg";
 	    	}
 	    	else if (ptyCode.equals("2")) {
 	    		weatherStr = "비/눈";
 	    		weatherImage = "rainy.svg";
 	    	}
 	    	else if (ptyCode.equals("3")) {
 	    		weatherStr = "눈";
 	    		weatherImage = "snowy.svg";
 	    	}
 	    	else if (ptyCode.equals("4")) {
 	    		weatherStr = "소나기";
 	    		weatherImage = "rainy.svg";
 	    	}

 			weatherVilageFcstInfo.setWeatherType(weatherStr);
 			weatherVilageFcstInfo.setWeatherImage(weatherImage);

 			// 강수량
 			String pcp = weatherVilageFcstInfo.getPcp();
 			if (pcp == null || pcp.equals("0") || pcp.equals("강수없음") || pcp.equals("-")) {
 				 weatherVilageFcstInfo.setPcp("- mm");
 			}

 			// 풍향
 			double vec = weatherVilageFcstInfo.getVec();

 			if (vec < 90 || vec == 360) {
 				weatherVilageFcstInfo.setVecStr("북");
 			}
 			else if (vec >= 90 && vec < 180) {
 				weatherVilageFcstInfo.setVecStr("동");
 			}
 			else if (vec >= 180 && vec < 270) {
 				weatherVilageFcstInfo.setVecStr("남");
 			}
 			else if (vec >= 270 && vec < 360) {
 				weatherVilageFcstInfo.setVecStr("서");
 			}
 		} else if (iconType.equals("detail")) {
 			// 시간에 따른 낮/밤 구분 처리
 			String fcstTime = weatherVilageFcstInfo.getFcstTime();
 			int fcstHour = Integer.valueOf(fcstTime.substring(0, 2));

 			String dayAndNight = "";
 			if ((fcstHour >= 18 && fcstHour <= 23) || (fcstHour >= 0 && fcstHour < 7))  dayAndNight = "_N";
 			// ptyCode -> 0 : 없음, 1 : 비, 2 : 비/눈, 3 : 눈, 4 : 소나기
 	    	// skyCode -> 1 : 맑음, 3 : 구름많음, 4 : 흐림
 	    	if (ptyCodeAndSkyCode.equals("01")) {
 	    		weatherStr = "맑음";
 	    		//weatherImage = "01_sunny.svg"; // 02_sunny_night
 	    		weatherImage = "DB01"+dayAndNight; // DB01_N
 	    		weatherNormalImage = "sunny.svg";
 	    	}
 	    	// 03_partlysunny : 구름 조금 (낮) // 04_partlysunny_night (밤)
 	    	else if (ptyCodeAndSkyCode.equals("03")) {
 	    		weatherStr = "구름많음";
 	    		//weatherImage = "05_partlycloudy.svg"; // 06_partlycloudy_night
 	    		weatherImage = "DB03"+dayAndNight; // DB03_N
 	    		weatherNormalImage = "cloudy.svg";
 	    	}
 	    	else if (ptyCodeAndSkyCode.equals("04")) {
 	    		weatherStr = "흐림";
 	    		//weatherImage = "07_cloudy.svg"; // 07_cloudy.svg
 	    		weatherImage = "DB04"+dayAndNight; // DB04_N
 	    		weatherNormalImage = "cloudy.svg";
 	    	}
 	    	/*
 	    	else if (ptyCode.equals("0") && weatherVilageFcstInfo.getWsd() >= 14) {
 	    		weatherStr = "강풍";
 	    		weatherImage = "windy.svg";
 	    	}
 	    	*/

 	    	if (ptyCode.equals("1")) {
 	    		weatherStr = "비";
 	    		//weatherImage = "09_rainy.svg";
 	    		weatherImage = "DB05";
 	    		weatherNormalImage = "rainy.svg";
 	    	}
 	    	else if (ptyCode.equals("2")) {
 	    		weatherStr = "비/눈";
 	    		//weatherImage = "13_rainy_or_snowy.svg";
 	    		weatherImage = "DB06";
 	    		weatherNormalImage = "rainy.svg";
 	    	}
 	    	else if (ptyCode.equals("3")) {
 	    		weatherStr = "눈";
 	    		//weatherImage = "11_snowy.svg";
 	    		weatherImage = "DB08";
 	    		weatherNormalImage = "snowy.svg";
 	    	}
 	    	else if (ptyCode.equals("4")) {
 	    		weatherStr = "소나기";
 	    		//weatherImage = "08_shower.svg";
 	    		weatherImage = "DB09";
 	    		weatherNormalImage = "rainy.svg";
 	    	}

 			weatherVilageFcstInfo.setWeatherType(weatherStr);
 			weatherVilageFcstInfo.setWeatherImage(weatherImage);
 			weatherVilageFcstInfo.setWeatherNormalImage(weatherNormalImage);

 			// 강수량
 			String pcp = weatherVilageFcstInfo.getPcp();
 			if (pcp == null || pcp.equals("0") || pcp.equals("강수없음") || pcp.equals("-")) {
 				 weatherVilageFcstInfo.setPcp("-");
 			}

 			// 풍향 : E, N, NE, NW, S, SE, SW, W
 			double vec = weatherVilageFcstInfo.getVec();

 			if (vec <= 0 && vec < 45 || vec == 360) {
 				weatherVilageFcstInfo.setVecEngStr("N");
 				weatherVilageFcstInfo.setVecStr("북");
 			}
 			else if (vec >= 45 && vec < 90) {
 				weatherVilageFcstInfo.setVecEngStr("NE");
 				weatherVilageFcstInfo.setVecStr("북동");
 			}
 			else if (vec >= 90 && vec < 135) {
 				weatherVilageFcstInfo.setVecEngStr("E");
 				weatherVilageFcstInfo.setVecStr("동");
 			}
 			else if (vec >= 135 && vec < 180) {
 				weatherVilageFcstInfo.setVecEngStr("SE");
 				weatherVilageFcstInfo.setVecStr("남동");
 			}
 			else if (vec >= 180 && vec < 225) {
 				weatherVilageFcstInfo.setVecEngStr("S");
 				weatherVilageFcstInfo.setVecStr("남");
 			}
 			else if (vec >= 225 && vec < 270) {
 				weatherVilageFcstInfo.setVecEngStr("SW");
 				weatherVilageFcstInfo.setVecStr("남서");
 			}
 			else if (vec >= 270 && vec < 315) {
 				weatherVilageFcstInfo.setVecEngStr("W");
 				weatherVilageFcstInfo.setVecStr("서");
 			}
 			else if (vec >= 315 && vec < 360) {
 				weatherVilageFcstInfo.setVecEngStr("NW");
 				weatherVilageFcstInfo.setVecStr("북서");
 			}
 			/*
 			int vecType = (int)((vec + 22.5 * 0.5) / 22.5);
 			switch (vecType) {
 				case 0:
 					weatherVilageFcstInfo.setVecStr("N");
 					break;
 				case 1:
 					weatherVilageFcstInfo.setVecStr("NNE");
 					break;
 				case 2:
 					weatherVilageFcstInfo.setVecStr("NE");
 					break;
 				case 3:
 					weatherVilageFcstInfo.setVecStr("ENE");
 					break;
 				case 4:
 					weatherVilageFcstInfo.setVecStr("E");
 					break;
 				case 5:
 					weatherVilageFcstInfo.setVecStr("ESE");
 					break;
 				case 6:
 					weatherVilageFcstInfo.setVecStr("SE");
 					break;
 				case 7:
 					weatherVilageFcstInfo.setVecStr("SSE");
 					break;
 				case 8:
 					weatherVilageFcstInfo.setVecStr("S");
 					break;
 				case 9:
 					weatherVilageFcstInfo.setVecStr("SSW");
 					break;
 				case 10:
 					weatherVilageFcstInfo.setVecStr("SW");
 					break;
 				case 11:
 					weatherVilageFcstInfo.setVecStr("WSW");
 					break;
 				case 12:
 					weatherVilageFcstInfo.setVecStr("W");
 					break;
 				case 13:
 					weatherVilageFcstInfo.setVecStr("WNW");
 					break;
 				case 14:
 					weatherVilageFcstInfo.setVecStr("NW");
 					break;
 				case 15:
 					weatherVilageFcstInfo.setVecStr("NNW");
 					break;
 				case 16:
 					weatherVilageFcstInfo.setVecStr("N");
 					break;
 				default:
 					break;
 			}
 			*/
 		}

 		// 체감온도
 		// 참고 : https://www.weatheri.co.kr/useful/useful03.php
 		double windChillTmp = 13.12 + (0.6215 * weatherVilageFcstInfo.getTmp()) - (11.37 * Math.pow(weatherVilageFcstInfo.getWsd()*3.6, 0.16)) + (0.3965 * Math.pow(weatherVilageFcstInfo.getWsd()*3.6, 0.16) * weatherVilageFcstInfo.getTmp());
 		weatherVilageFcstInfo.setWindChillTmp(Math.round(windChillTmp * 10) / 10.0);

 		return weatherVilageFcstInfo;
 	}

	// nx, ny로 현재 시각 단기예보 정보 가져오기
    public WeatherVilageFcstVO selectCurrentWeatherVilageFcstInfoIntoByNxNy(HashMap<String, Object> param) {
    	return weatherMapper.selectCurrentWeatherVilageFcstInfoIntoByNxNy(param);
    }
	// nx, ny로 전체 단기예보 정보 가져오기
    public ArrayList<WeatherVilageFcstVO> selectWeatherVilageFcstInfoListIntoByNxNy(HashMap<String, Object> param){
    	return weatherMapper.selectWeatherVilageFcstInfoListIntoByNxNy(param);
    }
	// 등록된 지점의 현재 시각 단기예보 정보 가져오기
    public List<WeatherVilageFcstVO> selectCurrentWeatherVilageFcstInfoAll(HashMap<String, Object> param){
    	return weatherMapper.selectWeatherVilageFcstInfoListIntoByNxNy(param);
    }
	// 등록된 지점의 현재 시각 초단기실황 정보 가져오기
    public List<WeatherUltraSrtNcstVO> selectCurrentWeatherUltraSrtNcstInfoAll(HashMap<String, Object> param){
    	return weatherMapper.selectCurrentWeatherUltraSrtNcstInfoAll(param);
    }
	// MidLandRegId로 현재 시각 중기육상예보 정보 가져오기
    public WeatherMidLandFcstVO selectCurrentWeatherMidLandFcstIntoByMidLandRegId(HashMap<String, Object> param){
    	return weatherMapper.selectCurrentWeatherMidLandFcstIntoByMidLandRegId(param);
    }
	// midTaRegId로 현재 시각 중기기온조회 정보 가져오기
    public WeatherMidTaVO selectCurrentWeatherMidTaIntoByMidTaRegId(HashMap<String, Object> param){
    	return weatherMapper.selectCurrentWeatherMidTaIntoByMidTaRegId(param);
    }
	// legalCode로 현재 시각 중기기온조회 정보 가져오기
    public WeatherMidTaVO selectCurrentWeatherMidTaIntoByLegalCode(HashMap<String, Object> param){
    	return weatherMapper.selectCurrentWeatherMidTaIntoByLegalCode(param);
    }
	// cityId로 가장 최근 단기예보 1개 정보 가져오기
    public WeatherVilageFcstVO selectCurrentWeatherVilageFcstInfoIntoByCityId(HashMap<String, Object> param){
    	return weatherMapper.selectCurrentWeatherVilageFcstInfoIntoByCityId(param);
    }
	// cityId로 전체 단기예보 정보 가져오기
    public ArrayList<WeatherVilageFcstVO> selectWeatherVilageFcstInfoListIntoByCityId(HashMap<String, Object> param){
    	return weatherMapper.selectWeatherVilageFcstInfoListIntoByCityId(param);
    }
	// stnId로 현재 시각 특보목록 정보 가져오기
    public WeatherWthrWrnListVO selectCurrentWeatherWthrWrnListIntoByStnId(WeatherWthrWrnListVO param){
    	return weatherMapper.selectCurrentWeatherWthrWrnListIntoByStnId(param);
    }
	// stnId로 현재 시각 예비특보목록 정보 가져오기
    public WeatherWthrPwnListVO selectCurrentWeatherWthrPwnListIntoByStnId(WeatherWthrPwnListVO param){
    	return weatherMapper.selectCurrentWeatherWthrPwnListIntoByStnId(param);
    }
	// stnId로 현재 시각 특보통보문 정보 가져오기
    public WeatherWthrWrnMsgVO selectCurrentWeatherWthrWrnMsgIntoByStnId(HashMap<String, String> param){
    	return weatherMapper.selectCurrentWeatherWthrWrnMsgIntoByStnId(param);
    }
}
