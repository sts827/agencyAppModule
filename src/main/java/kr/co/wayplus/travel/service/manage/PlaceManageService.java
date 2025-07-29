package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.PlaceManageMapper;
import kr.co.wayplus.travel.model.BoardAttachFile;
import kr.co.wayplus.travel.model.BoardComment;
import kr.co.wayplus.travel.model.BoardContents;
import kr.co.wayplus.travel.model.BoardSetting;
import kr.co.wayplus.travel.model.PlaceSpot;
import kr.co.wayplus.travel.model.PlaceSpotImage;

@Service
public class PlaceManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PlaceManageMapper mapper;

    public PlaceManageService(PlaceManageMapper mapper) {
        this.mapper = mapper;
    }

//	<!--################################### PlaceSpot ###################################-->
    public int selectCountPlaceSpot(HashMap<String, Object> paramMap) {
    	return mapper.selectCountPlaceSpot(paramMap);
    }
    public ArrayList<PlaceSpot> selectListPlaceSpot(HashMap<String, Object> paramMap) {
    	return mapper.selectListPlaceSpot(paramMap);
    }
	public PlaceSpot selectOnePlaceSpot(HashMap<String, Object> paramMap) {
		return mapper.selectOnePlaceSpot(paramMap);
	}
	public void insertPlaceSpot(PlaceSpot ts) throws SQLException {
		mapper.insertPlaceSpot(ts);
	}
	public void updatePlaceSpot(PlaceSpot ts) throws SQLException {
		mapper.updatePlaceSpot(ts);
	}
	public void deletePlaceSpot(PlaceSpot ts) throws SQLException {
		mapper.deletePlaceSpot(ts);
	}
//	<!--################################### PlaceSpotImage ###################################-->
	public int selectCountPlaceSpotImage(HashMap<String, Object> paramMap) {
    	return mapper.selectCountPlaceSpotImage(paramMap);
    }
    public ArrayList<PlaceSpot> selectListPlaceSpotImage(HashMap<String, Object> paramMap) {
    	return mapper.selectListPlaceSpotImage(paramMap);
    }
	public PlaceSpotImage selectOnePlaceSpotImage(HashMap<String, Object> paramMap) {
		return mapper.selectOnePlaceSpotImage(paramMap);
	}

	public void insertPlaceSpotImage(PlaceSpotImage psi) throws SQLException {
		mapper.insertPlaceSpotImage(psi);
	}

	public void updatePlaceSpotImage(PlaceSpotImage psi) throws SQLException {
		mapper.updatePlaceSpotImage(psi);
	}
	public void deletePlaceSpotImage(PlaceSpotImage psi) throws SQLException {
		mapper.deletePlaceSpotImage(psi);
	}

}
