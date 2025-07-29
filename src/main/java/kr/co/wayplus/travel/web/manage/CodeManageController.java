package kr.co.wayplus.travel.web.manage;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.CodeItem;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.service.manage.CodeManageService;

@Controller
@RequestMapping("/manage/code")
public class CodeManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

	final String addPath = "images/";

    @Value("${upload.file.max-size}")
    int maxFileSize;

    private final CodeManageService svc;
    public CodeManageController(CodeManageService svc1) {
        this.svc = svc1;
    }

//	<!--################################### Code ###################################-->
    @GetMapping("/list")
    public ModelAndView code_list(){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> paramMap = new HashMap<>();

        mav.setViewName("manage/sub/code/list");
        return mav;
    }

//	<!--################################### CodeItem ###################################-->
    @PostMapping("/item-list")
    @ResponseBody
    public HashMap<String, Object> code_list_ajax(HttpServletRequest request, CodeItem ci,
    		//@RequestParam(value="start", defaultValue="0") int start,
    		//@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="categoryId") String categoryId,
    		@Param(value="applyCode") String applyCode,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

    		List<SortData> listSort = getListOrder(request);
    		paramMap.put("listSort", listSort);
/*
    		if(length >= 0) {
				paramMap.put("itemStartPosition", start);
				paramMap.put("pagePerSize", length);
    		}
*/
			paramMap.put("categoryId", categoryId);
			paramMap.put("applyCode", applyCode);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

    		int totalCount = svc.selectCountCodeItem(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", svc.selectListCodeItem(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/item-save")
    @ResponseBody
    public HashMap<String, Object> code_save_ajax(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		CodeItem ci,
    		//Multipart
    		HttpServletRequest request
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		paramMap.put( "code", ci.getCode() );
        		paramMap.put( "upperCode", ci.getUpperCode() );
        		//paramMap.put( "codeDepth", ci.getCodeDepth() );


	    		if(mode.equals("I")) {
	    			if( svc.selectCountCodeItem(paramMap) > 0) { /*중복체크*/
	        			retrunMap.put("result", "duplicate");
			    		retrunMap.put("message", "코드 중복이 발생하였습니다.");
			    		return retrunMap;
	        		}

	        		svc.insertCodeItem(ci);
	    		} else if(mode.equals("U")) {
	    			svc.updateCodeItem(ci);
	    		}

	    		retrunMap.put("result", "success");
	    		retrunMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retrunMap.put("result", "fail");
	    		retrunMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
		} catch (Exception e) {
			retrunMap.put("result", "error");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			retrunMap.put("info", e.getMessage());
			logger.error(e.getCause().getMessage());
		}
        return retrunMap;
    }

    @PostMapping("/item-delete")
    @ResponseBody
    public HashMap<String, Object> code_delete_ajax(
    		CodeItem ci
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> param = new HashMap<>();

    		param.put("upperCode", ci.getCode());

    		if( svc.selectCountCodeItem( param ) == 0 ) {
    			svc.deleteCodeItem(ci);
    			retrunMap.put("result", "success");
    			retrunMap.put("message", "처리가 완료 되었습니다.");
    		} else {
    			retrunMap.put("result", "children");
    			retrunMap.put("message", "사용중인 공통코드 항목이 있습니다.");
    		}

		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }
}
