package kr.co.wayplus.travel.web.manage;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.service.manage.ManageService;
import kr.co.wayplus.travel.service.manage.MenuManageService;
import kr.co.wayplus.travel.util.CryptoUtil;
import org.apache.coyote.Response;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manage")
public class ManageController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String tourTemplateUrl = "/manage/sub/product";

	final String addPath = "product/";

	@Value("${upload.file.path}")
	private String imageUploadPath;


	@Value("${upload.file.max-size}")
	int maxFileSize;

	private ManageService service;

	private final MenuManageService menuManageService;

	@Autowired
	private ManageController(ManageService svc, MenuManageService menuManageService) {
		this.service = svc;
		this.menuManageService = menuManageService;
	}

    @GetMapping(value={"", "/", "/index"})
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/index");
        return mav;
    }

    /*################################################statistics################################################*/
	@GetMapping("/statistics")
	public ModelAndView statistics(
		@RequestParam(value="page", defaultValue="1") int page,
		@RequestParam(value="pageSize", defaultValue="10") int pageSize){
		ModelAndView mav = new ModelAndView();

		HashMap<String, Object> paramMap = new HashMap<>();
		mav.addObject("p", paramMap);
		mav.setViewName("manage/sub/statistics/list");
  	return mav;
  }

  @GetMapping("/statistics-date")
  @ResponseBody
  public Map<String, Object>  statistics_date(
  		@RequestParam Map<String, Object> paramMap){
  	Map<String, Object> resultMap = new HashMap<>();
  	try{
  		resultMap.put("p", paramMap);

	    	resultMap.put("list", service.selectListStatisticDate(paramMap));
	    	resultMap.put("info", service.selectListStatisticInfo(paramMap));
	    	resultMap.put("result", "success");
			resultMap.put("message", "처리되었습니다.");
	    }catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			resultMap.put("result", "error");
			resultMap.put("error", e.getMessage());
		}

  	return resultMap;
  }


    /*###########################################################################################*/
    @GetMapping("/icon")
    public ModelAndView icon(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/icon/icon");
        return mav;
    }




}
