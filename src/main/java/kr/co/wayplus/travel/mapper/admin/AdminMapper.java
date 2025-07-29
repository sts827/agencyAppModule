package kr.co.wayplus.travel.mapper.admin;

import kr.co.wayplus.travel.model.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface AdminMapper {

    int selectAdministratorUserListCount();

    ArrayList<LoginUser> selectAdministratorUserList(HashMap<String, Object> param);

    LoginUser selectAdministratorInfo(String userEmail);

    void insertAdministrator(LoginUser user);

    void insertWithdrawalAdministrator(LoginUser user);

    void deleteAdministrator(LoginUser user);

    void updateAdministrator(LoginUser user);
}
