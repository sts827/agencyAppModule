package kr.co.wayplus.travel.mapper.user;

import kr.co.wayplus.travel.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface UserMapper {

    LoginUser selectUserByUserid(String username);

    void insertUserLoginLog(HashMap<String, String> parameterMap);

    void updateUserLastLoginDate(LoginUser user);

    int selectUserCountById(String id);

    void insertNewUser(LoginUser user);

    void insertWithdrawalUser(LoginUser storedUser);

    void deleteUserByUserid(LoginUser user);

    ArrayList<LoginUser> selectUserListByUserName(HashMap<String, Object> param);

    LoginUser selectRePasswordUserByUserInfo(HashMap<String, Object> param);

    void updateUserPassword(LoginUser user);

    String selectUserLastLoginTime(HashMap<String, String> userEmail);


    ArrayList<LoginUser> selectUserListByRole(String role);

    void updateUserSessionLogout(LoginUserSession loginUserSession);

    void insertUserLoginAttemptLog(LoginAttemptLog attemptLog);

    void updateUserNewTokenId(LoginUser user);

    void updateUserWebLog(HashMap<String, String> param);

    void insertUserWebLog(WebServiceLog webLog);

    void updateUserInfo(LoginUser user);


    int selectOAuthUserCount(@Param("registrationId") String registrationId, @Param("id") String id);

    OAuthUser selectOAuthUser(@Param("registrationId") String registrationId, @Param("id") String id);

    void insertOAuthUser(OAuthUser user);

    void updateOAuthUserLastLoginDate(OAuthUser user);

    LoginUser selectUserByIdToken(HashMap<String, Object> param);
}
