package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.BoardAttachFile;
import kr.co.wayplus.travel.model.BoardComment;
import kr.co.wayplus.travel.model.BoardContents;
import kr.co.wayplus.travel.model.BoardSetting;
import kr.co.wayplus.travel.model.PlaceSpot;
import kr.co.wayplus.travel.model.PlaceSpotImage;

@Mapper
@Repository
public interface PlaceManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */

	//	<!--################################### PlaceSpot ###################################-->
	int selectCountPlaceSpot(HashMap<String, Object> paramMap);
	ArrayList<PlaceSpot> selectListPlaceSpot(HashMap<String, Object> paramMap);
	PlaceSpot selectOnePlaceSpot(HashMap<String, Object> paramMap);
	void insertPlaceSpot(PlaceSpot ts) throws SQLException;
	void updatePlaceSpot(PlaceSpot ts) throws SQLException;
	void deletePlaceSpot(PlaceSpot ts) throws SQLException;
	//	<!--################################### PlaceSpotImage ###################################-->
	int selectCountPlaceSpotImage(HashMap<String, Object> paramMap);
	ArrayList<PlaceSpot> selectListPlaceSpotImage(HashMap<String, Object> paramMap);
	PlaceSpotImage selectOnePlaceSpotImage(HashMap<String, Object> paramMap);
	void insertPlaceSpotImage(PlaceSpotImage psi) throws SQLException;
	void updatePlaceSpotImage(PlaceSpotImage psi) throws SQLException;
	void deletePlaceSpotImage(PlaceSpotImage psi) throws SQLException;
}
