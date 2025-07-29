package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.util.*;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthUser implements OAuth2User {
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
    private String userMembershipEnd;
    private String userVerifiedEmail;
    private String userVerifiedMobile;
    private String userCi;
    private String userDi;
    private String userDiCorp;
    private String userJoinDate;
    private String userModifyDate;
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
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;



    private Set<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    public OAuthUser(){};

    public OAuthUser(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        Assert.notEmpty(attributes, "attributes cannot be empty");
        Assert.hasText(nameAttributeKey, "nameAttributeKey cannot be empty");
        if (!attributes.containsKey(nameAttributeKey)) {
            throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
        }
        this.authorities = (authorities != null)
                ? Collections.unmodifiableSet(new LinkedHashSet<>(this.sortAuthorities(authorities)))
                : Collections.unmodifiableSet(new LinkedHashSet<>(AuthorityUtils.NO_AUTHORITIES));
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        this.nameAttributeKey = nameAttributeKey;
    }

    public void setNewUserInfo(String email, String name, String registrationId) {
        this.userEmail = email;
        this.userName = name;
        this.userJoinType = registrationId;
    }

    @Override
    public String getName() {
        return this.getAttribute(this.nameAttributeKey).toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

}
