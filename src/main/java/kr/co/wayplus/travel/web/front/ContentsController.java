package kr.co.wayplus.travel.web.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.service.front.ContentsService;
import kr.co.wayplus.travel.service.front.PageService;

@Controller
@RequestMapping("/contents")
public class ContentsController  extends BaseController {

    @Value("${cookie-set.domain}")
    private String cookieDomain;
    @Value("${cookie-set.prefix}")
    private String cookieName;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PageService pageService; /*common하게*/
    private final ContentsService contentsService;

    @Autowired
    public ContentsController(PageService pageService, ContentsService contentsService) {
    	this.pageService = pageService;
        this.contentsService = contentsService;
    }

//    @GetMapping(value = {"/", "/index"})
//    public ModelAndView index(HttpServletRequest request){
//        ModelAndView mav = new ModelAndView();
//
//        mav.setViewName("index");
//        return mav;
//    }
//    @PutMapping("/popup/dismiss")
//    @ResponseBody
//    public HashMap<String, Object> popupDismiss (HttpServletResponse response,
//                                          @RequestParam(value="timeKey", defaultValue="") String timeKey,
//                                          @RequestParam(value="period", defaultValue="1") int period
//    ){
//        HashMap<String, Object> resultMap = new HashMap<>();
//        try {
//            Cookie cookie = new Cookie(cookieName + "popup." + timeKey, "0");
//            cookie.setMaxAge(period * 24 * 60 * 60);
//            cookie.setDomain(cookieDomain);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            resultMap.put("result", "success");
//            resultMap.put("message", "처리완료");
//        }catch (Exception e){
//            resultMap.put("result", "error");
//            resultMap.put("message", e.getMessage());
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//        return resultMap;
//    }

}
