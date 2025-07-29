package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.InquiryManageMapper;
import kr.co.wayplus.travel.model.InquiryCategory;
import kr.co.wayplus.travel.model.InquiryContent;

@Service
public class InquiryManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final InquiryManageMapper mapper;

    public InquiryManageService(InquiryManageMapper mapper) {
        this.mapper = mapper;
    }

    public ArrayList<HashMap<String, Object>> selectListCountInquiryContentByCalendar(HashMap<String, Object> param) {
    	return mapper.selectListCountInquiryContentByCalendar(param);
    }
    public ArrayList<HashMap<String, Object>> selectListInquiryContentByCheckList(HashMap<String, Object> param) {
		return mapper.selectListInquiryContentByCheckList(param);
    }
//	<!--################################### InquiryContent ###################################-->
	public int selectCountInquiryContent(HashMap<String, Object> paramMap) {
		return mapper.selectCountInquiryContent(paramMap);
	}
	public int selectCountInquiryContent(InquiryContent ic) {
		return mapper.selectCountInquiryContent(ic);
	}
	public ArrayList<InquiryContent> selectListInquiryContent(HashMap<String, Object> paramMap) {
		return mapper.selectListInquiryContent(paramMap);
	}
//	public ArrayList<InquiryContent> selectListInquiryContent(InquiryContent ic) {
//		return mapperWayplusManage.selectListInquiryContent(bc);
//	}
	public InquiryContent selectOneInquiryContent(HashMap<String, Object> paramMap) {
		return mapper.selectOneInquiryContent(paramMap);
	}
	public ArrayList<HashMap<String, Object>> selectListInquiryCountStatusType(HashMap<String, Object> paramMap) {
		return mapper.selectListInquiryCountStatusType(paramMap);
	}
	public void saveInquiryContent(InquiryContent ic) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", ic.getId());

		if( this.selectCountInquiryContent(paramMap) == 0) {
			mapper.insertInquiryContent(ic);
		} else {
			mapper.updateInquiryContent(ic);
		}
	}
	public void insertInquiryContent(InquiryContent ic) throws SQLException {
		mapper.insertInquiryContent(ic);
	}
	public void updateInquiryContent(InquiryContent ic) throws SQLException {
		mapper.updateInquiryContent(ic);
	}
	public void restoreInquiryContent(InquiryContent ic) throws SQLException {
		mapper.restoreInquiryContent(ic);
	}
	public void deleteInquiryContent(InquiryContent ic) throws SQLException {
		mapper.deleteInquiryContent(ic);
	}
//	<!--################################### InquiryCategory ###################################-->
	public int selectCountInquiryCategory(HashMap<String, Object> paramMap) {
		return mapper.selectCountInquiryCategory(paramMap);
	}
	public InquiryCategory selectOneInquiryCategory(HashMap<String, Object> paramMap) {
		return mapper.selectOneInquiryCategory(paramMap);
	}
	public ArrayList<InquiryCategory> selectListInquiryCategory(HashMap<String, Object> paramMap) {
		return mapper.selectListInquiryCategory(paramMap);
	}
	public void insertInquiryCategory(InquiryCategory ic) throws SQLException {
		mapper.insertInquiryCategory(ic);
	}
	public void updateInquiryCategory(InquiryCategory ic) throws SQLException {
		mapper.updateInquiryCategory(ic);
	}
	public void restoreInquiryCategory(InquiryCategory ic) throws SQLException {
		mapper.restoreInquiryCategory(ic);
	}
	public void deleteInquiryCategory(InquiryCategory ic) throws SQLException {
		mapper.deleteInquiryCategory(ic);
	}
}
