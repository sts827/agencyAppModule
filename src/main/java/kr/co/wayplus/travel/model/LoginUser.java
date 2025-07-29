package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUser implements UserDetails {
    private String userEmail;
    private String userPassword;
    private String userJoinType;
    private String userName;
    private String userMobile;
    private String userTel;
    private String userAddrZipcode;
    private String userAddrExtra;
    private String userAddrJibun;
    private String userAddrRoad;
    private String userAddrDetail;
    private String userNationality;
    private String userBirthday;
    private String userGender;
    private String userRole;
    private String userClassName;
    private String userClassCode;
    private String userGroupCode;
    private String userTokenId;
    private int userGradeId;
    private String userGrade;
    private String userGradeStart;
    private String userGradeEnd;
    private int userMembershipId;
    private String userMembershipGrade;
    private String userMembershipStart;
    private String userMembershipStartFormat;
    private String userMembershipEnd;
    private String userMembershipEndFormat;
    private String userVerifiedEmail;
    private String userVerifiedMobile;
    private String userCi;
    private String userDi;
    private String userDiCorp;
    private String userJoinDate;
    private String userModifyDate;
    private String userMemo; /*사용자 메모*/
    private String lastLoginDate;
    private String lastPasswordDate;
    private int lastLoginFailCount;
    private String accountStatus;
    private String accountType;
    private String naverToken;
    private String naverEmail;
    private String naverJoinDate;
    private String kakaoToken;
    private String kakaoEmail;
    private String kakaoJoinDate;
    private String googleToken;
    private String googleEmail;
    private String googleJoinDate;
    private String facebookToken;
    private String facebookEmail;
    private String facebookJoinDate;
    private String secondaryEmail;
    private int privacyRetentionDays;
    private String mailingYn;
    private List<Role> roles;
    private List<GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    private String encrypt;
    private String iv;
    private String operator;
    private String userTeam;

    //UserDetail 의 Username 으로인해 자동처리 안되어 생성
    public String getUserName() {
        return userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public LoginUser addGuestUser(String uuidData) {
    	this.userEmail = uuidData;
    	this.userTokenId = uuidData;
    	return this;
    }

    public LoginUser addUserName(String userName) {
    	this.userName = userName;
    	return this;
    }
    public LoginUser addUserJoinType(String userJoinType) {
    	this.userJoinType = userJoinType;
    	return this;
    }

	public LoginUser addEncrypt(String Encrypt) {
		this.encrypt = Encrypt;
		return this;
	}
	public LoginUser addIv(String Iv) {
		this.iv = Iv;
		return this;
	}

	public LoginUser addUserPassworad(String UserPassword) {
		this.userPassword = UserPassword;
		return this;
	}

}
