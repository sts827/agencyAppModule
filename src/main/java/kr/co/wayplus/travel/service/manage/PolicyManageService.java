package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.PolicyManageMapper;
import kr.co.wayplus.travel.model.Policy;

@Service
public class PolicyManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PolicyManageMapper mapper;

    public PolicyManageService(PolicyManageMapper mapper) {
        this.mapper = mapper;
    }

//	<!--################################### Policy ###################################-->
    public int selectCountPolicy(HashMap<String, Object> paramMap) {
    	return mapper.selectCountPolicy(paramMap);
    }
    public ArrayList<Policy> selectListPolicy(HashMap<String, Object> paramMap) {
    	return mapper.selectListPolicy(paramMap);
    }
	public Policy selectOnePolicy(HashMap<String, Object> paramMap) {
		return mapper.selectOnePolicy(paramMap);
	}
	public void insertPolicy(Policy param) throws SQLException {
		mapper.insertPolicy(param);
	}
	public void updatePolicy(Policy param) throws SQLException {
		mapper.updatePolicy(param);
	}
	public void deletePolicy(Policy param) throws SQLException {
		mapper.deletePolicy(param);
	}
//	<!--################################### Policy ###################################-->
    public int selectCountPolicyCategory(HashMap<String, Object> paramMap) {
    	return mapper.selectCountPolicyCategory(paramMap);
    }
    public ArrayList<Policy> selectListPolicyCategory(HashMap<String, Object> paramMap) {
    	return mapper.selectListPolicyCategory(paramMap);
    }
	public Policy selectOnePolicyCategory(HashMap<String, Object> paramMap) {
		return mapper.selectOnePolicyCategory(paramMap);
	}
}
