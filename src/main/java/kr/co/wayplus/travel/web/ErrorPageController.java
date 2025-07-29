package kr.co.wayplus.travel.web;

import jakarta.servlet.RequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));
		logger.error(httpStatus.toString());

		switch(status.toString()) {
			case "400":
				mav.setViewName("error/400");
				break;
			case "403":
				mav.setViewName("error/403");
				break;
			case "404":
				mav.setViewName("error/404");
				break;
			case "405":
				mav.setViewName("error/405");
				break;
			case "500":
				mav.setViewName("error/500");
				break;
			default:
				mav.setViewName("error/error");
				break;
		}
		
		mav.addObject("httpStatus", httpStatus);
		return mav;
	}

	@RequestMapping("/error/denied")
	public ModelAndView denied(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/denied");
		return mav;
	}
}
