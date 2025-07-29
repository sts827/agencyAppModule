package kr.co.wayplus.travel.service.manage;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wayplus.travel.mapper.manage.UserManageMapper;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.PagingDTO;
import kr.co.wayplus.travel.model.UserCustomerCounsel;
import kr.co.wayplus.travel.model.UserCustomerInfo;
import kr.co.wayplus.travel.model.UserCustomerOrderList;
import kr.co.wayplus.travel.model.UserCustomerPayment;
import kr.co.wayplus.travel.util.CryptoUtil;
import kr.co.wayplus.travel.util.CustomBcryptPasswordEncoder;

@Service
public class UserManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserManageMapper userManageMapper;

    public UserManageService(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    public int getUserListCount(HashMap<String, Object> param) {
        return userManageMapper.selectUserListCount(param);
    }

    public ArrayList<LoginUser> getUserList(HashMap<String, Object> param) {
        return userManageMapper.selectUserList(param);
    }

    public void changeUserAccountStatus(HashMap<String, Object> param) {
        userManageMapper.updateUserAccountStatus(param);
    }

    public LoginUser getUserDetail(String userEmail) {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	param.put("userEmail",userEmail);
    	param.put("userRole","USER");

        return this.getUserDetail(param);
    }

    public LoginUser getUserDetail(HashMap<String, Object> param) {
        return userManageMapper.selectUserDetail(param);
    }

    public void createUser(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(encrypted){
            user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
        }
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(user.getPassword()));
        userManageMapper.insertUser(user);
    }

    public UserCustomerInfo getUserCustomerInfo(String userEmail) {
        return userManageMapper.selectUserCustomerInfo(userEmail);
    }

    public void updateUserInfoByManager(LoginUser user) {
        userManageMapper.updateUserInfoByManager(user);
    }

    public void writeUserCustomerInfo(UserCustomerInfo customerInfo) {
        userManageMapper.insertUserCustomerInfo(customerInfo);
    }

    public int getUserCustomerCounselListCount(String userEmail) {
    	 HashMap<String, Object> param = new HashMap<>();
         param.put("userEmail", userEmail);
        return userManageMapper.selectUserCustomerCounselListCount(param);
    }

    public int getUserCustomerCounselListCount(HashMap<String, Object> param) {
    	return userManageMapper.selectUserCustomerCounselListCount(param);
    }

    public ArrayList<UserCustomerCounsel>  getUserCustomerCounselList(LoginUser user, PagingDTO paging) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("userEmail", user.getUserEmail());
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        return this.getUserCustomerCounselList(param);
    }

    public ArrayList<UserCustomerCounsel>  getUserCustomerCounselList(HashMap<String, Object> param) {
        return userManageMapper.selectUserCustomerCounselList(param);
    }

    public UserCustomerCounsel getUserCustomerCounsel(HashMap<String, Object> param) {
        return userManageMapper.selectUserCustomerCounsel(param);
    }

    public void writeUserCustomerCounsel(UserCustomerCounsel counsel) {
        userManageMapper.insertUserCustomerCounsel(counsel);
    }

	public void deleteUserCustomerCounsel(UserCustomerCounsel counsel) throws Exception {
		userManageMapper.deleteUserCustomerCounsel(counsel);
	}

	public void restoreUserCustomerCounsel(UserCustomerCounsel counsel) throws Exception {
		userManageMapper.restoreUserCustomerCounsel(counsel);
	}

	public void changeUserAccountSimpleInfo(HashMap<String, Object> param) {
		userManageMapper.updateUserAccountSimpleInfo(param);
	}

//	<!--################################### UserCustomerPayment ###################################-->
	public int selectCountUserCustomerPayment(HashMap<String, Object> paramMap) {
		return userManageMapper.selectCountUserCustomerPayment(paramMap);
	}
	public int selectCountUserCustomerPayment(UserCustomerPayment data) {
		return userManageMapper.selectCountUserCustomerPayment(data);
	}
	public ArrayList<UserCustomerPayment> selectListUserCustomerPayment(HashMap<String, Object> paramMap) {
		return userManageMapper.selectListUserCustomerPayment(paramMap);
	}
	public UserCustomerPayment selectOneUserCustomerPayment(HashMap<String, Object> paramMap) {
		return userManageMapper.selectOneUserCustomerPayment(paramMap);
	}
	public ArrayList<HashMap<String, Object>> selectListInquiryCountStatusType(HashMap<String, Object> paramMap) {
		return userManageMapper.selectListInquiryCountStatusType(paramMap);
	}
	public void saveUserCustomerPayment(UserCustomerPayment data) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", data.getId());

		if( this.selectCountUserCustomerPayment(paramMap) == 0) {
			userManageMapper.insertUserCustomerPayment(data);
		} else {
			userManageMapper.updateUserCustomerPayment(data);
		}
	}
	public void insertUserCustomerPayment(UserCustomerPayment data) throws SQLException {
		userManageMapper.insertUserCustomerPayment(data);
	}
	public void updateUserCustomerPayment(UserCustomerPayment data) throws SQLException {
		userManageMapper.updateUserCustomerPayment(data);
	}
	public void restoreUserCustomerPayment(UserCustomerPayment data) throws SQLException {
		userManageMapper.restoreUserCustomerPayment(data);
	}
	public void deleteUserCustomerPayment(UserCustomerPayment data) throws SQLException {
		userManageMapper.deleteUserCustomerPayment(data);
	}

	public int selectCountUserCustomerPaymentVirtual(HashMap<String, Object> param) {
		return userManageMapper.selectCountUserCustomerPaymentVirtual(param);
	}
	public ArrayList<HashMap<String, Object>> selectListUserCustomerPaymentVirtual(HashMap<String, Object> param) {
		return userManageMapper.selectListUserCustomerPaymentVirtual(param);
	}

	public HashMap<String, Object> selectListUserCustomerPaymentVirtualTotal(HashMap<String, Object> param) {
		return  userManageMapper.selectListUserCustomerPaymentVirtualTotal(param);
	}
//	<!--################################### UserCustomerPayment( ###################################-->
	public Integer selectCountUserCustomerOrderList(HashMap param) {
		return userManageMapper.selectCountUserCustomerOrderList(param);
	}
	public ArrayList<UserCustomerOrderList> selectListUserCustomerOrderList(HashMap param) {
		return userManageMapper.selectListUserCustomerOrderList(param);
	}
	public UserCustomerOrderList selectOneUserCustomerOrderList(HashMap param) {
		return userManageMapper.selectOneUserCustomerOrderList(param);
	}
	@Transactional
	public void insertUserCustomerOrderList(UserCustomerOrderList ord) throws SQLException{
		userManageMapper.insertUserCustomerOrderList(ord) ;
	}
	@Transactional
	public void updateUserCustomerOrderList(UserCustomerOrderList ord) throws SQLException{
		userManageMapper.updateUserCustomerOrderList(ord) ;
	}
	@Transactional
	public void deleteUserCustomerOrderList(UserCustomerOrderList ord) throws SQLException{
		userManageMapper.deleteUserCustomerOrderList(ord) ;
	}
}
