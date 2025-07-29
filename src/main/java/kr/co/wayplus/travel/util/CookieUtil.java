package kr.co.wayplus.travel.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.WebServiceLog;
import kr.co.wayplus.travel.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.UUID;

@Component
public class CookieUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;


    @Value("${cookie-set.domain}")
    private String COOKIE_DOMAIN;

    @Value("${cookie-set.prefix}")
    private String COOKIE_PREFIX;

    @Value("${cookie-set.tracking-age}")
    private int TRACKER_MAX_AGE;

    @Autowired
    public CookieUtil(UserService userService) {
        this.userService = userService;
    }

    public CookieUtil(UserService userService, String cookieDomain, String cookiePrefix, int trackerMaxAge){
        this.userService = userService;
        this.COOKIE_DOMAIN = cookieDomain;
        this.COOKIE_PREFIX = cookiePrefix;
        this.TRACKER_MAX_AGE = trackerMaxAge;
    }

    public String writeWebTrackingLog(HttpServletRequest request, HttpServletResponse response){
        String message = "";
        try {
            HttpSession session = request.getSession();
            LoginUser user = null;
            if (session.getAttribute("login") != null) {
                user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

            //트래킹 쿠키 확인 및 생성
            Cookie trackerCookie = getCookieByName(request, "tracker.id");
            if (trackerCookie == null) {
                //트래킹 쿠키가 없을 경우 생성
                if (user != null) {
                    if (user.getUserTokenId() == null) {
                        user.setUserTokenId(String.valueOf(UUID.randomUUID()));
                        userService.updateUserNewTokenId(user);
                    }
                    trackerCookie = createCookie("tracker.id", user.getUserTokenId(), TRACKER_MAX_AGE);
                } else {
                    trackerCookie = createCookie("tracker.id", String.valueOf(UUID.randomUUID()), TRACKER_MAX_AGE);
                }
                logger.debug("Create Tracker ID... " + trackerCookie.getName() + ":" + trackerCookie.getValue());
            } else {
                logger.debug("Exist Tracker ID... " + trackerCookie.getName() + ":" + trackerCookie.getValue());
                //트래킹 쿠키 ID 값과 사용자 ID 값이 다를 경우 처리
                if (user != null) {
                    if (user.getUserTokenId() != null && !user.getUserTokenId().equals(trackerCookie.getValue())) {
                        trackerCookie.setValue(user.getUserTokenId());
                        logger.debug("User Cookie Not Matched Update Tracker ID... " + trackerCookie.getName() + ":" + trackerCookie.getValue());
                    } else if (user.getUserTokenId() == null){
                        user.setUserTokenId(trackerCookie.getValue());
                        userService.updateUserNewTokenId(user);
                    }
                }
                trackerCookie.setMaxAge(TRACKER_MAX_AGE);
            }
            response.addCookie(trackerCookie);

            //타 페이지 호출을 위한 세션 트래킹 설정용
            if(session.getAttribute("tracker-id") == null){
                logger.debug("Create Session Tracker ID... ");
                session.setAttribute("tracker-id", trackerCookie.getValue());
            }
            writeWebServiceLog(request, response, user, trackerCookie);

            message = "Tracking Log Write... Token ID : " + trackerCookie.getValue();
        } catch (Exception e){
            message = e.getMessage();
            logger.error(e.getMessage());
        }

        return message;
    }

    /**
     * 설정된 쿠키ID명으로 저장된 쿠키를 찾는다
     * @param request
     * @param cookieId
     * @return savedCookie
     */
    public Cookie getCookieByName(HttpServletRequest request, String cookieId){
        Cookie resultCookie = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_PREFIX + cookieId)) {
                resultCookie = cookie;
                logger.debug("Find Cookie... " + cookie.getDomain() + " / " + cookie.getName() + " / " + cookie.getValue() + " / " + cookie.getMaxAge());
            }
        }
        return resultCookie;
    }

    public Cookie createCookie(String cookieId, String value, int maxAge){
        Cookie cookie = new Cookie(COOKIE_PREFIX + cookieId, value);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
    public Cookie createCookieWithOutPrefix(String cookieId, String value, int maxAge){
        Cookie cookie = new Cookie(cookieId, value);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    /**
     * 로그인 여부 및 Cookie 에 따른 웹 페이지 URL 호출에 대한 로그를 기록한다.
     * @param request
     * @param response
     * @param user
     * @param trackerCookie
     */
    public void writeWebServiceLog(HttpServletRequest request, HttpServletResponse response, LoginUser user, Cookie trackerCookie){
        //웹 로그 작성
        WebServiceLog webLog = new WebServiceLog();
        HttpSession session = request.getSession();
        webLog.setSessionId(session.getId());
        if (user != null) {
            webLog.setUserToken(user.getUserTokenId());
            webLog.setUserEmail(user.getUserEmail());
            webLog.setTracking("Y");
        } else {
            webLog.setUserToken(trackerCookie.getValue());
            webLog.setTracking("N");
        }

        if(request.getHeader("referer") != null && request.getHeader("referer").length() > 480) {
            webLog.setReferer(request.getHeader("referer").substring(0, 480));
        } else {
            webLog.setReferer(request.getHeader("referer"));
        }

        if(request.getHeader("user-agent") != null && request.getHeader("user-agent").length() > 480) {
            webLog.setRequestAgent(request.getHeader("user-agent").substring(0, 480));
        } else {
            webLog.setRequestAgent(request.getHeader("user-agent"));
        }

        webLog.setRequestHost(request.getRemoteHost());
        webLog.setRequestUri(request.getRequestURI());
        webLog.setResponseStatus(response.getStatus());
        userService.writeUserWebLog(webLog);
    }

}
