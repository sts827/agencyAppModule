package kr.co.wayplus.travel.web.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.InquiryContent;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.ProductInfo;
import kr.co.wayplus.travel.model.Reservation;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.model.UserCustomerPayment;
import kr.co.wayplus.travel.service.manage.ManageService;
import kr.co.wayplus.travel.service.manage.ReservationManageService;
import kr.co.wayplus.travel.service.manage.UserManageService;

@Controller
@RequestMapping("/manage/reservation")
public class ReservationManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

	final String addPath = "file/reservation/";

	@Value("${key.crypto.encrypt}")
    private String encrypt;
    @Value("${key.crypto.iv}")
    private String iv;

    @Value("${upload.file.max-size}")
    int maxFileSize;

    private final ReservationManageService svcReservation;
    private final ManageService svcManage;
    private final UserManageService svcUser;

    public ReservationManageController(
    		ReservationManageService svcReservation,
    		ManageService svcManage,
    		UserManageService userManageService) {
        this.svcReservation = svcReservation;
        this.svcManage  = svcManage;
        this.svcUser  = userManageService;
    }

//	<!--################################### Reservation ###################################-->
    @GetMapping("/list")
    public ModelAndView list( HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> paramMap = new HashMap<>();

        String url = request.getRequestURL().toString();
        String strPath = url.substring( url.lastIndexOf("/")+1 );

        mav.setViewName("manage/sub/reservation/list");
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView form(
    		 HttpServletRequest request,
    		@RequestParam(value="mode", defaultValue="V") String mode,
    		@RequestParam(value="id", defaultValue="0") String id){

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> paramMap = new HashMap<>();

        if(mode.equals("U")) {
        	paramMap.put("id", id);
        	Reservation data = svcReservation.selectOneReservation(paramMap);

        	mav.addObject("reservation",  data);

        	paramMap.clear();
        	paramMap.put("userEmail",data.getCreateId());

        	LoginUser user = svcUser.getUserDetail(paramMap);
        	if(user != null) {
        		mav.addObject("user", user);
        	} else {
        		user = new LoginUser().addGuestUser(data.getUserEmail());

        		mav.addObject("user", user);
        	}

        	if(data.getProductSerial() != null && data.getProductTourId() != null) {
        		paramMap.put( "productSerial", data.getProductSerial() );
        		paramMap.put( "productTourId", data.getProductTourId() );
        		mav.addObject("product", svcManage.selectProductInfo(paramMap));
        	} else {
        		mav.addObject("product",      new ProductInfo());
        	}

        } else {
        	mav.addObject("reservation",  new Reservation());
        	mav.addObject("user",         new LoginUser());
        	mav.addObject("product",      new ProductInfo());
        }
        mav.addObject("mode", mode);
        mav.setViewName("manage/sub/reservation/form");
        return mav;
    }

  //<!--################################### ajax ###################################-->
    @GetMapping("/calendar-count")
    @ResponseBody
    public HashMap<String, Object> inquiry_calendar_count_ajax(
    		HttpServletRequest request,
    		Reservation data,
    		@RequestParam HashMap<String, Object> paramMap ){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		ArrayList<HashMap<String, Object> > list = svcReservation.selectListCountReservationContentByCalendar(paramMap);

    		retrunMap.put("data",  list);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @GetMapping("/calendar-check-list")
    @ResponseBody
    public HashMap<String, Object> inquiry_calendar_check_list_ajax(
    		HttpServletRequest request,
    		Reservation data,
    		@RequestParam HashMap<String, Object> paramMap ){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		ArrayList<HashMap<String, Object> > list = svcReservation.selectListReservationContentByCheckList(paramMap);

    		retrunMap.put("data",  list);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @GetMapping("/status-count")
    @ResponseBody
    public HashMap<String, Object> reservation_count_status_type_ajax(
    		HttpServletRequest request,
    		Reservation data ){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();
    		paramMap.put("cancelYn",     data.getCancelYn());

    		retrunMap.put("data", svcReservation.selectListReservationCountStatusType(paramMap) );

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/list")
    @ResponseBody
    public HashMap<String, Object> reservation_list_ajax(
    		HttpServletRequest request,
    		Reservation ic,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,

    		@Param(value="dateType") String dateType,
    		@Param(value="dateFrom") String dateFrom,
    		@Param(value="dateTo") String dateTo,
    		@Param(value="reservationCode") String reservationCode,
    		@Param(value="searchKey") String searchKey,

    		@Param(value="userEmail") String userEmail,
    		@Param(value="categoryId") String categoryId,
    		@Param(value="applyCode") String applyCode,
    		@Param(value="cancelCode") String cancelCode,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike
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


    		paramMap.put("dateType", dateType);
    		paramMap.put("dateFrom", dateFrom);
    		paramMap.put("dateTo",   dateTo + " 23:59:59");
    		paramMap.put("reservationCode", reservationCode);
    		paramMap.put("searchKey", searchKey);

    		paramMap.put("userEmail", userEmail);

    		paramMap.put("applyCode", applyCode);

    		paramMap.put("cancelCode", cancelCode);
			paramMap.put("categoryId", categoryId);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

    		int totalCount = svcReservation.selectCountReservation(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", svcReservation.selectListReservation(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/save")
    @ResponseBody
    public HashMap<String, Object> inquiry_save_ajax(
    		HttpSession session,
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		@RequestParam(value="isGuest", defaultValue="false") boolean isGuest,
    		Reservation data,
    		//Multipart
    		HttpServletRequest request
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser manager = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		String thumnailUrl = "";

	    		if(mode.equals("I")) {
	    			if(data.getCreateId() == null) data.setCreateId(manager.getUserEmail());

	    			if(isGuest) {
	    				String uuidEmail = String.valueOf(UUID.randomUUID());

	    				data.setUserEmail( uuidEmail );
	    				data.setCreateId( uuidEmail );

	    				if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
	    				if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");

	    				LoginUser userGuest = new LoginUser()
    						.addGuestUser( uuidEmail )
	    					.addUserName( data.getUserName())
	    					.addUserJoinType( "reservation" )
	    					.addEncrypt( encrypt )
	    					.addIv( iv )
	    					.addUserPassworad("Guest");
	    				;

    		            svcUser.createUser(userGuest, false);
	    			}

	    			svcReservation.insertReservation(data);

	    			if(data.getTotalAmount() != null) {
	    				UserCustomerPayment payData = new UserCustomerPayment()
	    					.addReservationId(data.getId())
	    					.addUserEmail(data.getUserEmail())
	    					.addPayAmount(data.getTotalAmount())
	    					.addPayDivision("G")
	    					.addPayComment("최초 결제금액 등록")
	    					.addCreateId(data.getCreateId())
	    				;

	    				svcUser.insertUserCustomerPayment(payData);
	    			}
	    		} else {
	    			svcReservation.updateReservation(data);
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


    @PostMapping("/delete")
    @ResponseBody
    public HashMap<String, Object> inquiry_delete_ajax(
    		Reservation data
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		if(data.getId() == null) throw new Exception("파라미터가 없습니다.");

    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser manager = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		if(data.getCreateId() == null) data.setCreateId(manager.getUserEmail());

	    		svcReservation.deleteReservation(data);

	    		retrunMap.put("result", "success");
	    		retrunMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retrunMap.put("result", "fail");
	    		retrunMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }

    @PostMapping("/restore")
    @ResponseBody
    public HashMap<String, Object> inquiry_restore_ajax(
    		Reservation data
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		if(data.getId() == null) throw new Exception("파라미터가 없습니다.");

    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser manager = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		if(data.getCreateId() == null) data.setCreateId(manager.getUserEmail());

	    		svcReservation.restoreReservation(data);

	    		retrunMap.put("result", "success");
	    		retrunMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retrunMap.put("result", "fail");
	    		retrunMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
    	} catch (Exception e) {
    		retrunMap.put("result", "fail");
    		retrunMap.put("message", "처리중 문제가 발생했습니다.");
    	}


    	return retrunMap;
    }
}
