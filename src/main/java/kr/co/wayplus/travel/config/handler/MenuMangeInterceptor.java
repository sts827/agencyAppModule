package kr.co.wayplus.travel.config.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.wayplus.travel.service.manage.ManageService;

/**
 * 관리자 사이트의 메뉴정보를 제공하기위함.
 */
@Component
public class MenuMangeInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    ManageService svc;

    @Autowired
    public MenuMangeInterceptor(ManageService svc) {
        this.svc = svc;
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("================== meneu manage Interceptor [[postHandle]] ==================");
		logger.debug(request.getRequestURI());

		try {
			if(modelAndView != null) {
				if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
					HashMap<String, Object> retMap = new HashMap<String, Object>();
					HashMap<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("useYn", "Y");
					svc.selectList(paramMap, retMap);
					modelAndView.addObject("listTopMenu", retMap.get("data"));
					modelAndView.addObject("list", retMap.get("list"));
				}
			}
		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}


}
