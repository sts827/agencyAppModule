package kr.co.wayplus.travel.mapper.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.WeatherMidLandFcstVO;
import kr.co.wayplus.travel.model.WeatherMidTaVO;
import kr.co.wayplus.travel.model.WeatherUltraSrtNcstVO;
import kr.co.wayplus.travel.model.WeatherVilageFcstVO;
import kr.co.wayplus.travel.model.WeatherWthrPwnListVO;
import kr.co.wayplus.travel.model.WeatherWthrWrnListVO;
import kr.co.wayplus.travel.model.WeatherWthrWrnMsgVO;

@Mapper
@Repository
public interface WeatherMapper {
	// 관측지 정보 가져오기 (nx, ny)
	HashMap<String, String> selectCityInfo(HashMap<String, String> param);

	Boolean insertConversionNxNy(HashMap<String, String> conversionInfoHashMap);

	// nx, ny가 있는 지 확인 후 관측지 정보 ID, 관측지명, 변환된 nx, ny 정보 가져오기
	HashMap<String, String> selectConversionNxNyIntoByOldNxNy(HashMap<String, String> param);
	// nx, ny로 현재 시각 단기예보 정보 가져오기
	WeatherVilageFcstVO selectCurrentWeatherVilageFcstInfoIntoByNxNy(HashMap<String, Object> param);
	// nx, ny로 전체 단기예보 정보 가져오기
	ArrayList<WeatherVilageFcstVO> selectWeatherVilageFcstInfoListIntoByNxNy(HashMap<String, Object> param1);
	// 등록된 지점의 현재 시각 단기예보 정보 가져오기
	List<WeatherVilageFcstVO> selectCurrentWeatherVilageFcstInfoAll(HashMap<String, Object> param);
	// 등록된 지점의 현재 시각 초단기실황 정보 가져오기
	List<WeatherUltraSrtNcstVO> selectCurrentWeatherUltraSrtNcstInfoAll(HashMap<String, Object> param);
	// MidLandRegId로 현재 시각 중기육상예보 정보 가져오기
	WeatherMidLandFcstVO selectCurrentWeatherMidLandFcstIntoByMidLandRegId(HashMap<String, Object> param);
	// midTaRegId로 현재 시각 중기기온조회 정보 가져오기
	WeatherMidTaVO selectCurrentWeatherMidTaIntoByMidTaRegId(HashMap<String, Object> param);
	// legalCode로 현재 시각 중기기온조회 정보 가져오기
	WeatherMidTaVO selectCurrentWeatherMidTaIntoByLegalCode(HashMap<String, Object> param);
	// cityId로 가장 최근 단기예보 1개 정보 가져오기
	WeatherVilageFcstVO selectCurrentWeatherVilageFcstInfoIntoByCityId(HashMap<String, Object> param1);
	// cityId로 전체 단기예보 정보 가져오기
	ArrayList<WeatherVilageFcstVO> selectWeatherVilageFcstInfoListIntoByCityId(HashMap<String, Object> param1);
	// stnId로 현재 시각 특보목록 정보 가져오기
	WeatherWthrWrnListVO selectCurrentWeatherWthrWrnListIntoByStnId(WeatherWthrWrnListVO param);
	// stnId로 현재 시각 예비특보목록 정보 가져오기
	WeatherWthrPwnListVO selectCurrentWeatherWthrPwnListIntoByStnId(WeatherWthrPwnListVO param);
	// stnId로 현재 시각 특보통보문 정보 가져오기
	WeatherWthrWrnMsgVO selectCurrentWeatherWthrWrnMsgIntoByStnId(HashMap<String, String> param);
}