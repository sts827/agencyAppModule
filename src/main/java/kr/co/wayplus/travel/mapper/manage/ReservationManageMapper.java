package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.Reservation;

@Mapper
@Repository
public interface ReservationManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */
//	<!--################################### Reservation ###################################-->
	int selectCountReservation(HashMap<String, Object> paramMap);
	int selectCountReservation(Reservation ic);
	ArrayList<Reservation> selectListReservation(HashMap<String, Object> paramMap);
	ArrayList<Reservation> selectListReservation(Reservation ic);
	Reservation selectOneReservation(HashMap<String, Object> paramMap);
	ArrayList<HashMap<String, Object>> selectListInquiryCountStatusType(HashMap<String, Object> paramMap);
	void insertReservation(Reservation ic)throws SQLException;
	void updateReservation(Reservation ic) throws SQLException;
	void restoreReservation(Reservation ic) throws SQLException;
	void deleteReservation(Reservation ic) throws SQLException;

	ArrayList<HashMap<String, Object>> selectListCountReservationContentByCalendar(HashMap<String, Object> paramMap);
	ArrayList<HashMap<String, Object>> selectListReservationContentByCheckList(HashMap<String, Object> paramMap);
	HashMap<String, Object> selectListReservationCountStatusType(HashMap<String, Object> paramMap);
}
