package kr.co.wayplus.travel.web.manage;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.InquiryCategory;
import kr.co.wayplus.travel.model.InquiryContent;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.ProductInfo;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.model.ProductPriceOption.DayList;
import kr.co.wayplus.travel.model.ProductPriceOption.FixPriceList;
import kr.co.wayplus.travel.service.manage.InquiryManageService;
import kr.co.wayplus.travel.service.manage.ManageService;

@Controller
@RequestMapping("/manage/inquiry")
public class InquiryManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

	final String addPath = "file/inquiry/";

    @Value("${upload.file.max-size}")
    int maxFileSize;

    private final InquiryManageService svcInquiry;
    private final ManageService svcManage;

    public InquiryManageController(
    		InquiryManageService svcInquiry,
    		ManageService svcManage) {
        this.svcInquiry = svcInquiry;
        this.svcManage  = svcManage;
    }

//	<!--################################### InquiryContent(Inquiry) ###################################-->
    @GetMapping(value={"/inquiry","/reservation"})
    public ModelAndView inquiry_list( HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> paramMap = new HashMap<>();

        String url = request.getRequestURL().toString();
        String strPath = url.substring( url.lastIndexOf("/")+1 );

        switch(strPath) {
	        case  "inquiry" :
	        	mav.addObject("reservationYn","N");
	        	paramMap.put("reservationYn","N");
	        	break;
	        case  "reservation" :
	        	mav.addObject("reservationYn","Y");
	        	paramMap.put("reservationYn","Y");
	        	break;
        }

        paramMap.put("useYn","Y");
        paramMap.put("deleteYn","N");
        paramMap.put("sort","orderNum");
        paramMap.put("sortOrder","ASC");

        mav.addObject("listCategory",  svcInquiry.selectListInquiryCategory( paramMap ));
        mav.setViewName("manage/sub/inquiry/list");
        return mav;
    }

    @GetMapping(value={"/inquiry/form","/reservation/form"})
    public ModelAndView inquiry_view(
    		 HttpServletRequest request,
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		@RequestParam(value="id", defaultValue="0") String id){

        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> paramMap = new HashMap<>();

        String url = request.getRequestURL().toString();
        int pos1 = url.lastIndexOf("/");
        int pos2 = url.lastIndexOf("/", pos1-1);

        String strPath = url.substring( pos2+1, pos1 );

        switch(strPath) {
	        case  "inquiry" :
	        	mav.addObject("reservationYn","N");
	        	break;
	        case  "reservation" :
	        	mav.addObject("reservationYn","Y");
	        	break;
        }

        paramMap.put("useYn","Y");
        paramMap.put("deleteYn","N");
        paramMap.put("sort","orderNum");
        paramMap.put("sortOrder","ASC");

        paramMap.put("reservationYn","N");
        mav.addObject("listCategoryInquiry",  svcInquiry.selectListInquiryCategory( paramMap ));
        paramMap.put("reservationYn","Y");
        mav.addObject("listCategoryReservation",  svcInquiry.selectListInquiryCategory( paramMap ));


        paramMap.clear();

        if(mode.equals("U")) {
        	paramMap.put("id", id);
        	InquiryContent data = svcInquiry.selectOneInquiryContent(paramMap);
        	mav.addObject("inquiry",  data);

        	HashMap<String, Object> paramProduct = new HashMap<>();

        	if(data.getProductSerial() != null) {
        		paramProduct.put("productSerial", data.getProductSerial());

        		ProductInfo product = svcManage.selectProductInfo(paramProduct);
        		paramProduct.put("productTourId", product.getProductTourId());

	    		ArrayList<FixPriceList> optionPriceListFix = svcManage.selectListProductFixPrice( paramProduct );//상품 고정가격 옵션
	    		ArrayList<DayList> optionPriceListDay      = svcManage.selectListProductDayPrice( paramProduct );//상품 일자별 가격옵션

	    		mav.addObject("product",  product);
	    		mav.addObject("optionPriceListFix", optionPriceListFix);
	    		mav.addObject("optionPriceListDay", optionPriceListDay);
        	} else {
        		mav.addObject("product",  null);
	    		mav.addObject("optionPriceListFix", null);
	    		mav.addObject("optionPriceListDay", null);
        	}

        	HashMap<String, Object> paramMap2 = new HashMap<>();
        	paramMap2.put("id", data.getCategoryId());
        	mav.addObject("category",  svcInquiry.selectOneInquiryCategory(paramMap2));
        	mav.addObject("p", paramMap);
        } else {
        	mav.addObject("inquiry",  new InquiryContent());
        	mav.addObject("category",  new InquiryCategory());
        }
        mav.addObject("mode", mode);
        mav.setViewName("manage/sub/inquiry/form");
        return mav;
    }

