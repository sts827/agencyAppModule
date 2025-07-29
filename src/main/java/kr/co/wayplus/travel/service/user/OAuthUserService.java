package kr.co.wayplus.travel.service.user;

import kr.co.wayplus.travel.model.OAuthAttributes;
import kr.co.wayplus.travel.model.OAuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuthUserService extends DefaultOAuth2UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final UserPointService userPointService;

    public OAuthUserService(UserService userService, UserPointService userPointService) {
        this.userService = userService;
        this.userPointService = userPointService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        logger.debug("OAuth2 Login Load User");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.build(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        OAuthUser user = new OAuthUser(AuthorityUtils.createAuthorityList("ROLE_USER"), attributes.getAttributes(), attributes.getNameAttributeKey());
        if(userService.findOAuthUserCount(registrationId, attributes.getId()) > 0){
            user = userService.loadUserByOAuthRegistration(registrationId, attributes.getId());
            user.setAttributes(attributes.getAttributes());
            user.setNameAttributeKey(userNameAttributeName);
            logger.debug(String.format("Find Exist User %s_%s", registrationId, attributes.getId()));
        }else{
            //신규 유저
            user.setNewUserInfo(attributes.getEmail(), attributes.getName(), registrationId);
            user.setUserTokenId(String.format("%s_%s", registrationId, attributes.getId()));
            switch (registrationId){
                case "kakao":
                    user.setKakaoToken(attributes.getId());
                    user.setKakaoEmail(attributes.getEmail());
                    break;
                case "naver":
                    user.setNaverToken(attributes.getId());
                    user.setNaverEmail(attributes.getEmail());
                    break;
                case "google":
                    user.setGoogleToken(attributes.getId());
                    user.setGoogleEmail(attributes.getEmail());
                    break;
            }
            userService.addOAuthUser(user);
            userPointService.createJoinPoint(user);
            logger.debug("Create New User");
        }

        return user;
    }


}
