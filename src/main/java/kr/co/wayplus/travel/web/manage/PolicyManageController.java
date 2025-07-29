package kr.co.wayplus.travel.web.manage;

import java.util.ArrayList;
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
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.Policy;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.service.manage.PolicyManageService;

@Controller
@RequestMapping("/manage")
public class PolicyManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

    @Value("${upload.file.max-size}")
    int maxFileSize;

    private final PolicyManageService svcPolicy;
    public PolicyManageController(PolicyManageService svc1) {
        this.svcPolicy = svc1;
    }
////<!--################################### policy ###################################-->
    @GetMapping("/policy/list")
    public ModelAndView Policy(
    		@RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="pageSize", defaultValue="10") int pageSize,
		    @RequestParam(value="searchType", defaultValue="") String searchType,
		    @RequestParam(value="searchKey", defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> paramMap = new HashMap<String, Object>();

    	mav.setViewName("manage/sub/policy/list");
    	return mav;
    }

    @GetMapping("/policy/form")
    public ModelAndView policy_form(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		@RequestParam(value="id", defaultValue="0") String id){
        ModelAndView mav = new ModelAndView();

        if(mode.equals("U")) {
        	HashMap<String, Object> paramMap = new HashMap<>();
        	paramMap.put("id", id);
        	mav.addObject("policy",  svcPolicy.selectOnePolicy(paramMap));
        	paramMap.clear();
        	mav.addObject("p", paramMap);
        } else {
        	mav.addObject("policy",  new Policy());
        }
        mav.addObject("mode", mode);
        mav.addObject("policyCategory",  svcPolicy.selectListPolicyCategory(null));

        mav.setViewName("manage/sub/policy/form");
        return mav;
    }

    @PostMapping("/policy-list")
    @ResponseBody
   	public HashMap<String, Object> policy_list(
   			HttpServletRequest request,
   			Policy ts,
   			@Param(value="titleLike") String titleLike
   		){
   		HashMap<String, Object> resultMap = new HashMap<>();

   		try{
   			HashMap<String, Object> paramMap = new HashMap<>();
   			paramMap.put("titleLike", titleLike);

   			int totalCount = svcPolicy.selectCountPolicy(paramMap);
   			ArrayList<Policy> lists = svcPolicy.selectListPolicy(paramMap);

   			List<SortData> listSort = getListOrder(request);
   			paramMap.put("listSort", listSort);

	        resultMap.put("recordsTotal", totalCount);
	  		resultMap.put("recordsFiltered", totalCount);
	  		resultMap.put("data", lists);

   			resultMap.put("result", "success");
   			resultMap.put("message", "처리되었습니다.");
   		}catch (Exception e){
   			logger.error(e.getMessage());
   			resultMap.put("result", "error");
//   			resultMap.put("message", "처리중 오류가 발생하였습니다.");
   			resultMap.put("error", e.getMessage());
   			resultMap.put("message", e.getMessage());
   		}

   		return resultMap;
   	}

    @PostMapping("/policy-save")
	@ResponseBody
	public HashMap<String, Object> place_save(Policy ts){
		HashMap<String, Object> resultMap = new HashMap<>();
		try{
			Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;

				if(ts.getId() == null) {
					ts.setCreateId( user.getUserEmail() );
					svcPolicy.insertPolicy(ts);
				} else {
					ts.setLastUpdateId( user.getUserEmail() );
					svcPolicy.updatePolicy(ts);
				}

				resultMap.put("result", "success");
	    		resultMap.put("message", "처리가 완료 되었습니다.");
	    	} else {
	    		resultMap.put("result", "fail");
	    		resultMap.put("message", "로그인 문제가 발생되었습니다.");
	    	}
		}catch (Exception e){
			logger.error(e.getCause().getLocalizedMessage());
			resultMap.put("result", "error");
			resultMap.put("message", e.getMessage());

		}

		return resultMap;
	}

    @PostMapping("/policy/delete")
	@ResponseBody
	public HashMap<String, Object> place_delete(Policy ts){
		HashMap<String, Object> resultMap = new HashMap<>();
		try{
			svcPolicy.deletePolicy(ts);

			resultMap.put("result", "success");
			resultMap.put("message", "처리되었습니다.");
		}catch (Exception e){
			logger.error(e.getMessage());
			resultMap.put("result", "error");
			resultMap.put("error", e.getMessage());
		}

		return resultMap;
	}
}