//<!--################################### Inquirycategory ###################################-->
    @GetMapping("/category")
    public ModelAndView category_view(){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("manage/sub/inquiry/category");
        return mav;
    }
  //<!--################################### ajax ###################################-->
    @GetMapping("/calendar-count")
    @ResponseBody
    public HashMap<String, Object> inquiry_calendar_count_ajax(
    		HttpServletRequest request,
    		InquiryContent ic,
    		@RequestParam HashMap<String, Object> paramMap ){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		ArrayList<HashMap<String, Object> > list = svcInquiry.selectListCountInquiryContentByCalendar(paramMap);

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
    		InquiryContent ic,
    		@RequestParam HashMap<String, Object> paramMap ){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		ArrayList<HashMap<String, Object> > list = svcInquiry.selectListInquiryContentByCheckList(paramMap);

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
    public HashMap<String, Object> inquiry_count_status_type_ajax(
    		HttpServletRequest request,
    		InquiryContent ic,
    		@Param(value="listCategoryId[]") int[] listCategoryId ){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

    		paramMap.put("categoryId",   ic.getCategoryId());
    		paramMap.put("listCategoryId",   listCategoryId);

    		paramMap.put("reservationYn",ic.getReservationYn());
    		paramMap.put("cancelYn",     ic.getCancelYn());

    		retrunMap.put("data", svcInquiry.selectListInquiryCountStatusType(paramMap) );

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/contents-list")
    @ResponseBody
    public HashMap<String, Object> inquiry_list_ajax(HttpServletRequest request, InquiryContent ic,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="categoryId") String categoryId,
    		@Param(value="userEmail") String userEmail,
    		@Param(value="applyCode") String applyCode,
    		@Param(value="reservationCode") String reservationCode,
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

    		paramMap.put("reservationYn",ic.getReservationYn() );

    		paramMap.put("userEmail", userEmail);
    		paramMap.put("applyCode", applyCode);
    		paramMap.put("reservationCode", reservationCode);
    		paramMap.put("cancelCode", cancelCode);
			paramMap.put("categoryId", categoryId);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);
			paramMap.put("sort", "createDate");
			paramMap.put("sortOrder", "desc");

    		int totalCount = svcInquiry.selectCountInquiryContent(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", svcInquiry.selectListInquiryContent(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/contents-save")
    @ResponseBody
    public HashMap<String, Object> inquiry_save_ajax(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		InquiryContent data,
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
	    			svcInquiry.insertInquiryContent(data);
	    		} else {
	    			if(data.getLastUpdateId() == null) data.setLastUpdateId(manager.getUserEmail());
	    			svcInquiry.updateInquiryContent(data);
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



    @PostMapping("/contents-delete")
    @ResponseBody
    public HashMap<String, Object> inquiry_delete_ajax(
    		InquiryContent data
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		if(data.getId() == null) throw new Exception("파라미터가 없습니다.");

    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser manager = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		if(data.getCreateId() == null) data.setCreateId(manager.getUserEmail());
	    		svcInquiry.deleteInquiryContent(data);

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

    @PostMapping("/contents-restore")
    @ResponseBody
    public HashMap<String, Object> inquiry_restore_ajax(
    		InquiryContent data
    	){

    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		if(data.getId() == null) throw new Exception("파라미터가 없습니다.");

    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser manager = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		if(data.getCreateId() == null) data.setCreateId(manager.getUserEmail());
	    		svcInquiry.restoreInquiryContent(data);

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

    @GetMapping("/category-list")
    @ResponseBody
    public HashMap<String, Object> inquiry_category_list_ajax(HttpServletRequest request, InquiryCategory ic,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="categoryId") String categoryId,
    		@Param(value="applyCode") String applyCode,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike,
    		@Param(value="sort") String sort,
    		@Param(value="sortOrder") String sortOrder
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

			paramMap.put("categoryId", categoryId);
			paramMap.put("applyCode", applyCode);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

			if(sort != null && sortOrder != null) {
	    		paramMap.put("sort",sort);
	    		paramMap.put("sortOrder",sortOrder);
			}

			if(ic.getReservationYn() != null) 		paramMap.put("reservationYn", ic.getReservationYn());
			if(ic.getReservationSwitchYn() != null) paramMap.put("reservationSwitchYn", ic.getReservationSwitchYn());

    		int totalCount = svcInquiry.selectCountInquiryCategory(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", svcInquiry.selectListInquiryCategory(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/category-save")
    @ResponseBody
    public HashMap<String, Object> inquiry_category_save_ajax(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		InquiryCategory ic,
    		HttpServletRequest request
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		String thumnailUrl = "";

	    		if(mode.equals("I")) {
	    			svcInquiry.insertInquiryCategory(ic);
	    		} else {
	    			svcInquiry.updateInquiryCategory(ic);
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

    @PostMapping("/category-order-save")
    @ResponseBody
    public HashMap<String,Object> inquiry_category_order_save_ajax(HttpServletRequest request ){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		String total = request.getParameter("total");

    		for (int i = 0; i < Integer.parseInt(total); i++) {
    			String id 		= request.getParameter("order["+i+"][menuId]");
    			String orderNum 	= request.getParameter("order["+i+"][menuSort]");

    			Integer _id 	  = Integer.parseInt( id );
    			Integer _orderNum = Integer.parseInt( orderNum );

    			InquiryCategory _menu = new InquiryCategory().addId( _id ).addOrderNum( _orderNum );

    			svcInquiry.updateInquiryCategory(_menu);
			}

    		retMap.put("result","success");
    		retMap.put("message","처리 성공 하였습니다.");

		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getCause().getMessage());
		}

        return retMap;
    }
    @PostMapping("/category-del")
    @ResponseBody
    public HashMap<String,Object> inquiry_category_del_ajax(InquiryCategory ic){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		svcInquiry.deleteInquiryCategory(ic);

    		retMap.put("result","success");
    		retMap.put("message","처리 성공 하였습니다.");

		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getCause().getMessage());
		}

        return retMap;
    }

    @PostMapping("/category-restore")
    @ResponseBody
    public HashMap<String,Object> inquiry_category_restore_ajax(InquiryCategory ic){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		svcInquiry.restoreInquiryCategory(ic);

    		retMap.put("result","success");
    		retMap.put("message","처리 성공 하였습니다.");

		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getCause().getMessage());
		}

        return retMap;
    }

}
