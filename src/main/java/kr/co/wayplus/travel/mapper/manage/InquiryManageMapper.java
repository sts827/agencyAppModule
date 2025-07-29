package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wayplus.travel.model.InquiryAttachFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.InquiryCategory;
import kr.co.wayplus.travel.model.InquiryContent;

@Mapper
@Repository
public interface InquiryManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */
	ArrayList<HashMap<String, Object>> selectListCountInquiryContentByCalendar(HashMap<String, Object> param);
	ArrayList<HashMap<String, Object>> selectListInquiryContentByCheckList(HashMap<String, Object> param);
//	<!--################################### InquiryContent ###################################-->
	int selectCountInquiryContent(HashMap<String, Object> paramMap);
	int selectCountInquiryContent(InquiryContent ic);
	ArrayList<InquiryContent> selectListInquiryContent(HashMap<String, Object> paramMap);
	ArrayList<InquiryContent> selectListInquiryContent(InquiryContent ic);
	InquiryContent selectOneInquiryContent(HashMap<String, Object> paramMap);
	ArrayList<HashMap<String, Object>> selectListInquiryCountStatusType(HashMap<String, Object> paramMap);
	void insertInquiryContent(InquiryContent ic)throws SQLException;
	void updateInquiryContent(InquiryContent ic) throws SQLException;
	void restoreInquiryContent(InquiryContent ic) throws SQLException;
	void deleteInquiryContent(InquiryContent ic) throws SQLException;
//	<!--################################### InquiryCategory ###################################-->
	int selectCountInquiryCategory(HashMap<String, Object> paramMap);
	ArrayList<InquiryCategory> selectListInquiryCategory(HashMap<String, Object> paramMap);
	InquiryCategory selectOneInquiryCategory(HashMap<String, Object> paramMap);
	void insertInquiryCategory(InquiryCategory ic)throws SQLException;
	void updateInquiryCategory(InquiryCategory ic) throws SQLException;
	void restoreInquiryCategory(InquiryCategory ic) throws SQLException;
	void deleteInquiryCategory(InquiryCategory ic) throws SQLException;
	List<InquiryAttachFile> selectOneInquiryContentFiles(HashMap<String, Object> paramMap) throws SQLException;
}
