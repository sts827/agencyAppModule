package kr.co.wayplus.travel.web.manage;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.PagingDTO;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.model.UserCustomerCounsel;
import kr.co.wayplus.travel.model.UserCustomerInfo;
import kr.co.wayplus.travel.model.UserCustomerPayment;
import kr.co.wayplus.travel.service.manage.UserManageService;
import kr.co.wayplus.travel.util.LoggerUtil;

@Controller
@RequestMapping("/manage/user")
public class UserManageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserManageService userManageService;

    @Value("${key.crypto.encrypt}")
    private String encrypt;
    @Value("${key.crypto.iv}")
    private String iv;

    public UserManageController(UserManageService userManageService) {
        this.userManageService = userManageService;
    }

    @GetMapping("/list")
    public ModelAndView userList(@RequestParam(value="page",defaultValue="1") int page,
                                 @RequestParam(value="pageSize",defaultValue="20") int pageSize,
                                 @RequestParam(value="type",defaultValue="ALL") String type,
                                 @RequestParam(value="searchKey",defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/user/list");

        HashMap<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("searchKey", searchKey);

        int totalCount = userManageService.getUserListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("userList", userManageService.getUserList(param));
        mav.addObject("paging", paging);

        return mav;
    }

    @GetMapping("/view")
    public ModelAndView userForm(@RequestParam(value="userEmail", defaultValue="") String userEmail){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/user/view");
        mav.addObject("user", userManageService.getUserDetail(userEmail));
        mav.addObject("customer", userManageService.getUserCustomerInfo(userEmail));
        return mav;
    }

//<!--################################### counsel ###################################-->
    @GetMapping("/counsel/list")
    public ModelAndView userCounselList(@RequestParam(value="page",defaultValue="1") int page,
                                 @RequestParam(value="pageSize",defaultValue="20") int pageSize,
                                 @RequestParam(value="type",defaultValue="ALL") String type,
                                 @RequestParam(value="searchKey",defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/user/counsel/list");
/*
        HashMap<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("searchKey", searchKey);

        int totalCount = userManageService.getUserListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("userList", userManageService.getUserList(param));
        mav.addObject("paging", paging);
*/
        return mav;
    }

    @GetMapping("/counsel/view")
    public ModelAndView userCounselView(
    		@RequestParam(value="id", defaultValue="") String id,
    		@RequestParam HashMap<String, Object> param){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/user/counsel/view");
        UserCustomerCounsel data = userManageService.getUserCustomerCounsel(param);
        String userEmail = data.getUserEmail();

        mav.addObject("counsel", data);
        mav.addObject("id", id);
        mav.addObject("user", userManageService.getUserDetail(userEmail));

        return mav;
    }

    @GetMapping("/counsel/form")
    public ModelAndView userCounselForm(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/user/counsel/form");

        return mav;
    }

//<!--################################### payment ###################################-->
      @GetMapping("/payment/list")
      public ModelAndView userPaymentList(@RequestParam(value="page",defaultValue="1") int page,
                                   @RequestParam(value="pageSize",defaultValue="20") int pageSize,
                                   @RequestParam(value="type",defaultValue="ALL") String type,
                                   @RequestParam(value="searchKey",defaultValue="") String searchKey){
          ModelAndView mav = new ModelAndView();
          mav.setViewName("manage/sub/user/payment/list");
/*
          HashMap<String, Object> param = new HashMap<>();
          param.put("type", type);
          param.put("searchKey", searchKey);

          int totalCount = userManageService.getUserListCount(param);
          PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
          param.put("itemStartPosition", paging.getItemStartPosition());
          param.put("pagePerSize", paging.getPagePerSize());
          mav.addObject("p", param);
          mav.addObject("userList", userManageService.getUserList(param));
          mav.addObject("paging", paging);
*/
          return mav;
      }

      @GetMapping("/payment/view")
      public ModelAndView userpaymentView(
      		@RequestParam(value="id", defaultValue="") String id,
      		@RequestParam HashMap<String, Object> param){
          ModelAndView mav = new ModelAndView();
          mav.setViewName("manage/sub/user/payment/view");

          UserCustomerPayment uPay = userManageService.selectOneUserCustomerPayment(param);

          String userEmail = uPay.getUserEmail();

          mav.addObject("id", id);
          mav.addObject("user", userManageService.getUserDetail(userEmail));
          //UserCustomerCounsel data = userManageService.getUserCustomerCounsel(param);
          //mav.addObject("counsel", data);

          return mav;
      }

      @GetMapping("/payment/form")
      public ModelAndView userpaymentForm(){
          ModelAndView mav = new ModelAndView();
          mav.setViewName("manage/sub/user/payment/form");

          return mav;
      }

//<!--################################### ajax ###################################-->
    @PostMapping("/list")
    @ResponseBody
    public HashMap<String, Object> user_list_ajax(HttpServletRequest request,
    		@RequestParam HashMap<String, Object> paramMap,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="searchText") String searchText
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
//    		List<SortData> listSort = getListOrder(request);
//    		paramMap.put("listSort", listSort);

    		if(length >= 0) {
				paramMap.put("itemStartPosition", start);
				paramMap.put("pagePerSize", length);
    		}

    		paramMap.put("searchText", searchText);

    		int totalCount = userManageService.getUserListCount(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", userManageService.getUserList(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PutMapping("/account")
    @ResponseBody
    public HashMap<String, Object> userAccountStatus(@RequestParam(value="userEmail",defaultValue="") String userEmail,
                                                     @RequestParam(value="userTokenId",defaultValue="") String userTokenId,
                                                     @RequestParam(value="status",defaultValue="") String accountStatus){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(userEmail.isEmpty() || userTokenId.isEmpty() || accountStatus.isEmpty()){
                throw new Exception("파라미터가 없습니다.");
            }

            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HashMap<String, Object> param = new HashMap<>();
            param.put("userEmail", userEmail);
            param.put("userTokenId", userTokenId);
            param.put("accountStatus", accountStatus);
            userManageService.changeUserAccountStatus(param);

            resultMap.put("result", "success");
            resultMap.put("message", "처리됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/accountSimpleInfo")
    @ResponseBody
    public HashMap<String, Object> userAccountSimpleInfo(
    		@RequestParam(value="userEmail",defaultValue="") String userEmail,
            @RequestParam(value="userTokenId",defaultValue="") String userTokenId,
            @RequestParam(value="userName",defaultValue="") String userName,
            @RequestParam(value="userMobile",defaultValue="") String userMobile,
            @RequestParam(value="userGrade",defaultValue="") String userGrade,
            @RequestParam(value="userMemo",defaultValue="") String userMemo
             ){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(userEmail.isEmpty() || userTokenId.isEmpty() || userName.isEmpty() || userMobile.isEmpty()){
                throw new Exception("파라미터가 없습니다.");
            }

            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HashMap<String, Object> param = new HashMap<>();
            param.put("userEmail",   userEmail);
            param.put("userTokenId", userTokenId);
            param.put("userName",    userName);
            param.put("userMobile",  userMobile);
            param.put("userGrade",  userGrade);
            param.put("userMemo",    userMemo);
            userManageService.changeUserAccountSimpleInfo(param);

            resultMap.put("result", "success");
            resultMap.put("message", "처리됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/create")
    @ResponseBody
    public HashMap<String, Object> userCreate(HttpSession session,
                                              @RequestParam(value="encrypted", defaultValue="true") String encrypted,
                                              @ModelAttribute LoginUser user, BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(user == null
                    || user.getUserEmail() == null
                    || user.getUserPassword() == null
                    || user.getUserName() == null
            ) {
                throw new Exception("필수정보를 확인 해 주세요.");
            }

            if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
            if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
            user.setEncrypt(encrypt);
            user.setIv(iv);
            user.setUserTokenId(String.valueOf(UUID.randomUUID()));
            userManageService.createUser(user, Boolean.parseBoolean(encrypted));

            resultMap.put("result", "success");
            resultMap.put("message", "처리됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @PutMapping("/info")
    @ResponseBody
    public HashMap<String, Object> userInfo(@ModelAttribute LoginUser user, @ModelAttribute UserCustomerInfo customerInfo, BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);

            LoginUser manager = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userManageService.updateUserInfoByManager(user);
            customerInfo.setCreateType("manage/user/view");
            customerInfo.setCreateId(manager.getUserEmail());
            userManageService.writeUserCustomerInfo(customerInfo);

            resultMap.put("result", "success");
            resultMap.put("message", "처리됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }


    @GetMapping("/count")
    @ResponseBody
    public HashMap<String, Object> userCountAjax(
    		@ModelAttribute LoginUser user,
    		BindingResult bindingResult,
    		@Param(value="isOnlyNewJoin") Boolean isOnlyNewJoin){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
        	HashMap<String, Object> param = new HashMap<>();
        	param.put("isOnlyNewJoin", isOnlyNewJoin);

            int totalCount = userManageService.getUserListCount(param);

            resultMap.put("count", totalCount);
            resultMap.put("result", "success");
            resultMap.put("message", "조회완료");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @GetMapping("/counsel-list")
    @ResponseBody
    public HashMap<String, Object> userCounselList(@ModelAttribute LoginUser user, BindingResult bindingResult,
                                                   @RequestParam(value="page", defaultValue="1") int page,
                                                   @RequestParam(value="pageSize", defaultValue="10") int pageSize){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(user.getUserEmail() == null || user.getUserEmail().isEmpty()) throw new Exception("파라미터가 없습니다.");

            int totalCount = userManageService.getUserCustomerCounselListCount(user.getUserEmail());
            PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);

            resultMap.put("paging", paging);
            resultMap.put("list", userManageService.getUserCustomerCounselList(user, paging));

            resultMap.put("result", "success");
            resultMap.put("message", "조회완료");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/counsel-list")
    @ResponseBody
    public HashMap<String, Object> userCounselList_ajax(
    		@ModelAttribute LoginUser user, BindingResult bindingResult,
    		@RequestParam HashMap<String, Object> param,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="pageSize", defaultValue="10") int pageSize){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            //if(user.getUserEmail() == null || user.getUserEmail().isEmpty()) throw new Exception("파라미터가 없습니다.");

            int totalCount = userManageService.getUserCustomerCounselListCount(param);
            PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);

            param.put("itemStartPosition", paging.getItemStartPosition());
            param.put("pagePerSize", paging.getPagePerSize());

            resultMap.put("recordsTotal", totalCount);
            resultMap.put("recordsFiltered", totalCount);
            resultMap.put("data", userManageService.getUserCustomerCounselList(param));

            resultMap.put("result", "success");
            resultMap.put("message", "조회완료");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/counsel")
    @ResponseBody
    public HashMap<String, Object> userCounselWrite(@ModelAttribute UserCustomerCounsel counsel,
                                                    BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(counsel.getUserEmail().isEmpty() || counsel.getRequestText().isEmpty()) throw new Exception("파라미터가 없습니다.");
            LoginUser manager = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            counsel.setCreateId(manager.getUserEmail());
            userManageService.writeUserCustomerCounsel(counsel);

            resultMap.put("result", "success");
            resultMap.put("message", "저장완료");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/counsel-delete")
    @ResponseBody
    public HashMap<String, Object> userCounselDelete(@ModelAttribute UserCustomerCounsel counsel,
                                                    BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(counsel.getId() == null) throw new Exception("파라미터가 없습니다.");
            Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		if(counsel.getCreateId() == null) counsel.setCreateId(counsel.getUserEmail());
	            userManageService.deleteUserCustomerCounsel(counsel);

	            resultMap.put("result", "success");
	            resultMap.put("message", "삭제완료");
	        } else {
	    		resultMap.put("result", "fail");
	    		resultMap.put("message", "로그인 문제가 발생되었습니다.");
	    	}
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/counsel-restore")
    @ResponseBody
    public HashMap<String, Object> userCounselRestore(@ModelAttribute UserCustomerCounsel counsel,
                                                    BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(counsel.getId() == null) throw new Exception("파라미터가 없습니다.");

            Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		if(counsel.getCreateId() == null) counsel.setCreateId(counsel.getUserEmail());
	            userManageService.restoreUserCustomerCounsel(counsel);

	            resultMap.put("result", "success");
	            resultMap.put("message", "복구완료");
        	} else {
        		resultMap.put("result", "fail");
        		resultMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }
  //<!--################################### UserCustomerPayment ###################################-->
    @PostMapping("/payment-list-v")
    @ResponseBody
    public HashMap<String, Object> userCustomerPayment_List_ajax(
    		HttpServletRequest request,
    		@ModelAttribute LoginUser user,
    		@RequestParam HashMap<String, Object> param,
    		@Param(value="reservationId") Integer reservationId,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="pageSize", defaultValue="10") int pageSize,
            BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
/*
            if(reservationId == null || user.getUserEmail() == null || user.getUserEmail().isEmpty())
            	throw new Exception("파라미터가 없습니다.");
*/
        	List<SortData> listSort = getListOrder(request);
        	param.put("listSort", listSort);

            int totalCount = userManageService.selectCountUserCustomerPaymentVirtual(param);
            PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);

            param.put("itemStartPosition", paging.getItemStartPosition());
            param.put("pagePerSize", paging.getPagePerSize());
            if(reservationId != null) param.put("reservationId", reservationId);

            resultMap.put("recordsTotal", totalCount);
            resultMap.put("recordsFiltered", totalCount);
            resultMap.put("data", userManageService.selectListUserCustomerPaymentVirtual(param));
            resultMap.put("dataTotal", userManageService.selectListUserCustomerPaymentVirtualTotal(param));

            resultMap.put("result", "success");
            resultMap.put("message", "조회완료");
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/payment-save")
    @ResponseBody
    public HashMap<String, Object> userCustomerPayment_save_ajax(
    				HttpSession session,
					@RequestParam(value="encrypted", defaultValue="true") String encrypted,
					@ModelAttribute LoginUser user,
					@ModelAttribute UserCustomerPayment pay,
					BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(pay == null
                || pay.getUserEmail() == null
                || pay.getPayDivision() == null
                || pay.getPayComment() == null
                || pay.getPayAmount() == null
            ) {
                throw new Exception("필수정보를 확인 해 주세요.");
            }

            LoginUser manager = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            pay.setCreateId( manager.getUserEmail());

            userManageService.insertUserCustomerPayment(pay);


            resultMap.put("result", "success");
            resultMap.put("message", "처리됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
