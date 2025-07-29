package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.UserCustomerCounsel;
import kr.co.wayplus.travel.model.UserCustomerInfo;
import kr.co.wayplus.travel.model.UserCustomerOrderList;
import kr.co.wayplus.travel.model.UserCustomerPayment;

@Mapper
@Repository
public interface UserManageMapper {

    int selectUserListCount(HashMap<String, Object> param);

    ArrayList<LoginUser> selectUserList(HashMap<String, Object> param);

    void updateUserAccountStatus(HashMap<String, Object> param);

    //LoginUser selectUserDetail(String userEmail);

    LoginUser selectUserDetail(HashMap<String, Object> param);

    void insertUser(LoginUser user);

    UserCustomerInfo selectUserCustomerInfo(String userEmail);

    void updateUserInfoByManager(LoginUser user);

    void insertUserCustomerInfo(UserCustomerInfo customerInfo);

    int selectUserCustomerCounselListCount(HashMap<String, Object> param);

    ArrayList<UserCustomerCounsel> selectUserCustomerCounselList(HashMap<String, Object> userEmail);
    UserCustomerCounsel selectUserCustomerCounsel(HashMap<String, Object> param);

    void insertUserCustomerCounsel(UserCustomerCounsel counsel);

	void deleteUserCustomerCounsel(UserCustomerCounsel counsel) throws Exception;

	void restoreUserCustomerCounsel(UserCustomerCounsel counsel) throws Exception;

	void updateUserAccountSimpleInfo(HashMap<String, Object> param);

//	<!--################################### UserCustomerPayment( ###################################-->
	int selectCountUserCustomerPayment(HashMap<String, Object> paramMap);
	int selectCountUserCustomerPayment(UserCustomerPayment data);
	ArrayList<UserCustomerPayment> selectListUserCustomerPayment(HashMap<String, Object> paramMap);
	ArrayList<UserCustomerPayment> selectListUserCustomerPayment(UserCustomerPayment data);
	UserCustomerPayment selectOneUserCustomerPayment(HashMap<String, Object> paramMap);
	ArrayList<HashMap<String, Object>> selectListInquiryCountStatusType(HashMap<String, Object> paramMap);
	void insertUserCustomerPayment(UserCustomerPayment data)throws SQLException;
	void updateUserCustomerPayment(UserCustomerPayment data) throws SQLException;
	void restoreUserCustomerPayment(UserCustomerPayment data) throws SQLException;
	void deleteUserCustomerPayment(UserCustomerPayment data) throws SQLException;

	int selectCountUserCustomerPaymentVirtual(HashMap<String, Object> param);
	ArrayList<HashMap<String, Object>> selectListUserCustomerPaymentVirtual(HashMap<String, Object> param);

	HashMap<String, Object> selectListUserCustomerPaymentVirtualTotal(HashMap<String, Object> param);

//	<!--################################### UserCustomerPayment( ###################################-->
	Integer selectCountUserCustomerOrderList(HashMap param);
	ArrayList<UserCustomerOrderList> selectListUserCustomerOrderList(HashMap param);
	UserCustomerOrderList selectOneUserCustomerOrderList(HashMap param);
	void insertUserCustomerOrderList(UserCustomerOrderList ord) throws SQLException;
	void updateUserCustomerOrderList(UserCustomerOrderList ord);
	void deleteUserCustomerOrderList(UserCustomerOrderList ord);



}
