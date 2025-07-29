package kr.co.wayplus.travel.web.admin;

import java.util.HashMap;
import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.PagingDTO;
import kr.co.wayplus.travel.service.admin.AdminService;
import kr.co.wayplus.travel.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.model.ManageMenu;
import kr.co.wayplus.travel.service.manage.ManageService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${key.crypto.encrypt}")
	private String encrypt;
	@Value("${key.crypto.iv}")
	private String iv;

	private AdminService adminService;

	private ManageService service;

	@Autowired
	private AdminController(AdminService adminService, ManageService svc ) {
		this.adminService = adminService;
		this.service = svc;
	}

	@GetMapping("/menu/manage/")
	public ModelAndView menuList(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("manage/sub/menu/manage/list");
		return mav;
	}

    @GetMapping("/menu/manage/list")
    @ResponseBody
    public HashMap<String,Object> menu_List(){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		service.selectList(null, retMap);

//    		HashMap<String,Object> paramMap = new HashMap<String, Object>();
//
//    		ArrayList<ManageMenu> listTopMenu = new ArrayList<ManageMenu>(); /*최상위 메뉴 뽑기*/
//    		HashMap<String,ManageMenu> _map = new HashMap<String, ManageMenu>(); /*색인용 Map*/
//    		HashMap<String,ArrayList<ManageMenu>> _mapUpper = new HashMap<String, ArrayList<ManageMenu>>(); /*상위 메뉴 색인용 Map*/
//    		ArrayList<ManageMenu> list = service.selectListManageMenu(paramMap);
//
//    		for (ManageMenu _menu : list) {
//				_map.put(_menu.getMenuId().toString(), _menu);
//
//				if(_menu.getUpperMenuId() != null) {
//					ArrayList<ManageMenu> subList = null;
//					if( _mapUpper.containsKey(_menu.getUpperMenuId().toString()) ) {
//						subList = _mapUpper.get( _menu.getUpperMenuId().toString() );
//					} else {
//						subList = new ArrayList<ManageMenu>();
//					}
//					_mapUpper.put( _menu.getUpperMenuId().toString(), subList );
//					subList.add( _menu );
//				}
//			}
//
//    		for (ManageMenu _tmenu : list) {
//    			Long key = _tmenu.getMenuId();
//
//    			if(key != null)
//	    			if( _mapUpper.containsKey( key.toString() ) ) {
//	    				ArrayList<ManageMenu> menuList = _mapUpper.get( key.toString());
//	    				Collections.sort( menuList );
//	    				_tmenu.setListChildMenuL( menuList );
//	    			}
//			}
//
//    		for (ManageMenu _menu : list) {
//    			if(_menu.getUpperMenuId() == null) {
//					listTopMenu.add(_menu);
//				}
//			}
//    		Collections.sort( listTopMenu );
//
//    		retMap.put("data",listTopMenu);
//    		retMap.put("list",list);

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
    @PostMapping("/menu/manage/save")
    @ResponseBody
    public HashMap<String,Object> menu_save(ManageMenu menu){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		HashMap<String,Object> paramMap = new HashMap<String, Object>();

//    		ArrayList<ManageMenu> list = service.selectListManageMenu(paramMap);
//    		retMap.put("data",list);

    		if(menu != null) {
    			if(menu.getMenuId() == null) {
    				service.insertManageMenu(menu);
    			} else {
    				service.updateManageMenu(menu);
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

    @PostMapping("/menu/manage/order-save")
    @ResponseBody
    public HashMap<String,Object> menu_order_save(HttpServletRequest request ){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
//    		ArrayList<ManageMenu> listMenu = new ArrayList<ManageMenu>();

    		String total = request.getParameter("total");

    		for (int i = 0; i < Integer.parseInt(total); i++) {

    			String menuId 		= request.getParameter("order["+i+"][menuId]");
    			String menuSort 	= request.getParameter("order["+i+"][menuSort]");
    			String menuUpperId 	= request.getParameter("order["+i+"][menuUpperId]");

    			System.out.println(menuId + "," + menuSort + "," + menuUpperId );


    			Long _menuId 	  = Long.parseLong( menuId );
    			Integer _menuSort = Integer.parseInt( menuSort );

    			ManageMenu _menu = new ManageMenu().addMenuId( _menuId ).addMenuSort( _menuSort );

    			if( menuUpperId != null ) {
    				_menu.addupperMenuId( Long.parseLong( menuUpperId ) );
    			}

    			service.updateManageMenu(_menu);
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
    @PostMapping("/menu/manage/del")
    @ResponseBody
    public HashMap<String,Object> menu_del(ManageMenu menu){
    	HashMap<String,Object> retMap = new HashMap<String, Object>();

    	try {
    		service.deleteManageMenu(menu);

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


	@GetMapping("/administrator/list")
	public ModelAndView administratorList(@RequestParam(value="page", defaultValue="1") int page,
										  @RequestParam(value="pageSize", defaultValue="10") int pageSize){
		ModelAndView mav = new ModelAndView();
		int totalCount = adminService.getAdministratorUserListCount();
		HashMap<String, Object> param = new HashMap<>();
		PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
		param.put("itemStartPosition", paging.getItemStartPosition());
		param.put("pagePerSize", paging.getPagePerSize());
		mav.addObject("paging", paging);
		mav.addObject("administratorList", adminService.getAdministratorUserList(param));
		mav.setViewName("manage/sub/administrator/list");
		return mav;
	}

	@GetMapping("/administrator/add")
	public ModelAndView administratorAdd(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("manage/sub/administrator/form");
		if(session.getAttribute("encrypt") == null || session.getAttribute("iv") == null){
			CryptoUtil cryptoUtil = new CryptoUtil();
			session.setAttribute("encrypt", cryptoUtil.generateRandomEncryptKey(""));
			session.setAttribute("iv", cryptoUtil.generateRandomIv(""));
		}
		return mav;
	}

	@GetMapping("/administrator/view")
	public ModelAndView administratorView(HttpSession session,
										  @RequestParam(value="userEmail", defaultValue="") String userEmail){
		ModelAndView mav = new ModelAndView();
		mav.addObject("user", adminService.getAdministratorInfo(userEmail));
		mav.setViewName("manage/sub/administrator/form");
		if(session.getAttribute("encrypt") == null || session.getAttribute("iv") == null){
			CryptoUtil cryptoUtil = new CryptoUtil();
			session.setAttribute("encrypt", cryptoUtil.generateRandomEncryptKey(""));
			session.setAttribute("iv", cryptoUtil.generateRandomIv(""));
		}
		return mav;
	}

	@PostMapping("/administrator/create")
	@ResponseBody
	public HashMap<String,Object> administratorCreate(HttpSession session,
													  @RequestParam(value="encrypted", defaultValue="true") String encrypted,
													  @ModelAttribute LoginUser user, BindingResult bindingResult){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();

		try {
			if(user == null
					|| user.getUserEmail() == null
					|| user.getUserPassword() == null
					|| user.getUserName() == null
			) {
				throw new Exception("필수 입력 정보를 확인 해 주세요.");
			}
			if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
			if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
			user.setEncrypt(encrypt);
			user.setIv(iv);
			user.setUserTokenId(String.valueOf(UUID.randomUUID()));
			adminService.createAdministrator(user, Boolean.parseBoolean(encrypted));

			resultMap.put("result","success");
			resultMap.put("message","처리 완료.");
		} catch (Exception e) {
			resultMap.put("result","ERROR");
			resultMap.put("message","처리중 오류가 발생했습니다.");
			logger.debug(e.getCause().getMessage());
		}

		return resultMap;
	}

	@PutMapping("/administrator/update")
	@ResponseBody
	public HashMap<String,Object> administratorUpdate(HttpSession session,
													  @RequestParam(value="encrypted", defaultValue="true") String encrypted,
													  @ModelAttribute LoginUser user, BindingResult bindingResult){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();

		try {
			if(user == null
					|| user.getUserEmail() == null
					|| user.getUserPassword() == null
					|| user.getUserName() == null
			) {
				throw new Exception("필수 입력 정보를 확인 해 주세요.");
			}
			if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
			if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
			user.setEncrypt(encrypt);
			user.setIv(iv);
			adminService.updateAdministrator(user, Boolean.parseBoolean(encrypted));

			resultMap.put("result","success");
			resultMap.put("message","처리 완료.");
		} catch (Exception e) {
			resultMap.put("result","ERROR");
			resultMap.put("message","처리중 오류가 발생했습니다.");
			logger.debug(e.getCause().getMessage());
		}

		return resultMap;
	}

	@PutMapping("/administrator/withdraw")
	@ResponseBody
	public HashMap<String,Object> administratorWithdraw(@ModelAttribute LoginUser user, BindingResult bindingResult){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();

		try {
			if(user == null
					|| user.getUserEmail() == null
					|| user.getUserTokenId() == null
			) {
				throw new Exception("필수 입력 정보를 확인 해 주세요.");
			}

			LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user.setOperator(loginUser.getUserEmail());
			adminService.withdrawalAdministrator(user);

			resultMap.put("result","success");
			resultMap.put("message","처리 완료.");
		} catch (Exception e) {
			resultMap.put("result","ERROR");
			resultMap.put("message","처리중 오류가 발생했습니다.");
			logger.debug(e.getCause().getMessage());
		}

		return resultMap;
	}


}
