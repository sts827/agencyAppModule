package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wayplus.travel.mapper.manage.InquiryManageMapper;
import kr.co.wayplus.travel.mapper.manage.ReservationManageMapper;
import kr.co.wayplus.travel.model.InquiryCategory;
import kr.co.wayplus.travel.model.Reservation;

@Service
public class ReservationManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReservationManageMapper mapper;

    public ReservationManageService(ReservationManageMapper mapper) {
        this.mapper = mapper;
    }

//	<!--################################### Reservation ###################################-->
	public int selectCountReservation(HashMap<String, Object> paramMap) {
		return mapper.selectCountReservation(paramMap);
	}
	public int selectCountReservation(Reservation ic) {
		return mapper.selectCountReservation(ic);
	}
	public ArrayList<Reservation> selectListReservation(HashMap<String, Object> paramMap) {
		return mapper.selectListReservation(paramMap);
	}

	//	public ArrayList<Reservation> selectListReservation(Reservation ic) {
//		return mapperWayplusManage.selectListReservation(bc);
//	}

	public Reservation selectOneReservation(HashMap<String, Object> paramMap) {
		return mapper.selectOneReservation(paramMap);
	}

	public ArrayList<HashMap<String, Object>> selectListInquiryCountStatusType(HashMap<String, Object> paramMap) {
		return mapper.selectListInquiryCountStatusType(paramMap);
	}

	public void saveReservation(Reservation ic) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", ic.getId());

		if( this.selectCountReservation(paramMap) == 0) {
			mapper.insertReservation(ic);
		} else {
			mapper.updateReservation(ic);
		}
	}

	public void insertReservation(Reservation ic) throws SQLException {
		mapper.insertReservation(ic);
	}
	@Transactional
	public void updateReservation(Reservation ic) throws SQLException {
		mapper.updateReservation(ic);
	}
	public void restoreReservation(Reservation ic) throws SQLException {
		mapper.restoreReservation(ic);
	}
	public void deleteReservation(Reservation ic) throws SQLException {
		mapper.deleteReservation(ic);
	}

	public ArrayList<HashMap<String, Object>> selectListCountReservationContentByCalendar(HashMap<String, Object> paramMap) {
		return mapper.selectListCountReservationContentByCalendar(paramMap);
	}

	public ArrayList<HashMap<String, Object>> selectListReservationContentByCheckList(HashMap<String, Object> paramMap) {
		return mapper.selectListReservationContentByCheckList(paramMap);
	}

	public HashMap<String, Object> selectListReservationCountStatusType(HashMap<String, Object> paramMap) {
		return mapper.selectListReservationCountStatusType(paramMap);
	}
}
