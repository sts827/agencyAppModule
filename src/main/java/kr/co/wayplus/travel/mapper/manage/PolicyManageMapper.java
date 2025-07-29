package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.Policy;

@Mapper
@Repository
public interface PolicyManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */

	//	<!--################################### Policy ###################################-->
	int selectCountPolicy(HashMap<String, Object> paramMap);
	ArrayList<Policy> selectListPolicy(HashMap<String, Object> paramMap);
	Policy selectOnePolicy(HashMap<String, Object> paramMap);
	void insertPolicy(Policy param) throws SQLException;
	void updatePolicy(Policy param) throws SQLException;
	void deletePolicy(Policy param) throws SQLException;
	//	<!--################################### Policy ###################################-->
	int selectCountPolicyCategory(HashMap<String, Object> paramMap);
	ArrayList<Policy> selectListPolicyCategory(HashMap<String, Object> paramMap);
	Policy selectOnePolicyCategory(HashMap<String, Object> paramMap);
}
