package kr.co.wayplus.travel.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.OAuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {

                HttpSession session = request.getSession();
                if(session.getAttribute("loginType") != null){
                    if(session.getAttribute("loginType").equals("form")){
                        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        logger.debug("Access Denied. Path : " + request.getRequestURI() + ", User : " + user.getUserEmail() + ", Authorities : " + user.getAuthorities());
                    }else{
                        OAuthUser user = (OAuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        logger.debug("Access Denied. Path : " + request.getRequestURI() + ", User : " + user.getUserEmail() + ", Authorities : " + user.getAuthorities());
                    }
                }
            } else {
                logger.debug("Access Denied. Path : " + request.getRequestURI());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/error/denied").forward(request, response);
    }
}
