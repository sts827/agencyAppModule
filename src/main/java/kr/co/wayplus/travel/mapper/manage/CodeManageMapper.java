package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.CodeItem;

@Mapper
@Repository
public interface CodeManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */
//	<!--################################### CodeItem ###################################-->
	int selectCountCodeItem(HashMap<String, Object> paramMap);
	int selectCountCodeItem(CodeItem ci);
	ArrayList<CodeItem> selectListCodeItem(HashMap<String, Object> paramMap);
	ArrayList<CodeItem> selectListCodeItem(CodeItem ci);
	CodeItem selectOneCodeItem(HashMap<String, Object> paramMap);
	void insertCodeItem(CodeItem ci)throws SQLException;
	void updateCodeItem(CodeItem ci) throws SQLException;
	void restoreCodeItem(CodeItem ci) throws SQLException;
	void deleteCodeItem(CodeItem ci) throws SQLException;
}
