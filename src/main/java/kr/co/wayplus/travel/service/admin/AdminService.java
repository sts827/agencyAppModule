package kr.co.wayplus.travel.service.admin;

import kr.co.wayplus.travel.mapper.admin.AdminMapper;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.util.CryptoUtil;
import kr.co.wayplus.travel.util.CustomBcryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AdminService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AdminMapper adminMapper;

    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public int getAdministratorUserListCount() {
        return adminMapper.selectAdministratorUserListCount();
    }

    public ArrayList<LoginUser> getAdministratorUserList(HashMap<String, Object> param) {
        return adminMapper.selectAdministratorUserList(param);
    }

    public LoginUser getAdministratorInfo(String userEmail) {
        return adminMapper.selectAdministratorInfo(userEmail);
    }

    public void createAdministrator(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(encrypted){
            user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
        }
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(user.getPassword()));
        adminMapper.insertAdministrator(user);
    }

    public void withdrawalAdministrator(LoginUser user) {
        adminMapper.insertWithdrawalAdministrator(user);
        adminMapper.deleteAdministrator(user);
    }

    public void updateAdministrator(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            if (encrypted) {
                user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
            }
            CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        }
        adminMapper.updateAdministrator(user);
    }
}
