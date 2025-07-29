package kr.co.wayplus.travel.service.user;

import kr.co.wayplus.travel.mapper.user.UserMapper;
import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.util.CryptoUtil;
import kr.co.wayplus.travel.util.CustomBcryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Call loadUserByUserName. Username : " + username);
        LoginUser user = userMapper.selectUserByUserid(username);

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority( "ROLE_"+user.getUserRole() ));
        user.setAuthorities(roles);
        if(user == null) throw new UsernameNotFoundException(username);
        logger.debug("User Find. User Has Role : " + user.getAuthorities().toString());
        return user;
    }

    public void writeUserLoginLog(HashMap<String, String> parameterMap) {
        userMapper.insertUserLoginLog(parameterMap);
    }

    public void updateUserLastLoginDate(LoginUser user) {
        userMapper.updateUserLastLoginDate(user);
    }

    public int findUserCountById(String id) {
        return userMapper.selectUserCountById(id);
    }

    public void addUser(LoginUser user, boolean encrypted) throws RuntimeException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(encrypted){
            user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
        }
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertNewUser(user);
    }

    public void withdrawalUser(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(encrypted){
            user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
        }
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();

        LoginUser storedUser = userMapper.selectUserByUserid(user.getUsername());
        if(storedUser == null) throw new RuntimeException("사용자 정보가 없습니다.");
        if(passwordEncoder.directMatches(user.getUserPassword(), storedUser.getPassword())){
            userMapper.insertWithdrawalUser(storedUser);
            userMapper.deleteUserByUserid(storedUser);
        }else{
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    public ArrayList<LoginUser> findUserListByUserName(String name, String mobile) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("userName", name);
        param.put("userMobile", mobile);
        return userMapper.selectUserListByUserName(param);
    }

    public LoginUser findRePasswordUserByUserInfo(HashMap<String, Object> param) {
        return userMapper.selectRePasswordUserByUserInfo(param);
    }

    public void updateUserPasswordByLost(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(encrypted){
            user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
        }
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userMapper.updateUserPassword(user);
    }




    public void updateUserPassword(LoginUser user, String newPassword) throws Exception {
        LoginUser storedUser = userMapper.selectUserByUserid(user.getUserEmail());
        if(storedUser == null){
            throw new Exception("사용자를 찾을 수 없습니다.");
        }

        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        if(passwordEncoder.directMatches(user.getPassword(), storedUser.getPassword())){
            user.setUserPassword(passwordEncoder.encode(newPassword));
            userMapper.updateUserPassword(user);
        }else{
            throw new Exception("현재 비밀번호가 일치하지 않습니다.");
        }

    }

    public ArrayList<LoginUser> selectUserListByRole(String role) {
        return userMapper.selectUserListByRole(role);
    }


    public void updateUserSessionLogout(LoginUserSession loginUserSession) {
        userMapper.updateUserSessionLogout(loginUserSession);
    }

    public void writeUserLoginAttemptLog(LoginAttemptLog attemptLog) {
        userMapper.insertUserLoginAttemptLog(attemptLog);
    }

    public void updateUserNewTokenId(LoginUser user) {
        userMapper.updateUserNewTokenId(user);
    }

    public void updateUserWebLog(HashMap<String, String> param) {
        userMapper.updateUserWebLog(param);
    }

    public void writeUserWebLog(WebServiceLog webLog) {
        userMapper.insertUserWebLog(webLog);
    }


    public void updateAdminUserInfo(LoginUser user) throws Exception {
        LoginUser storedUser = userMapper.selectUserByUserid(user.getUserEmail());
        if(storedUser == null){
            throw new Exception("사용자를 찾을 수 없습니다.");
        }

        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        if(passwordEncoder.directMatches(user.getPassword(), storedUser.getPassword())){
            userMapper.updateUserInfo(user);
        }else{
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updateUserInfo(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            if (encrypted) {
                user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
            }
            CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        }
        userMapper.updateUserInfo(user);
    }






    public OAuthUser loadUserByOAuthRegistration(String registrationId, String id) {
        logger.debug(String.format("Call loadUserByOAuthRegistration. registration: %s, ID: %s", registrationId, id));
        OAuthUser user = userMapper.selectOAuthUser(registrationId, id);

        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority( "ROLE_"+user.getUserRole()));
        user.setAuthorities(roles);

        if(user == null) throw new UsernameNotFoundException(String.format("%s_%s", registrationId, id));
        logger.debug("User Find. User Has Role : " + user.getAuthorities().toString());
        return user;
    }

    public int findOAuthUserCount(String registrationId, String id) {
        return userMapper.selectOAuthUserCount(registrationId, id);
    }

    public void addOAuthUser(OAuthUser user) {
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(user.getUserTokenId()));
        userMapper.insertOAuthUser(user);
    }

    public void updateOAuthUserLastLoginDate(OAuthUser user) {
        userMapper.updateOAuthUserLastLoginDate(user);
    }

    public LoginUser findUserByIdToken(HashMap<String, Object> param) {
        return userMapper.selectUserByIdToken(param);
    }

	public void createUser(LoginUser user, boolean encrypted) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(encrypted){
            user.setUserPassword(CryptoUtil.aesDecode(user.getUserPassword(), user.getEncrypt(), user.getIv()));
        }
        CustomBcryptPasswordEncoder passwordEncoder = new CustomBcryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertNewUser(user);
    }
}
