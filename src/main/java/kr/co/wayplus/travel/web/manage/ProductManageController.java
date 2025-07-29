package kr.co.wayplus.travel.web.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.model.BoardAttachFile;
import kr.co.wayplus.travel.model.BoardContents;
import kr.co.wayplus.travel.model.BoardSetting;
import kr.co.wayplus.travel.model.CodeInfo;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.MenuUser;
import kr.co.wayplus.travel.model.PagingDTO;
import kr.co.wayplus.travel.model.ProductCategory;
import kr.co.wayplus.travel.model.ProductDetailSchedule;
import kr.co.wayplus.travel.model.ProductDetailScheduleImage;
import kr.co.wayplus.travel.model.ProductInfo;
import kr.co.wayplus.travel.model.ProductInventory;
import kr.co.wayplus.travel.model.ProductJsonWrapper;
import kr.co.wayplus.travel.model.ProductPostJson;
import kr.co.wayplus.travel.model.ProductPriceOption;
import kr.co.wayplus.travel.model.ProductTemplate;
import kr.co.wayplus.travel.model.ProductTemplateFile;
import kr.co.wayplus.travel.model.ProductTourImages;
import kr.co.wayplus.travel.model.ProductTourLink;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.service.manage.ManageService;
import kr.co.wayplus.travel.service.manage.MenuManageService;
import kr.co.wayplus.travel.util.FileInfoUtil;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String tourTemplateUrl = "/manage/sub";

    final String addPath = "product/";

    @Value("${upload.file.path}")
    private String imageUploadPath;

    @Value("${upload.file.max-size}")
    int maxFileSize;

    private ManageService service;

    private final MenuManageService menuManageService;

    @Autowired
    private ProductManageController(ManageService svc, MenuManageService menuManageService) {
        this.service = svc;
        this.menuManageService = menuManageService;
    }

    @PostMapping("/all-list")
    @ResponseBody
    public HashMap<String,Object> allProductList(
    		@RequestParam(value="page",defaultValue = "1")int page,
            @RequestParam(value="pageSize",defaultValue = "10")int pageSize,
            @Param(value="isNotMe") String isNotMe ){
    	HashMap<String,Object> resultMap = new HashMap<>();

        try {
        	HashMap<String, Object> paramMap = new HashMap<>();

        	paramMap.put("isNotMe", isNotMe);
        	paramMap.put("regacyYn", "N");

        	int totalCount = service.getProductListCount(paramMap);
            PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
            //paramMap.put("itemStartPosition", paging.getItemStartPosition());
            //paramMap.put("pagePerSize", paging.getPagePerSize());

        	List<ProductInfo> productList = service.selectProductList(paramMap);

        	resultMap.put("totalCount", totalCount);
        	resultMap.put("paging", paging);
        	resultMap.put("data", productList);

            resultMap.put("result","success");
            resultMap.put("message","처리되었습니다.");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }


    /*################################### 상품 목록화면 #####################################*/
    @GetMapping("/{typeCode}")
    public ModelAndView productListPage(
                                        @PathVariable(value = "typeCode") String typeCode,
                                        @RequestParam(value="page",defaultValue = "1")int page,
                                        @RequestParam(value="pageSize",defaultValue = "5")int pageSize,
                                        @RequestParam(value="productStatus", defaultValue="") String productStatus,
                                        @RequestParam(value="searchType",defaultValue = "") String searchType,
                                        @RequestParam(value="searchKey", defaultValue="") String searchKey,
                                        @RequestParam(value="productMenuId",defaultValue = "")String productMenuId,
                                        @RequestParam(value="productCategoryId",defaultValue = "")String productCategoryId){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> paramMap = new HashMap<>();

        //메뉴 정보 불러오기
        String manageUrl = "/" + typeCode;

        MenuUser menu = service.selectManageMenuByUrl(manageUrl);

        mav.addObject("menuName",menu.getMenuName());
        String menuTitle = menu.getMenuName();
        String menuType=menu.getMenuType();
        String menuSubType =menu.getMenuSubType();

        int menu_id=menu.getMenuId().intValue();

        // category 불러오기(2depth) : meneuUser
        ArrayList<MenuUser> menuCategory = service.getManageSubMenuListById(menu_id);

        paramMap.put("menuId",menu_id);
        mav.addObject("menuId",menu_id);
        mav.addObject("categoryList",menuCategory);

        // categroy 불러오기(3depth) : productCommonCategroy
        if(!productMenuId.equals("")){
            ArrayList<ProductCategory> subCategories = service.getSubCategoryListByMenuId(Integer.parseInt(productMenuId));
            mav.addObject("subCategoryList",subCategories);
        }
        paramMap.put("searchType",searchType);

        // 상품정보 불러오기
        if(menuType.equals("product")){
            paramMap.put("menuTitle",menuTitle);

            paramMap.put("deleteYn","N");
            paramMap.put("productMenuId",productMenuId);
            paramMap.put("productCategoryId",productCategoryId);
            paramMap.put("searchKey",searchKey);
            paramMap.put("productStatus",productStatus.substring(0));

            // 상품 개수
            int totalCount = service.getProductListCount(paramMap);
            PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
            paramMap.put("itemStartPosition", paging.getItemStartPosition());
            paramMap.put("pagePerSize", paging.getPagePerSize());
            // 상품 리스트
            List<ProductInfo> productList = service.selectProductList(paramMap);

            //상품가격
            for ( ProductInfo productInfo : productList ) {
                String productPrice = "가격정보없음";
                HashMap<String, Object> productParamMap = new HashMap<>();
                int productTourId = productInfo.getProductTourId();
                productParamMap.put("productTourId", productTourId);

                ProductPriceOption.FixPriceList fixPriceList = service.selectOneProductFixPriceList(productParamMap);
                ProductPriceOption.DayList dayPriceList = service.selectOneProductDayPriceList(productParamMap);
                if ( fixPriceList != null ) {
                    //1. 고정가격이 있는지 체크
                    productPrice = fixPriceList.getProductPrice();
                }
                else if ( dayPriceList != null) {
                    //2. 고정가격이 없고 요일별 있는지 체크
                    productPrice = dayPriceList.getProductPrice();
                }

                productInfo.setProductPrice( String.valueOf( productPrice ) );
            }

            // model 담기
            mav.addObject("totalCount", totalCount);
            mav.addObject("paging", paging);
            mav.addObject("productList",productList);

            if(menuSubType.equals("package")){
            }
            else if(menuSubType.equals("single")){
            }else if(menuSubType.equals("berth")){
            }else if(menuSubType.equals("golf")){
            }else if(menuSubType.equals("place")){
            }else if(menuSubType.equals("link")){

            }

            mav.addObject("typeCode", typeCode);
            mav.addObject("p",paramMap);

            // resource-template
            //mav.setViewName(tourTemplateUrl+"/"+menuType+"/"+menuSubType+"/"+"product-list");

            mav.setViewName(tourTemplateUrl+"/"+menuType+"/"+"product-list");
        }

        return mav;
    }



    /*################################### 상품 관리 #####################################*/
    // 상품 등록 페이지
    @GetMapping("/{typeCode}/registration")
    public ModelAndView productAddPage(
    		@PathVariable("typeCode") String typeCode,
    		@RequestParam(value="isnew",defaultValue = "false") boolean isnew) {
        ModelAndView mav = new ModelAndView();
        HashMap<String,Object> paramMap = new HashMap<>();
        try{
            //메뉴 정보 불러오기
            String manageUrl = "/" + typeCode;

            MenuUser menu = service.selectManageMenuByUrl(manageUrl);
            //메뉴정보
            ArrayList<MenuUser> menuList = menuManageService.selectListMenuUser(paramMap);
            int menu_id=menu.getMenuId().intValue();
            if ( menuList.size() > 0) {
                mav.addObject("listUrl", "/"+ menuList.get(0).getMenuType() +"/"+typeCode);
            }
            String menuType=menu.getMenuType();
            String menuSubType =menu.getMenuSubType();

            // menuCategory 불러오기
            ArrayList<MenuUser> menuCategory = service.getManageSubMenuListById(menu_id);
            paramMap.put("menuCategory",menuCategory);
            mav.addObject("policyInventory", false);

            mav.addObject("menu",menu);
            mav.addObject("menuId",menu.getMenuId());
            mav.addObject("menuName",menu.getMenuName());
            mav.addObject("categoryList",menuCategory);
			mav.addObject("mode","I");
			
            if(menuType.equals("product")){
                if(menuSubType.equals("package")) {
                }else if(menuSubType.equals("single")){
                }else if(menuSubType.equals("berth")){
                }else if(menuSubType.equals("golf")){
                }else if(menuSubType.equals("rentcar")){
                    mav.addObject("rentCarOptionList", service.selectRentCarOptionList());
                }
                else if(menuSubType.equals("place")){
                    // 장소
                }

                paramMap.put("productTourId", paramMap.get("upperMenuId"));
                //기본 가격 제공용
                List<ProductDetailSchedule> detailScheduleList = service.selectProductDetailScheduleList(paramMap);
                mav.addObject("dayBtnInfoList", service.selectProductDetailScheduleBtnList(paramMap));
                for (ProductDetailSchedule productDetailSchedule : detailScheduleList) {
                    productDetailSchedule.setImageNumList(service.selectProductDetailScheduleImageList(productDetailSchedule));
                }
                mav.addObject("detailScheduleList", detailScheduleList);

                if ( typeCode.equals("domestic") || typeCode.equals("overseas") ) {
                    paramMap.put("option_type", "package");
                }
                else {
                    paramMap.put("option_type", typeCode.toUpperCase());
                }

                MenuUser menuModel = menuManageService.selectOneMenuUser( "/"+typeCode );
                paramMap.put("option_type", menuModel.getMenuSubType());

                List<ProductPriceOption.DayList> fixPriceOptionList = service.selectPriceOptionList(paramMap);
                mav.addObject("fixPriceOptionList", fixPriceOptionList);

                //logger.info(tourTemplateUrl+"/"+ menuType +"/"+menuSubType+"/"+"product-add");

                mav.addObject("productType", menuSubType);

            	mav.addObject("productId", null); 					//상품시퀀스
				mav.addObject("productInfo", new ProductInfo()); 	//상품모델(빈껍데기)
				mav.addObject("productImageList", null);			//상품이미지
				mav.addObject("addBasicOptionId", null);
				mav.addObject("priceSelectValue", "fix");

                if( isnew ) {
                	mav.setViewName(tourTemplateUrl+"/"+ menuType +"/"+"product-form");
                } else {
                	mav.setViewName(tourTemplateUrl+"/"+ menuType +"/"+menuSubType+"/"+"product-add");
                }
            }

        }catch(Exception e){
            logger.error(e.getMessage());
        }

        return mav;
    }

    //상품 수정 페이지
    // TODO.. 추후 productNum이 아닌 serialNumber로 바꿀것
    @GetMapping("/{typeCode}/modify/{productNum}")
    public ModelAndView productModify(
			@PathVariable String typeCode,
			@PathVariable int productNum,
    		@RequestParam(value="isnew",defaultValue = "false") boolean isnew){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> paramMap = new HashMap<>();
        //상품 기본정보
        paramMap.put("productTourId", productNum);
        ProductInfo productInfo = service.selectProductInfo(paramMap);
        mav.addObject("productInfo", productInfo);

        mav.addObject("policyInventory", productInfo.getPolicyInventory().equals("1"));

//        ProductTemplate productTemplate = service.selectProductTemplateById(paramMap);
//        mav.addObject("productTemplate",productTemplate);

        //상품 이미지
        if ( productInfo.getProductImages() != null ) {
            String[] images = productInfo.getProductImages().split(",");
            if ( images.length > 0 ) {
                paramMap.put("images", images);
                List<ProductTourImages> productImageList = service.selectProductInfoImageList(paramMap);
                mav.addObject("productImageList", productImageList);
            }
        }

        //메뉴정보
        paramMap.put("menuId",productInfo.getProductMenuId());
        ArrayList<MenuUser> menuList = menuManageService.selectListMenuUser(paramMap);
        if ( menuList.size() > 0) {
            mav.addObject("listUrl", "/"+ menuList.get(0).getMenuType() +"/"+typeCode);
        }

        //메뉴 정보 불러오기
        String manageUrl = "/" + typeCode;
        MenuUser menu = service.selectManageMenuByUrl(manageUrl);
        mav.addObject("menu",menu);
        mav.addObject("menuId",productInfo.getProductMenuId());
        mav.addObject("menuName",menu.getMenuName());

        String menuType=menu.getMenuType();
        String menuSubType =menu.getMenuSubType();

        int menu_id=menu.getMenuId().intValue();
        // menuCategory 불러오기
        ArrayList<MenuUser> menuCategory = service.getManageSubMenuListById(menu_id);
        paramMap.put("menuCategory",menuCategory);
        mav.addObject("categoryList",menuCategory);
        // typeCode
        paramMap.put("typeCode",typeCode);

        //판매가격설정용
        String optionGroupCode = service.selectProductGroupCode(paramMap);
        mav.addObject("optionGroupCode", optionGroupCode);
        //판매가격설정용 - 기본가격
        List<String> addBasicOptionId = new ArrayList<>();
        paramMap.put("option_type", typeCode.toUpperCase());
        List<ProductPriceOption.FixPriceList> oneProductFixPriceOption = service.selectOneProductFixPriceOption(paramMap);

        if ( oneProductFixPriceOption.size() > 0 ) {
            mav.addObject("fixPriceOptionList", oneProductFixPriceOption);
        } else {
            MenuUser menuModel = menuManageService.selectOneMenuUser( "/"+typeCode );
            paramMap.put("option_type", menuModel.getMenuSubType());

            List<ProductPriceOption.DayList> fixPriceOptionList = service.selectPriceOptionList(paramMap);
            mav.addObject("fixPriceOptionList", fixPriceOptionList);
        }

        String priceSelectValue = "fix";
        /**등록된 가격들의 부모 기본가격id 목록과 가격타입**/
        List<ProductPriceOption> oneProductPriceOptionList = service.selectOneProductPriceOptionList(paramMap);
        if ( oneProductPriceOptionList.size() > 0 ) {
            priceSelectValue = "fix";
            for (ProductPriceOption oneProductPriceOption : oneProductPriceOptionList) {
                addBasicOptionId.add(String.valueOf(oneProductPriceOption.getPriceOptionId()));
            }
        }

        List<ProductPriceOption> oneProductDayPriceOptionList = service.selectOneProductDayPriceOptionList(paramMap);
        if ( oneProductDayPriceOptionList.size() > 0 ) {
            priceSelectValue = "calender";
            for (ProductPriceOption oneProductDayPriceOption : oneProductDayPriceOptionList) {
                addBasicOptionId.add(String.valueOf(oneProductDayPriceOption.getPriceOptionId()));
            }
        }

        if ( addBasicOptionId.size() > 0 ) {
            mav.addObject("addBasicOptionId", addBasicOptionId);
        }
        mav.addObject("priceSelectValue", priceSelectValue);
        /**등록된 가격들의 부모 기본가격id 목록과 가격타입**/

        //세부일정용
        List<ProductDetailSchedule> detailScheduleList = service.selectProductDetailScheduleList(paramMap);
        mav.addObject("dayBtnInfoList", service.selectProductDetailScheduleBtnList(paramMap));
        for (ProductDetailSchedule productDetailSchedule : detailScheduleList) {
            productDetailSchedule.setImageNumList(service.selectProductDetailScheduleImageList(productDetailSchedule));
        }
        //상품연계정보
        HashMap<String, Object> Param = new HashMap<>();
        Param.put("productSerial", productInfo.getProductSerial() );
        List<ProductTourLink> ProductTourLinkList = service.selectListProductTourLink(Param);
        mav.addObject("productTourLinkList", ProductTourLinkList);

        if(menuSubType.equals("package")) {}
        else if(menuSubType.equals("single")){}
        else if(menuSubType.equals("berth")){}
        else if(menuSubType.equals("golf")){
            //골프 옵션 정보용
            //product_tour_detail 테이블에서 detail_category가 'golfOption'인 것들을 가져온다.
            HashMap<String, Object> golfParam = new HashMap<>();
            golfParam.put("productTourId", productNum);
            golfParam.put("detailCategory", "golfOption");
            List<ProductDetailSchedule> productDetailList = service.selectProductDetailList(golfParam);
            mav.addObject("productGolfDetailList", productDetailList);
            for (ProductDetailSchedule productDetailSchedule : productDetailList) {
                productDetailSchedule.setImageNumList(service.selectProductDetailScheduleImageList(productDetailSchedule));
            }
        }
        else if(menuSubType.equals("rentcar")){
            mav.addObject("rentCarOptionList", service.selectRentCarOptionList());
        }
        else if(menuSubType.equals("stay")){
            //객실 옵션 정보용
            //product_tour_detail 테이블에서 detail_category가 'stayOption'인 것들을 가져온다.
            HashMap<String, Object> golfParam = new HashMap<>();
            golfParam.put("productTourId", productNum);
            golfParam.put("detailCategory", "stayOption");
            List<ProductDetailSchedule> productDetailList = service.selectProductDetailList(golfParam);
            mav.addObject("productStayDetailList", productDetailList);
            for (ProductDetailSchedule productDetailSchedule : productDetailList) {
                productDetailSchedule.setImageNumList(service.selectProductDetailScheduleImageList(productDetailSchedule));
            }
        }

        mav.addObject("productInfo", productInfo);
        mav.addObject("menuName",menu.getMenuName());
        mav.addObject("categoryList",menuCategory);
        mav.addObject("optionGroupCode", optionGroupCode);

        mav.addObject("detailScheduleList", detailScheduleList);
        mav.addObject("p", paramMap);
        mav.addObject("productId", productNum);
        mav.addObject("typeCode", typeCode);
        mav.addObject("productType", menuSubType);
        mav.addObject("mode","U");

        //mav.setViewName(tourTemplateUrl+"/"+ menuType +"/"+menuSubType+"/"+"product-modify");
        
        if( isnew ) {
        	mav.setViewName(tourTemplateUrl+"/"+ menuType +"/"+"product-form");
        } else {
        	mav.setViewName(tourTemplateUrl+"/"+ menuType +"/"+menuSubType+"/"+"product-modify");
       }

        

        return mav;
    }


    /*###################################  #####################################*/

    // 메뉴 카테고리
    @GetMapping("/getProductCategory")
    @ResponseBody
    public HashMap<String,Object> getProductCategoryById(
    	//	@RequestParam("categoryId") int categoryId,
    		@RequestParam HashMap<String, Object> param,
    		@ModelAttribute ProductCategory data){
        HashMap<String,Object> resultMap = new HashMap<>();
        try{
        	ArrayList<ProductCategory> categories = service.selectListProductCategory(param);
            //ArrayList<ProductCategory> categories = service.getProductCategoriesById(categoryId);
            resultMap.put("result","success");
            resultMap.put("categories",categories);
            resultMap.put("message","처리 성공");
        }catch (Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();

        }

        return resultMap;
    }


    @PostMapping("/category-save")
    @ResponseBody
    public HashMap<String, Object> category_save_ajax(
    		@RequestParam HashMap<String, Object> paramMap,
    		@ModelAttribute ProductCategory pc) {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        try {

        	Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		pc.setCreateId( user.getUserEmail() );
	    		service.saveProductCommonCategory(pc);

	    		resultMap.put("result", "success");
	    		resultMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		resultMap.put("result", "fail");
	    		resultMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/category-order-save")
    @ResponseBody
    public HashMap<String,Object> category_order_save(HttpServletRequest request ){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
//    		ArrayList<MenuUser> listMenu = new ArrayList<MenuUser>();

    		String total = request.getParameter("total");

    		String _productMenuId = request.getParameter("productMenuId");

    		Integer productMenuId = Integer.parseInt( _productMenuId );

    		for (int i = 0; i < Integer.parseInt(total); i++) {

    			String menuId 		= request.getParameter("order["+i+"][menuId]");
    			String menuSort 	= request.getParameter("order["+i+"][menuSort]");
    			String menuUpperId 	= request.getParameter("order["+i+"][menuUpperId]");

    			if(menuId != null) {
	    			Integer _menuId   = Integer.parseInt( menuId );
	    			Integer _menuSort = Integer.parseInt( menuSort );

	    			ProductCategory _pc =
						new ProductCategory()
	    					.addProductCategoryId( _menuId )
	    					.addSortOrder( _menuSort )
	    					.addProductMenuId( productMenuId )
	    				;

	    			service.updateProductCommonCategory(_pc);
    			}
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
    public HashMap<String,Object> category_del(@ModelAttribute ProductCategory pc){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		pc.setDeleteId( user.getUserEmail() );
        		service.deleteProductCommonCategory(pc);

	    		retMap.put("result", "success");
	    		retMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retMap.put("result", "fail");
	    		retMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getCause().getMessage());
		}

        return retMap;
    }

    @PostMapping("/category-resotre")
    @ResponseBody
    public HashMap<String,Object> category_resotre(@ModelAttribute ProductCategory pc){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    		if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		pc.setLastUpdateId( user.getUserEmail() );
        		service.resotreProductCommonCategory(pc);

	    		retMap.put("result", "success");
	    		retMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retMap.put("result", "fail");
	    		retMap.put("message", "로그인 문제가 발생되었습니다.");
        	}

		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getCause().getMessage());
		}

        return retMap;
    }

    //템플릿 저장 기능
    @PostMapping( "/addTemplate")
    @ResponseBody
    public void ImgSaveTest(ProductTemplate productTemplate) {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //나머지 정보들 등록 기능

        //html 저장
        productTemplate.setCreateId(user.getUserEmail());
        service.insertProductTemplate(productTemplate);
        service.htmlImageConverter(productTemplate, user.getUserEmail());
    }

    @GetMapping( "/template-list")
    @ResponseBody
    public HashMap<String,Object> templateList(){
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("contentType", "template");

            List<ProductTemplate> templateList = service.selectProductTemplateList(paramMap);

            resultMap.put("templateList", templateList);

            resultMap.put("result","success");
            resultMap.put("message","처리 성공");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }


    @GetMapping("/getSampleTemplate")
    @ResponseBody
    public HashMap<String,Object> selectTemplate(@RequestParam int templateId){
        logger.debug("templateId: "+templateId);
        HashMap<String,Object> resultMap = new HashMap<>();
        try{

            ProductTemplate template = service.getSampleTemplateById(templateId);
            String templateInfo = template.getTemplateHtml();

            logger.debug("templateInfo: "+templateInfo);

            resultMap.put("template",templateInfo);
            resultMap.put("result","success");
            resultMap.put("message","처리 성공");

        }catch(Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();

        }

        return resultMap;
    }


    @PostMapping("/remove-template")
    @ResponseBody
    public HashMap<String, Object> removeTemplate(@RequestParam HashMap<String, Object> paramMap) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            service.deleteTemplate(paramMap);

            resultMap.put("result","success");
            resultMap.put("message","삭제되었습니다");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @GetMapping("/load-price-option")
    @ResponseBody
    public HashMap<String, Object> loadPriceOption(@RequestParam HashMap<String, Object> paramMap) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            //요구 파라미터 : 상품종류
            List<ProductPriceOption.DayList> basicPriceOptionList = service.selectPriceOptionList(paramMap);

            resultMap.put("basicPriceOptionList", basicPriceOptionList);
            resultMap.put("result", "success");
            resultMap.put("message", "처리되었습니다");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", "처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @GetMapping("/load-modify-price-option")
    @ResponseBody
    public HashMap<String, Object> loadModifyPriceOption(@RequestParam HashMap<String, Object> paramMap) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            List<ProductPriceOption> addedPriceSetList = service.selectAddedPriceSetList(paramMap);
            List<ProductPriceOption> uniqueList = new ArrayList<>(addedPriceSetList.stream()
                    .collect(Collectors.toMap(ProductPriceOption::getPriceOptionId, Function.identity(), (o1, o2) -> o1))
                    .values());
            paramMap.put("priceOptionId", uniqueList);
            for ( ProductPriceOption productPriceOption : addedPriceSetList ) {
                if ( productPriceOption.getPriceSetType().equals("periodSamePrice") ) {
                    paramMap.put("priceSetType", "periodSamePrice");
                    break;
                }
            }
            List<ProductPriceOption> addedBasicPriceList = service.selectAddedBasicPriceList(paramMap);

            Optional<ProductPriceOption> startDate = addedPriceSetList.stream()
                    .min(Comparator.comparing(ProductPriceOption::getPriceSetDate));
            Optional<ProductPriceOption> endDate = addedPriceSetList.stream()
                    .max(Comparator.comparing(ProductPriceOption::getPriceSetDate));

            resultMap.put("addedBasicPriceList", addedBasicPriceList);
            resultMap.put("addedPriceSetList", addedPriceSetList);
            resultMap.put("startDate", startDate);
            resultMap.put("endDate", endDate);

            resultMap.put("result", "success");
            resultMap.put("message", "처리되었습니다");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", "처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }


    @PostMapping("/load-added-price-option-list")
    @ResponseBody
    public HashMap<String, Object> loadAddedPriceOptionList(@RequestBody ProductPriceOption productPriceOption) {
        HashMap<String,Object> resultMap = new HashMap<>();

        List<ProductPriceOption> AddedPriceOptionList = new ArrayList<>();
        try {
            if( productPriceOption.getOptionGroupCode() != null ) {
                AddedPriceOptionList = service.selectAddedPriceSetMinimumList(productPriceOption);
            }

            resultMap.put("AddedPriceOptionList", AddedPriceOptionList);
            resultMap.put("result", "success");
            resultMap.put("message", "처리되었습니다");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", "처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }


    @PostMapping("/add-product-schedule")
    @ResponseBody
    public HashMap<String,Object> addProductSchedule(@RequestBody ProductPriceOption productPriceOption) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HashMap<String,Object> paramMap = service.insertProductSchedule(loginUser, productPriceOption);

            resultMap.put("basicPriceIdList", paramMap.get("basicPriceIdList"));
            resultMap.put("priceIdList", paramMap.get("priceIdList"));
            resultMap.put("result", "success");
            resultMap.put("message", "저장되었습니다");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", "처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    //판매항목 추가
    @PostMapping("/add-basic-option")
    @ResponseBody
    public HashMap<String,Object> addBasicOption(@RequestBody ProductPriceOption.BasicPriceList basicPriceList) {
        HashMap<String,Object> resultMap = new HashMap<>();

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        basicPriceList.setCreateId(loginUser.getUserEmail());

        try {
            service.insertPriceBasicOption(basicPriceList);

            resultMap.put("priceOptionId", basicPriceList.getPriceOptionId());
            resultMap.put("result", "success");
            resultMap.put("message", "처리되었습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", "처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/remove-price-set")
    @ResponseBody
    public HashMap<String, Object> removePriceSet(@RequestBody HashMap<String, Object> paramMap) {
        HashMap<String,Object> resultMap = new HashMap<>();

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            paramMap.put("createId", loginUser.getUserEmail());
            service.deletePriceSetService(paramMap);
            List<ProductPriceOption> deletePriceOptionList = service.selectAddedPriceSetList(paramMap);

            resultMap.put("deletePriceOptionList", deletePriceOptionList);
            resultMap.put("result", "success");
            resultMap.put("message", "삭제되었습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", "처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @GetMapping("/product-list")
    @ResponseBody
    public HashMap<String, Object> productList(
    		@RequestParam HashMap<String,Object> paramMap,
    		String arrExceptList,
    		String limitMenuUrl
    		){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            //메뉴 정보 불러오기
        	if( paramMap.containsKey("typeCode") ) {
	            String manageUrl = "/" + paramMap.get("typeCode");
	            MenuUser menu = service.selectManageMenuByUrl(manageUrl);
	            int menu_id=menu.getMenuId().intValue();
	            paramMap.put("menuId",menu_id);
        	}

            if(!paramMap.containsKey("orderYn"))       paramMap.put("orderYn", "Y");
            if(!paramMap.containsKey("productStatus")) paramMap.put("productStatus", "ALL");

        	System.out.println( arrExceptList );

        	if( limitMenuUrl != null ) {
        		paramMap.put("menuUrl", limitMenuUrl);

        	}
        	if( arrExceptList != null ) {
        		String[] list = arrExceptList.split(",");

        		paramMap.put("exceptList", list);
        	}


            List<ProductInfo> productList = service.selectProductList(paramMap);
            int productListCount = service.getProductListCount(paramMap);

            resultMap.put("recordsTotal", productList);
            resultMap.put("recordsFiltered", productListCount);
            resultMap.put("data", productList);

            resultMap.put("result", "success");
            resultMap.put("message", "처리되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @PostMapping("/change-product-order")
    @ResponseBody
    public HashMap<String, Object> changeProductOrder(@RequestBody List<ProductInfo> productInfoList){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (ProductInfo productInfo : productInfoList){
                productInfo.setCreateId(loginUser.getUserEmail());
                service.updateProductOrder(productInfo);
            }


            resultMap.put("result", "success");
            resultMap.put("message", "저장되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }
    /*####################################### 정보 관리 ####################################################*/
    @GetMapping("/category/list")
    public ModelAndView productCategoryListPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/manage/sub/info/productCategory/product-category-list");

//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("upperCode", "product, rentCarOption");
//        List<CodeInfo> productCodeList = service.selectOptionCodeList(paramMap);
//        mav.addObject("productCodeList", productCodeList);

        return mav;
    }
    @GetMapping("/option/list")
    public ModelAndView productOptionListPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/manage/sub/info/productOption/product-option-list");

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("upperCode", "product, rentCarOption");
        List<CodeInfo> productCodeList = service.selectOptionCodeList(paramMap);
        mav.addObject("productCodeList", productCodeList);

        return mav;
    }


    @GetMapping("/product-option-list")
    @ResponseBody
    public HashMap<String, Object> productOptionList(@RequestParam HashMap<String,Object> paramMap){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            ArrayList<ProductPriceOption> productOptionList = service.selectProductOptionList(paramMap);
            int productOptionListCount = service.selectProductOptionListCount();

            resultMap.put("recordsTotal", productOptionListCount);
            resultMap.put("recordsFiltered", productOptionListCount);
            resultMap.put("data", productOptionList);

            resultMap.put("result", "success");
            resultMap.put("message", "처리되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }


    @GetMapping("/product-option-item")
    @ResponseBody
    public HashMap<String, Object> productOptionItem(@RequestParam HashMap<String, Object> paramMap){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            ProductPriceOption productOptionItem = service.selectProductOptionItem(paramMap);

            resultMap.put("productOptionItem", productOptionItem);

            resultMap.put("result", "success");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }


    @PostMapping("/add-product-option")
    @ResponseBody
    public HashMap<String, Object> addProductOption(ProductPriceOption productPriceOption){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            productPriceOption.setCreateId(loginUser.getUserEmail());
            service.insertProductOption(productPriceOption);

            resultMap.put("result", "success");
            resultMap.put("message", "저장되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }


    @PostMapping("/modify-product-option")
    @ResponseBody
    public HashMap<String, Object> modifyProductOption(ProductPriceOption productPriceOption){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            productPriceOption.setCreateId(loginUser.getUserEmail());

            service.updateProductOption(productPriceOption);

            resultMap.put("result", "success");
            resultMap.put("message", "저장되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }


    @PostMapping("/delete-product-option")
    @ResponseBody
    public HashMap<String, Object> deleteProductOption(ProductPriceOption productPriceOption){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            productPriceOption.setCreateId(loginUser.getUserEmail());

            service.deleteProductOption(productPriceOption);

            resultMap.put("result", "success");
            resultMap.put("message", "삭제되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }


    @PostMapping("/change-product-option-order")
    @ResponseBody
    public HashMap<String, Object> changeProductOptionOrder(@RequestBody List<ProductPriceOption.BasicPriceList> basicPriceList){
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (ProductPriceOption.BasicPriceList basicPrice : basicPriceList){
                basicPrice.setCreateId(loginUser.getUserEmail());
                service.updateBasicProductOptionOrder(basicPrice);
            }


            resultMap.put("result", "success");
            resultMap.put("message", "저장되었습니다.");
        } catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
            resultMap.put("message", e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }



    @PostMapping("/upload-tour-detail-schedule-image")
    @ResponseBody
    public HashMap<String, Object> uploadTourDetailScheduleImage(@RequestParam("file") List<MultipartFile> attachFile){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (attachFile != null) {
                List<MultipartFile> multipartFiles = attachFile;
                if (attachFile.size() > 0) {
                    HashMap<String, Object> paramMap = new HashMap<>();
                    //저장 디렉토리 생성 확인
                    File file = new File(imageUploadPath);
                    if (!file.exists()) file.mkdirs();
                    //업로드 된 파일들 복사
                    for (MultipartFile multipartFile : multipartFiles) {
                        if (!multipartFile.getContentType().contains("jpg") &&
                                !multipartFile.getContentType().contains("jpeg") &&
                                !multipartFile.getContentType().contains("png")) {
                            throw new Exception("jpg, jpeg, png 파일을 첨부해주세요.");
                        }

                        if (multipartFile.getSize() > maxFileSize) {
                            throw new Exception((maxFileSize) + "MB 이하의 파일만 첨부 가능합니다.");
                        }

                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(imageUploadPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());

                        ProductDetailScheduleImage productDetailScheduleImage = new ProductDetailScheduleImage();
                        productDetailScheduleImage.setDetailId(0);
                        productDetailScheduleImage.setUploadPath(imageUploadPath);
                        productDetailScheduleImage.setUploadFilename(uploadName);
                        productDetailScheduleImage.setOriginFilename(multipartFile.getOriginalFilename());
                        productDetailScheduleImage.setFileSize((int) multipartFile.getSize());
                        productDetailScheduleImage.setFileMimetype(multipartFile.getContentType());
                        if (multipartFile.getOriginalFilename().contains(".")) {
                            productDetailScheduleImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                        }
                        productDetailScheduleImage.setCreateId(loginUser.getUserEmail());

                        service.insertDetailScheduleAttachFile(productDetailScheduleImage);
                        returnMap.put("imageNum", productDetailScheduleImage.getDetailImageId());
                        returnMap.put("uploadFilename", productDetailScheduleImage.getUploadFilename());
                    }
                }
            }

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }


        return returnMap;
    }
    @PostMapping("/upload-template-image")
    @ResponseBody
    public HashMap<String,Object> uploadEditorImage(@RequestParam(value="editor-image-upload",required = false) MultipartFile[] imageFiles,
                                                    @RequestParam(value="editor-bgImage-upload",required = false)MultipartFile[] bgImageFiles){
        HashMap<String, Object> resultMap = new HashMap<>();
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            ArrayList<ProductTemplateFile> uploadImages = new ArrayList<>();
            if (imageFiles != null && imageFiles.length > 0){
                File file = new File(imageUploadPath+addPath);
                if(!file.exists()) file.mkdirs();

                //업로드 된 이미지 파일들 복사
                for(MultipartFile multipartFile : imageFiles){

                    if(multipartFile.getSize() > maxFileSize){
                        throw new Exception((maxFileSize / 1024) + "MB 이하의 파일만 첨부 가능합니다.");
                    }

                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(imageUploadPath+addPath+uploadName));

                    ProductTemplateFile templateFile = new ProductTemplateFile();
                    templateFile.setUploadPath(imageUploadPath+addPath);
                    templateFile.setUploadFilename(uploadName);
                    templateFile.setOriginFilename(multipartFile.getOriginalFilename());
                    templateFile.setFileSize((int) multipartFile.getSize());
                    templateFile.setFileMimetype(multipartFile.getContentType());
                    if(multipartFile.getOriginalFilename().contains(".")) {
                        templateFile.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1));
                    }
                    templateFile.setCreateId(user.getUserEmail());

                    service.uploadEditorImage(templateFile);
                    uploadImages.add(templateFile);

                }
            }
            if(bgImageFiles != null && bgImageFiles.length > 0){
                File file = new File(imageUploadPath+addPath);
                if(!file.exists()) file.mkdirs();

                //업로드 된 이미지 파일들 복사
                for(MultipartFile multipartFile : bgImageFiles){

                    if(multipartFile.getSize() > maxFileSize){
                        throw new Exception((maxFileSize / 1024) + "MB 이하의 파일만 첨부 가능합니다.");
                    }

                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(imageUploadPath+addPath+uploadName));

                    ProductTemplateFile templateFile = new ProductTemplateFile();
                    templateFile.setUploadPath(imageUploadPath+addPath);
                    templateFile.setUploadFilename(uploadName);
                    templateFile.setOriginFilename(multipartFile.getOriginalFilename());
                    templateFile.setFileSize((int) multipartFile.getSize());
                    templateFile.setFileMimetype(multipartFile.getContentType());
                    if(multipartFile.getOriginalFilename().contains(".")) {
                        templateFile.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1));
                    }
                    templateFile.setCreateId(user.getUserEmail());

                    service.uploadEditorImage(templateFile);
                    uploadImages.add(templateFile);

                }
            }
            resultMap.put("result", "success");
            resultMap.put("list", uploadImages);

        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
        }

        return resultMap;
    }


    @PostMapping("/upload-product-images")
    @ResponseBody
    public HashMap<String, Object> uploadProductImages(MultipartRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.debug("request file:"+request.getFiles("upload"));
        try{
            if(request.getFile("upload") != null){
                List<MultipartFile> multipartFiles = request.getFiles("upload");
                ArrayList<ProductTourImages> productSlideImages = new ArrayList<>();
                if(!multipartFiles.isEmpty()){
                    //저장 디렉토리 생성 확인
                    File file = new File(imageUploadPath + addPath);
                    if(!file.exists()) file.mkdirs();

                    //업로드 된 이미지 파일들 복사
                    for(MultipartFile multipartFile : multipartFiles){
                        if(multipartFile.getSize() > maxFileSize){
                            throw new Exception(String.valueOf(maxFileSize/1024/1024) + "MB 이하의 파일만 첨부 가능합니다.");
                        }
                        if (!multipartFile.getContentType().contains("jpg") &&
                                !multipartFile.getContentType().contains("jpeg") &&
                                !multipartFile.getContentType().contains("png")) {
                            throw new Exception("jpg, jpeg, png 파일을 첨부해주세요.");
                        }

                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(imageUploadPath+addPath+uploadName));
                        logger.debug("Product Image File Uploaded : " + multipartFile.getOriginalFilename());

                        ProductTourImages productImage = new ProductTourImages();
                        productImage.setServiceType("");
                        productImage.setUploadPath(imageUploadPath+addPath);
                        productImage.setUploadFilename(uploadName);
                        productImage.setOriginFilename(multipartFile.getOriginalFilename());
                        productImage.setFileSize(multipartFile.getSize());
                        productImage.setFileMimetype(multipartFile.getContentType());
                        if(multipartFile.getOriginalFilename().contains(".")) {
                            productImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1));
                        }
                        productImage.setCreateId(user.getUserEmail());

                        service.writeProductSlideImages(productImage);
                        productSlideImages.add(productImage);
                    }
                }
                resultMap.put("result", "success");
                resultMap.put("list", productSlideImages);
            }
            else{
                throw new Exception("첨부된 파일이 없습니다.");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
        }

        return resultMap;
    }

    @DeleteMapping("/removeEditorImage")
    @ResponseBody
    public HashMap<String, Object> deleteProductImages(
            @RequestParam(value="file_id", defaultValue="0") int fileID,
            @RequestParam(value="template_id", defaultValue="0") int templateId){
        HashMap<String, Object> resultMap = new HashMap<>();
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            logger.debug("DELETE FILE... ACCESS USER : " + user.getUserEmail());

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("templateId", templateId);
            paramMap.put("fileId", fileID);

//            ProductTourImages image = service.getProductImagesByKey(paramMap);
            ProductTemplateFile templateFile = service.getProductTemplateFileImageByKey(paramMap);
            File file = new File(templateFile.getUploadPath()+templateFile.getUploadFilename() +addPath);
            if(file.exists()){
                logger.debug("파일을 삭제했습니다. Delete File Path : " + file.getPath() + file.getName());
                file.delete();
            }else{
                logger.debug("파일이 존재하지 않습니다.");
            }

            service.deleteProductImageByKey(paramMap);

            resultMap.put("result", "success");
            resultMap.put("message", "삭제됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
        }
        return resultMap;
    }

    @DeleteMapping
    @ResponseBody
    public HashMap<String,Object> deleteEditorImage(
            @RequestParam(value="file_id",defaultValue ="0") int fileId,
            @RequestParam(value="template_id",defaultValue = "0") int templateId
    ){
        HashMap<String, Object> resultMap = new HashMap<>();
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            logger.debug("DELETE FILE... ACCESS USER : " + user.getUserEmail());

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("templateId", templateId);
            paramMap.put("fileId", fileId);

//            ProductTemplateFile template = service.getProductImagesByKey(paramMap);
            ProductTemplateFile template = service.getTemplateEditorImageByKey(paramMap);
            logger.debug("file-path: "+template.getUploadPath()+template.getUploadFilename() + addPath);
            File file = new File(template.getUploadPath()+template.getUploadFilename() + addPath);
            if(file.exists()){
                logger.debug("파일을 삭제했습니다. Delete File Path : " + file.getPath() + file.getName());
                file.delete();
            }else{
                logger.debug("파일이 존재하지 않습니다.");
            }

//            service.deleteProductImageByKey(paramMap);
            service.deleteTemplateEditorImageByKey(paramMap);

            resultMap.put("result", "success");
            resultMap.put("message", "삭제됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("error", e.getMessage());
        }
        return resultMap;
    }

    @PostMapping("/saveProductInfo")
    @ResponseBody
    public HashMap<String,Object> saveProductInfo(ProductInfo productInfo){
        // 임시저장
        HashMap<String,Object> resultMap = new HashMap<>();
        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // JSON wrapper 클래스로부터 binding
//            ProductInfo basicInfo = productJsonWrapper.getProductInfo();
//            ProductTemplate templateInfo = productJsonWrapper.getProductTemplate();
//
//            // product_menu_id  -> menu ID 삽입
////            String url = "/product/domestic";
////            MenuUser menu = service.selectManageMenuByUrl(url);
//
//            if(productInfo != null){
//                productInfo.setCreateId(loginUser.getUserEmail());
////				basicInfo.setProductMenuId(Long.valueOf(menu.getMenuId()).intValue());
//                logger.debug("images: "+productInfo.getImages());
//                if(productInfo.getImages() != null && productInfo.getImages().length > 0){
//                    String productImages = "";
//                    for(String image : productInfo.getImages()){
//                        if(image != null){
//                            if(productImages.equals("")){
//                                productImages =image;
//                            }else{
//                                productImages+=","+image;
//                            }
//                        }
//                    }
//                    productInfo.setProductImages(productImages);
//                }
//
//                // info
//                service.insertProductTempInfo(productInfo);
//                resultMap.put("id",productInfo.getProductTourId());
//                resultMap.put("result","success");
//                resultMap.put("status","success");
//            }
//            if(templateInfo != null ){
//                logger.debug("templateInfo: "+templateInfo.getProductTourId());
//                templateInfo.setCreateId(loginUser.getUserEmail());
//
//                // template
//                service.insertProductTemplate(templateInfo);
//                resultMap.put("result","success");
//                resultMap.put("status","success");
//            }

            resultMap.put("result","fail");

        }catch (Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/saveProduct")
    @ResponseBody
    public HashMap<String,Object> saveProductInfo(@RequestBody ProductJsonWrapper productJsonWrapper){
        // 임시저장
        HashMap<String,Object> resultMap = new HashMap<>();
        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // JSON wrapper 클래스로부터 binding
            ProductInfo basicInfo = productJsonWrapper.getProductInfo();
            ProductTemplate templateInfo = productJsonWrapper.getProductTemplate();

            // product_menu_id  -> menu ID 삽입
//            String url = "/product/domestic";
//            MenuUser menu = service.selectManageMenuByUrl(url);

            if(basicInfo != null){
                basicInfo.setCreateId(loginUser.getUserEmail());
//				basicInfo.setProductMenuId(Long.valueOf(menu.getMenuId()).intValue());
                logger.debug("images: "+basicInfo.getImages());
                if(basicInfo.getImages() != null && basicInfo.getImages().length > 0){
                    String productImages = "";
                    for(String image : basicInfo.getImages()){
                        if(image != null){
                            if(productImages.equals("")){
                                productImages =image;
                            }else{
                                productImages+=","+image;
                            }
                        }
                    }
                    basicInfo.setProductImages(productImages);
                }

                // info
                service.insertProductInfoWithProductSerial(basicInfo); //upsert with serial updates
                resultMap.put("id",basicInfo.getProductTourId());
                resultMap.put("result","success");
                resultMap.put("status","success");
            }
            if(templateInfo != null ){
                logger.debug("templateInfo: "+templateInfo.getProductTourId());
                templateInfo.setCreateId(loginUser.getUserEmail());

                // template
                service.insertProductTemplate(templateInfo);
                resultMap.put("result","success");
                resultMap.put("status","success");
            }
        }catch (Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/modifyProduct")
    @ResponseBody
    public HashMap<String,Object> modifyProductInfo(@RequestBody ProductJsonWrapper productJsonWrapper){
        HashMap<String,Object> resultMap = new HashMap<>();

        try{
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // JSON wrapper 클래스로부터 binding
            ProductInfo basicInfo = productJsonWrapper.getProductInfo();
            ProductTemplate templateInfo = productJsonWrapper.getProductTemplate();

            // product_menu_id  -> menu ID 삽입
            //String url = "/product/domestic";
            //MenuUser menu = service.selectManageMenuByUrl(url);

            int productTourId = 0;

            if(basicInfo != null){
                basicInfo.setCreateId(loginUser.getUserEmail());
                if(basicInfo.getImages() != null && basicInfo.getImages().length > 0){
                    String productImages = "";
                    logger.debug("images: "+basicInfo.getImages());
                    for(String image : basicInfo.getImages()){
                        if(image != null){
                            if(productImages.equals("")){
                                productImages = image;
                            }else{
                                productImages+=","+image;
                            }
                        }
                    }
                    logger.debug("images: "+productImages);

                    basicInfo.setProductImages(productImages);
                }
                // info
                service.updateProductInfo(basicInfo);
                productTourId = service.updateAndCopyProductInfo(basicInfo);
                resultMap.put("productTourId", productTourId);
                resultMap.put("result","success");
                resultMap.put("message","저장되었습니다.");
            }
            if(templateInfo != null ){
                //templateInfo.setCreateId(loginUser.getUserEmail());
                //TODO 메뉴 기능 완성시 menu NULL 현상 처리
                //templateInfo.setProductTourId(Long.valueOf(menu.getMenuId()).intValue());

                //template
                //service.updateProductTemplate(templateInfo);

                //상품의 description 업데이트
                ProductInfo productInfo = new ProductInfo();
                productInfo.setCreateId(loginUser.getUserEmail());
                productInfo.setProductDescription(templateInfo.getTemplateHtml());
                productInfo.setProductDescriptionType(templateInfo.getTemplateType());
                productInfo.setProductTourId(templateInfo.getProductTourId());
                service.updateProductDescription(productInfo);
                //상품 복사
                productTourId = service.updateAndCopyProductInfo(productInfo);

                resultMap.put("productTourId", productTourId);
                resultMap.put("result","success");
                resultMap.put("message","저장되었습니다.");
            }
        }catch(Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;

    }

    @PostMapping("/registProduct")
    @ResponseBody
    public HashMap<String,Object> writeProductInfo(@ModelAttribute ProductInfo productInfo, HttpSession session){
        // 등록하기 - 임시저장X
        HashMap<String,Object> resultMap = new HashMap<>();
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            productInfo.setCreateId(user.getUserEmail());

            // product_menu_id  -> menu ID 삽입
            String url = "/"+productInfo.getUrl();
            MenuUser menu = service.selectManageMenuByUrl(url);
            // TODO 아래  "menu" is null 오류 남
//            productInfo.setProductMenuId(Long.valueOf(menu.getMenuId()).intValue());
            if(productInfo.getImages() != null && productInfo.getImages().length >0){
                String productImages = "";
                for(String image : productInfo.getImages()){
                    if(image != null){
                        if(productImages.equals("")){
                            productImages = image;
                        }else{
                            productImages += ","+image;
                        }
                    }
                }
                productInfo.setProductImages(productImages);
            }
            // save info
//            service.insertProductInfo(productInfo);
//			service.updateAndCopyProductInfo(productInfo);
            service.updateProductInfo(productInfo);

            session.removeAttribute("tempBasicInfo");
            session.removeAttribute("tempTemplateInfo");
            resultMap.put("result","success");
            resultMap.put("message","처리되었습니다.");
            resultMap.put("status","success");
        }catch (Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }



    @PostMapping("/removeProduct")
    @ResponseBody
    public HashMap<String,Object> removeProductInfo(@ModelAttribute ProductInfo productInfo, HttpSession session){
        // 등록하기 - 임시저장X
        HashMap<String,Object> resultMap = new HashMap<>();
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            productInfo.setCreateId(user.getUserEmail());
            service.deleteProductInfo(productInfo);

            resultMap.put("result","success");
            resultMap.put("message","취소되었습니다.");
        }catch (Exception e){
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    @PostMapping("/add-product-price-info")
    @ResponseBody
    public HashMap<String,Object> addProductPriceInfo(@RequestBody ProductPostJson productPostJson) {
        HashMap<String,Object> resultMap = new HashMap<>();
        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            int productTourId = service.updateBasicPriceOptionUseYnService(loginUser, productPostJson);

            resultMap.put("productTourId",productTourId);
            resultMap.put("result","success");
            resultMap.put("message","저장되었습니다");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/add-product-detail-schedule")
    @ResponseBody
    public HashMap<String,Object> addProductDetailSchedule(@RequestBody ProductPostJson productPostJson) {
        HashMap<String,Object> resultMap = new HashMap<>();
        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            int productTourId = service.addProductDetailScheduleService(loginUser, productPostJson);

            resultMap.put("productTourId",productTourId);
            resultMap.put("result","success");
            resultMap.put("message","저장되었습니다");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    //상품복사
    @PostMapping("/copy-product")
    @ResponseBody
    public HashMap<String, Object> copyProduct(@RequestParam HashMap<String, Object> paramMap) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            service.copyProductService(paramMap);

            resultMap.put("result","success");
            resultMap.put("message","복사되었습니다");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/change-use-yn")
    @ResponseBody
    public HashMap<String, Object> changeProductUseYn(@RequestParam HashMap<String, Object> paramMap) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            paramMap.put("createId", loginUser.getUserEmail());
            int updateCount = service.updateProductUseYn(paramMap);

            resultMap.put("updateCount",updateCount);
            resultMap.put("result","success");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PostMapping("/save-rentcar-info")
    @ResponseBody
    public HashMap<String, Object> saveRentCarInfo(@RequestBody ProductInfo productInfo) {
        HashMap<String,Object> resultMap = new HashMap<>();

        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            productInfo.setCreateId(loginUser.getUserEmail());

            int productTourId = service.updateProductRentCarInfoService(productInfo);

            resultMap.put("productTourId", productTourId);
            resultMap.put("result","success");
            resultMap.put("message","처리되었습니다.");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    private void setSessionProductId(HttpSession session,int productID){
        if(session.getAttribute("productId") == null ){
            session.setAttribute("productId",productID);
        }
    }


    /*################################################ProductInventory################################################*/
    @GetMapping("/inventory")
    public ModelAndView ProductInventoryList(
    		HttpServletRequest request,
    		ProductInventory bc,
    		@RequestParam(value="page", defaultValue="1") int page,
    		@RequestParam(value="pageSize", defaultValue="5") int pageSize,
    		@Param(value="boardId") String boardId,
    		@Param(value="boardUrl") String boardUrl,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike){
        ModelAndView mav = new ModelAndView();

        try {
    		HashMap<String, Object> paramMap = new HashMap<>();
//
//    		List<SortData> listSort = getListOrder(request);
//    		paramMap.put("listSort", listSort);
//
//			paramMap.put("boardId", boardId);
//			paramMap.put("boardUrl", boardUrl);
//			paramMap.put("titleLike", titleLike);
//			paramMap.put("contentLike", contentLike);
//
//    		int totalCount = boardService.selectCountBoardContents(paramMap);
//
//    		PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
//			paramMap.put("itemStartPosition", paging.getItemStartPosition());
//			paramMap.put("pagePerSize", paging.getPagePerSize());
//
//            mav.addObject("p", paramMap);
//            mav.addObject("boardList", boardService.selectListBoardContents(paramMap));
//            mav.addObject("paging", paging);

    		paramMap.put("menuUrl","/shop");
    		paramMap.put("orderYn","Y");

        	mav.addObject("listProduct", service.selectProductList(paramMap));



		} catch (Exception e) {
			mav.addObject("boardList", null);
			logger.error(e.getMessage());
		}

        mav.setViewName("/manage/sub/productInventory/list");
        return mav;
    }

    @GetMapping("/inventory/form")
    public ModelAndView ProductInventoryForm(
    		HttpServletRequest request,
    		ProductInventory bc,
    		@RequestParam(value="id") Integer id ){
        ModelAndView mav = new ModelAndView();

//        HashMap<String, Object> paramMap = new HashMap<>();
//
//        paramMap.put("id", id);
//        paramMap.put("boardId", id);
//
//        mav.addObject("p", paramMap);
//        mav.addObject("content", boardService.selectOneBoardContents(paramMap));
//
//        mav.addObject("attachList", boardService.selectListBoardAttachFile(paramMap));

        mav.setViewName("/manage/sub/productInventory/form");
        return mav;
    }

    @PostMapping("/inventory-list")
    @ResponseBody
    public HashMap<String, Object> ProductInventory_list_ajax(
    		HttpServletRequest request,
    		ProductInventory bc,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="productSerial") String productSerial
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

//    		List<SortData> listSort = getListOrder(request);
//    		paramMap.put("listSort", listSort);

    		if(length >= 0) {
				paramMap.put("itemStartPosition", start);
				paramMap.put("pagePerSize", length);
    		}

			paramMap.put("productSerial", productSerial);

    		int totalCount = service.selectCountProductInventory(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", service.selectListProductInventory(paramMap));

    		retrunMap.put("summary", service.selectSummaryProductInventory(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }
    @PostMapping("/inventory-save")
    @ResponseBody
    public HashMap<String, Object> ProductInventory_save_ajax(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		ProductInventory bc,
    		//Multipart
    		HttpServletRequest request
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

	    		if(mode.equals("I")) {
	    			bc.setCreateId( user.getUserEmail());
	    			service.insertProductInventory(bc);
	    		} else {
	    			bc.setLastUpdateId( user.getUserEmail());
	    			service.updateProductInventory(bc);
	    		}
	    		List<MultipartFile> multipartFiles = null;

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

    @PostMapping("/inventory-delete")
    @ResponseBody
    public HashMap<String, Object> board_delete_ajax(
    		ProductInventory bc
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		service.deleteProductInventory(bc);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }
    
    
    @PostMapping("/save-link-product")
    @ResponseBody
    public HashMap<String,Object> saveLinkProduct(@RequestBody ProductPostJson productPostJson) {
        HashMap<String,Object> resultMap = new HashMap<>();
        try {
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            service.saveProductTourLink(loginUser, productPostJson);

            //resultMap.put("productTourId",productTourId);
            resultMap.put("result","success");
            resultMap.put("message","저장되었습니다");
        } catch (Exception e) {
            resultMap.put("result","error");
            resultMap.put("message","처리중 에러 발생하였습니다.");
            logger.info("------");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }
}
