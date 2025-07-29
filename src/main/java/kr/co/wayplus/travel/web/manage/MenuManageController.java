package kr.co.wayplus.travel.web.manage;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.model.MenuUser;
import kr.co.wayplus.travel.service.manage.BannerManageService;
import kr.co.wayplus.travel.service.manage.ManageService;
import kr.co.wayplus.travel.service.manage.MenuManageService;

@Controller
@RequestMapping("/manage/menu")
public class MenuManageController  {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private MenuManageService svcMenu;
	private final BannerManageService svcBanner;
	private final ManageService svcManage;

	@Autowired
	private MenuManageController(
			MenuManageService svc1,
			BannerManageService svc2,
			ManageService svc3 ) {
		this.svcMenu = svc1;
		this.svcBanner = svc2;
		this.svcManage = svc3;
	}

    @GetMapping( "/user/list-table")
    public ModelAndView menuList_table(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/menu/user/list1");
        return mav;
    }

    @GetMapping( "/user/list-order")
    public ModelAndView menuList_order(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/menu/user/changeOrder");
        return mav;
    }

    @GetMapping( "/user/list-tree")
    public ModelAndView menuList_tree(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/menu/user/list");
        return mav;
    }

    @GetMapping("/user-list")
    @ResponseBody
    public HashMap<String,Object> menu_List(){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		HashMap<String,Object> paramMap = new HashMap<String, Object>();

    		//HashMap<String,Object> categoryMap = svcManage.selectListProductCategoryByMenuId();

    		svcMenu.selectList(paramMap, retMap);

    		retMap.put("result","success");
    		retMap.put("message","처리 성공");
		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getMessage());
		}

        return retMap;
    }

    @PostMapping("/user-save")
    @ResponseBody
    public HashMap<String,Object> menu_save(@ModelAttribute MenuUser menu){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		HashMap<String,Object> paramMap = new HashMap<String, Object>();

//    		ArrayList<MenuUser> list = service.selectListMenuUser(paramMap);
//    		retMap.put("data",list);

    		if(menu != null) {
				/** 추가부분 ^---^ **/

    			if(menu.getMainExposeType() == null) {
    				menu.setMainExposeYn("N");
    			}else if(menu.getMainExposeType().equals("undefined")){
					menu.setMainExposeYn("N");
				}else{
					menu.setMainExposeYn("Y");
				}

    			if(menu.getMenuId() == null) {
    				svcMenu.insertMenuUser(menu);
    			} else {
    				svcMenu.updateMenuUser(menu);
    			}

    			retMap.put("result","success");
    			retMap.put("message","처리 성공 하였습니다.");
    		} else {
    			retMap.put("result","fail");
    			retMap.put("message","처리 실패 하였습니다.");
    		}
		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
			logger.info("------");
			logger.debug(e.getCause().getMessage());
		}

        return retMap;
    }

    @PostMapping("/user-duplicate")
    @ResponseBody
    public HashMap<String,Object> menu_duplicate(MenuUser menu){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		HashMap<String,Object> paramMap = new HashMap<String, Object>();

    		if(menu != null) {
    			paramMap.put("upperMenuId", menu.getUpperMenuId()  );
    			paramMap.put("menuUrl", menu.getMenuUrl());
    			Integer count = svcMenu.selectCountMenuUser(paramMap);

    			retMap.put("result","success");
    			retMap.put("message","처리 성공 하였습니다.");
    			retMap.put("count",count);

    		} else {
    			retMap.put("result","fail");
    			retMap.put("message","처리 실패 하였습니다.");
    		}
		} catch (Exception e) {
			retMap.put("result","ERROR");
			retMap.put("message","처리중 에러 발생하였습니다.");
		}

        return retMap;
    }

    @PostMapping("/user-order-save")
    @ResponseBody
    public HashMap<String,Object> menu_order_save(HttpServletRequest request ){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
//    		ArrayList<MenuUser> listMenu = new ArrayList<MenuUser>();

    		String total = request.getParameter("total");

    		for (int i = 0; i < Integer.parseInt(total); i++) {

    			String menuId 		= request.getParameter("order["+i+"][menuId]");
    			String menuSort 	= request.getParameter("order["+i+"][menuSort]");
    			String menuUpperId 	= request.getParameter("order["+i+"][menuUpperId]");

    			System.out.println(menuId + "," + menuSort + "," + menuUpperId );


    			Long _menuId 	  = Long.parseLong( menuId );
    			Integer _menuSort = Integer.parseInt( menuSort );

    			MenuUser _menu = new MenuUser().addMenuId( _menuId ).addMenuSort( _menuSort );

    			if( menuUpperId != null ) {
    				_menu.addupperMenuId( Long.parseLong( menuUpperId ) );
    			}

    			svcMenu.updateMenuUser(_menu);
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
    @PostMapping("/user-del")
    @ResponseBody
    public HashMap<String,Object> menu_del(MenuUser menu){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		svcMenu.deleteMenuUser(menu);

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
