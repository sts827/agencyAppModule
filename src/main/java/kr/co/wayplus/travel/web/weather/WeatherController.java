package kr.co.wayplus.travel.web.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import kr.co.wayplus.travel.model.WeatherVilageFcstVO;
import kr.co.wayplus.travel.service.weather.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/one")
	@ResponseBody
    public HashMap<String, Object> touristSpot(
  		  @RequestParam(value="authkey", defaultValue="wayplus", required = false) String authkey,
  		  @RequestParam(value="lat", defaultValue="33.499602") double lat,
  		  @RequestParam(value="lon", defaultValue="126.531253") double lon,
  		  @RequestParam(value="size", defaultValue="6") int size,
  		  @RequestParam(value="type", defaultValue="weather") String type){

        HashMap<String, Object> result = new HashMap<>();

        try{
      	  StringBuilder urlBuilder = new StringBuilder("http://region.wayplus.kr/api/navigator/weather"); /*URL*/
      	  urlBuilder.append("?" + URLEncoder.encode("authkey","UTF-8") + "="+authkey); /*Service Key*/
      	  urlBuilder.append("&" + URLEncoder.encode("lat","UTF-8") + "="+lat); /*경도*/
      	  urlBuilder.append("&" + URLEncoder.encode("lon","UTF-8") + "="+lon); /*위도*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            Gson gson = new Gson();
            Map map = gson.fromJson(sb.toString(), Map.class);


//            HashMap<String, Object> param = new HashMap<>();
//            param.put("authkey", authkey);
//            param.put("lat", latitude);
//            param.put("lon", longitude);
//            param.put("size", size);
//            param.put("type", type);

//            ArrayList<TouristSpot> touristSpots = waytripApiService.getMainTouristSpotList(param);
//            result.put("list", touristSpots);
            result.put("data", map);
            result.put("message", "정상 처리");
            result.put("result", "success");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            result.put("message", e.getMessage());
            result.put("result", "error");
        }
        return result;
    }


    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> info(double lat, double lon){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
        	// 캠핑장 중기육상예보 가져오기, 33.47305556,126.5470083
    	    //String midLandRegId = "11C10000";
    	    //WeatherMidLandFcstVO weatherMidLandFcstInfo = weatherService.getCurrentWeatherMidLandFcstIntoByMidLandRegId(midLandRegId);

    	    // 캠핑장 단기예보 (전체) 정보 가져오기
        	//126.5470083	33.47305556
        	// 지자체 날씨정보 (헤더 롤링용)
//    		List<WeatherVilageFcstVO> weatherVilageFcstInfoListForHeader = null;
    	    List<WeatherVilageFcstVO> weatherVilageFcstInfoList = weatherService.getWeatherVilageFcstInfoListIntoByLatLon(lat, lon);
    	    if( weatherVilageFcstInfoList != null ) {
	    	    ArrayList<String> tmpArrayList = new ArrayList<String>(); /*단기예보 라인 처리용*/
	    	    ArrayList<String> listDate = new ArrayList<String>(); /*단기예보 라인 처리용*/

	    	    HashMap<String, Object> mapDateInfo = new HashMap<String, Object>();
	    	    HashMap<String, Object> resultWeatherVilageFcstInfoList = new HashMap<String, Object>();

	    	    for (WeatherVilageFcstVO _data: weatherVilageFcstInfoList) {
	    	        String fcstDate = _data.getFcstDate();
	    	        String fcstTime = _data.getFcstTime();
	    	        String mapKey = fcstDate;
	    	        ArrayList<WeatherVilageFcstVO> weatherList =  null; //MapData 처리용

	    	        if( fcstTime != null ) {
	    	        	if( fcstTime.equals("0600") ) {
	//    	        		logger.info( fcstTime + " " + _data.getTmn() );
	    	        		mapDateInfo.put(fcstDate+"_tmn", _data.getTmn());
	    	        	}
	    	        	if( fcstTime.equals("1500") ) {
	//    	        		logger.info( fcstTime + " " + _data.getTmx() );
	    	        		mapDateInfo.put(fcstDate+"_tmx", _data.getTmx());
	    	        	}
	    	        }

	      	        tmpArrayList.add(String.valueOf(_data.getTmp())); // 그래프용 기온 데이터

	    	        if (!resultWeatherVilageFcstInfoList.containsKey( mapKey )) {
	    	        	weatherList = new ArrayList<WeatherVilageFcstVO>();
	    	        	resultWeatherVilageFcstInfoList.put( mapKey , weatherList);
	    	        	listDate.add(fcstDate); // 날짜 목록 확인용.

	    	        	mapDateInfo.put(fcstDate+"_dt", _data.getDatef1() );
	    	        	mapDateInfo.put(fcstDate+"_dtf", _data.getDatef2() );
	    	        	mapDateInfo.put(fcstDate+"_dow", _data.getDayOfWeek() );
	    	        	mapDateInfo.put(fcstDate+"_st", _data.getTimef1());
	    	        	mapDateInfo.put(fcstDate+"_sdt", _data.getTimef2());
	/*
	    	        	mapDateInfo.put(fcstDate+"_dtf", fcstDate);
	    	        	mapDateInfo.put(fcstDate+"_st", ft1.parse(fcstTime));
	    	        	mapDateInfo.put(fcstDate+"_sdt", ft2.parse(fcstTime));
	*/
	    	        } else {
	    	        	weatherList = (ArrayList<WeatherVilageFcstVO>) resultWeatherVilageFcstInfoList.get( mapKey );
	    	        }

	    	        weatherList.add(_data);
	    	    }

	    	    String tmpVilageFcstStr = String.join(",", tmpArrayList);

	    	    resultWeatherVilageFcstInfoList.put("list_header", listDate);
	    	    resultWeatherVilageFcstInfoList.put("date_info", mapDateInfo);
	    	    resultWeatherVilageFcstInfoList.put("temp_data", tmpVilageFcstStr);

	    	    resultMap.put("data", resultWeatherVilageFcstInfoList); // 캠핑장 단기예보 (전체) 정보
	    	    resultMap.put("result", "success");
	    	    resultMap.put("message", "처리 완료");
    	    } else {
	    	    resultMap.put("result", "null");
	    	    resultMap.put("message", "날씨 정보가 없습니다.");

    	    }
        }catch (Exception e){
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

}
