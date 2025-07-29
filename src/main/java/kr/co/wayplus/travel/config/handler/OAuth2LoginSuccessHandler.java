package kr.co.wayplus.travel.config.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.model.OAuthUser;
import kr.co.wayplus.travel.service.user.UserService;
import kr.co.wayplus.travel.util.CookieUtil;
import kr.co.wayplus.travel.util.IPAddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final CookieUtil cookieUtil;
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Value("${cookie-set.domain}")
    private String cookieDomain;
    @Value("${cookie-set.prefix}")
    private String cookiePrefix;
    @Value("${cookie-set.tracking-age}")
    private int trackerMaxAge;

    public OAuth2LoginSuccessHandler(UserService userService, CookieUtil cookieUtil) {
        this.userService = userService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        logger.debug("OAuth2 Login Success");

        HttpSession session = request.getSession();
        if(authentication instanceof OAuth2AuthenticationToken){

        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuthUser user = (OAuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        logger.debug("Login Success. User id : " + user.getUserEmail() + ", Session Id : " + session.getId());

        session.setAttribute("login", user);
        session.setAttribute("loginType", "oauth");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        HashMap<String, String> parameterMap = new HashMap<>();
        parameterMap.put("userEmail", user.getUserEmail());
        parameterMap.put("sessionId", session.getId());
        parameterMap.put("loginIp", IPAddrUtil.getClientIpAddr(request));
        parameterMap.put("userAgent", request.getHeader("User-Agent"));
        parameterMap.put("loginType", "OAuth2");
        if(savedRequest != null) parameterMap.put("referer", savedRequest.getRedirectUrl());
        else parameterMap.put("referer", request.getHeader("Referer"));

        logger.debug("Write Login Log... Param: " + parameterMap);

        //로그인 로그 저장
        userService.writeUserLoginLog(parameterMap);

        //마지막 로그인 일자 업데이트
        userService.updateOAuthUserLastLoginDate(user);

        //추적용 쿠키 아이디 업데이트
        Cookie trackingCookie = cookieUtil.getCookieByName(request, "tracker.id");
        if(trackingCookie != null){
            if(!trackingCookie.getValue().equals(user.getUserTokenId())){
                HashMap<String, String> param = new HashMap<>();
                param.put("before", trackingCookie.getValue());
                param.put("after", user.getUserTokenId());
                userService.updateUserWebLog(param);
            }
        }

        //로그인 전 요청페이지로 리다이렉션
        if(savedRequest != null){
            logger.debug("Redirect Saved URL : " + savedRequest.getRedirectUrl());
            response.sendRedirect(savedRequest.getRedirectUrl());
        }else{
            response.sendRedirect("/");
        }

    }
}
