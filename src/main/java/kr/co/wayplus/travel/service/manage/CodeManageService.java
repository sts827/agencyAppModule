package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.CodeManageMapper;
import kr.co.wayplus.travel.model.CodeItem;

@Service
public class CodeManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CodeManageMapper mapper;

    public CodeManageService(CodeManageMapper mapper) {
        this.mapper = mapper;
    }

//	<!--################################### CodeItem ###################################-->
	public int selectCountCodeItem(HashMap<String, Object> paramMap) {
		return mapper.selectCountCodeItem(paramMap);
	}
	public int selectCountCodeItem(CodeItem ci) {
		return mapper.selectCountCodeItem(ci);
	}
	public ArrayList<CodeItem> selectListCodeItem(HashMap<String, Object> paramMap) {
		return mapper.selectListCodeItem(paramMap);
	}
//	public ArrayList<CodeItem> selectListCodeItem(CodeItem ci) {
//		return mapperWayplusManage.selectListCodeItem(bc);
//	}
	public CodeItem selectOneCodeItem(HashMap<String, Object> paramMap) {
		return mapper.selectOneCodeItem(paramMap);
	}
	public void saveCodeItem(CodeItem ci) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", ci.getCode());

		if( this.selectCountCodeItem(paramMap) == 0) {
			mapper.insertCodeItem(ci);
		} else {
			mapper.updateCodeItem(ci);
		}
	}
	public void insertCodeItem(CodeItem ci) throws SQLException {
		mapper.insertCodeItem(ci);
	}
	public void updateCodeItem(CodeItem ci) throws SQLException {
		mapper.updateCodeItem(ci);
	}
	public void restoreCodeItem(CodeItem ci) throws SQLException {
		mapper.restoreCodeItem(ci);
	}
	public void deleteCodeItem(CodeItem ci) throws SQLException {
		mapper.deleteCodeItem(ci);
	}
}
