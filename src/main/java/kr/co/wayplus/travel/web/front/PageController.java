package kr.co.wayplus.travel.web.front;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.CodeItem;
import kr.co.wayplus.travel.model.InquiryContent;
import kr.co.wayplus.travel.model.PagingDTO;
import kr.co.wayplus.travel.model.Policy;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.service.front.PageService;

@Controller
public class PageController  extends BaseController {

    @Value("${cookie-set.domain}")
    private String cookieDomain;
    @Value("${cookie-set.prefix}")
    private String cookieName;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PageService pageService;

    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/terms_view")
    public ModelAndView termsView(
    		HttpServletRequest request,
    		Policy bc,
    		@RequestParam(value="id") Integer id ){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> paramMap = new HashMap<>();

        paramMap.put("id", id);

        mav.addObject("p", paramMap);
        mav.addObject("content", pageService.selectOnePolicy(paramMap));

        mav.setViewName("test_terms_view");
        return mav;
    }


    @PutMapping("/popup/dismiss")
    @ResponseBody
    public HashMap<String, Object> popupDismiss (HttpServletResponse response,
                                          @RequestParam(value="timeKey", defaultValue="") String timeKey,
                                          @RequestParam(value="period", defaultValue="1") int period
    ){
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            Cookie cookie = new Cookie(cookieName + "popup." + timeKey, "0");
            cookie.setMaxAge(period * 24 * 60 * 60);
            cookie.setDomain(cookieDomain);
            cookie.setPath("/");
            response.addCookie(cookie);
            resultMap.put("result", "success");
            resultMap.put("message", "처리완료");
        }catch (Exception e){
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
//	<!--################################### Code ###################################-->

  //inquiry
      @GetMapping("/inquiry/list")
      public ModelAndView inquiryList(
      		HttpServletRequest request,
      		InquiryContent ic,
      		@RequestParam(value="page", defaultValue="1") int page,
      		@RequestParam(value="pageSize", defaultValue="5") int pageSize,
      		@Param(value="titleLike") String titleLike,
      		@Param(value="contentLike") String contentLike){
          ModelAndView mav = new ModelAndView();

          try {
      		HashMap<String, Object> paramMap = new HashMap<>();

      		List<SortData> listSort = getListOrder(request);
      		paramMap.put("listSort", listSort);

  			paramMap.put("titleLike", titleLike);
  			paramMap.put("contentLike", contentLike);

      		int totalCount = pageService.selectCountInquiryContent(paramMap);

      		PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
  			paramMap.put("itemStartPosition", paging.getItemStartPosition());
  			paramMap.put("pagePerSize", paging.getPagePerSize());

              mav.addObject("p", paramMap);
              mav.addObject("inquiryList", pageService.selectListInquiryContent(paramMap));
              mav.addObject("paging", paging);

  		} catch (Exception e) {
  			mav.addObject("inquiryList", null);
  			logger.error(e.getMessage());
  		}

          mav.setViewName("_test/test_inquiry_list");
          return mav;
      }

      @GetMapping("/inquiry/view")
      public ModelAndView inquiryView(
      		HttpServletRequest request,
      		InquiryContent ic,
      		@RequestParam(value="id") Integer id ){
          ModelAndView mav = new ModelAndView();

          HashMap<String, Object> paramMap1 = new HashMap<>();
          paramMap1.put("id", id);

          InquiryContent data = pageService.selectOneInquiryContent(paramMap1);
          mav.addObject("content", data);

          HashMap<String, Object> paramMap2 = new HashMap<>();
          paramMap2.put("id", data.getCategoryId());
          mav.addObject("category", pageService.selectOneInquiryCategory(paramMap2));

          mav.addObject("p1", paramMap1);
          mav.addObject("p2", paramMap2);

          mav.setViewName("_test/test_inquiry_view");
          return mav;
      }

      @GetMapping("/inquiry/form")
      public ModelAndView inquiryForm(
      		HttpServletRequest request,
      		InquiryContent ic,
      		@Param(value="id") Integer id,
      		@RequestParam(value="mode", defaultValue="I") String mode ){
          ModelAndView mav = new ModelAndView();

          HashMap<String, Object> paramMap = new HashMap<>();


          paramMap.put("useYn","Y");
          paramMap.put("deleteYn","N");
          paramMap.put("sort","orderNum");
          paramMap.put("sortOrder","ASC");

          mav.addObject("listCategory",  pageService.selectListInquiryCategory( paramMap ));


          paramMap.clear();

          mav.addObject("mode", mode);

          if( mode.equals("I") ) {
          	mav.addObject("inquiry", new InquiryContent());
          } else {
          	paramMap.put("id", id);
          	mav.addObject("inquiry", pageService.selectOneInquiryContent(paramMap));
          	mav.addObject("id", id);
          }
  /*
          mav.addObject("p", paramMap);
          mav.addObject("content", pageService.selectOneInquiryContent(paramMap));
  */
          mav.setViewName("_test/test_inquiry_form");
          return mav;
      }

      @PostMapping("/inquiry/save")
      @ResponseBody
      public HashMap<String, Object> inquiry_save_ajax(
      		@RequestParam(value="mode", defaultValue="I") String mode,
      		@ModelAttribute InquiryContent ic,
      		//Multipart
      		HttpServletRequest request
      	){
      	HashMap<String, Object> retrunMap = new HashMap<>();

      	try {
      		//Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

          	//if(_user instanceof LoginUser) {
          		//LoginUser user = (LoginUser)_user;
          		HashMap<String, Object> paramMap = new HashMap<>();

  	    		if(mode.equals("I")) {
  	    			pageService.insertInquiryContent(ic);
  	    		} else {
  	    			pageService.updateInquiryContent(ic);
  	    		}
  	    		retrunMap.put("result", "success");
  	    		retrunMap.put("message", "처리가 완료 되었습니다.");
          	/*} else {
          		retrunMap.put("result", "fail");
  	    		retrunMap.put("message", "로그인 문제가 발생되었습니다.");
          	}*/
  		} catch (Exception e) {
  			retrunMap.put("result", "error");
  			retrunMap.put("message", "처리중 문제가 발생했습니다.");
  			retrunMap.put("info", e.getMessage());
  			logger.error(e.getCause().getMessage());
  		}
          return retrunMap;
      }
//	<!--################################### CodeItem ###################################-->
    @GetMapping("/code-list")
    @ResponseBody
    public HashMap<String, Object> code_list_ajax(HttpServletRequest request,
    		@ModelAttribute CodeItem ci,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="code") String code,
    		@Param(value="useYn") String useYn
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

    		List<SortData> listSort = getListOrder(request);
    		paramMap.put("listSort", listSort);

    		if(length >= 0) {
				paramMap.put("itemStartPosition", start);
				paramMap.put("pagePerSize", length);
    		}

			paramMap.put("code", ci.getCode());
			paramMap.put("useYn", useYn);
			paramMap.put("upperCode", ci.getUpperCode());

    		int totalCount = pageService.selectCountCodeItem(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", pageService.selectListCodeItem(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }
}
