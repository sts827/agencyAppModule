package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import kr.co.wayplus.travel.model.ManageMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.MenuManageMapper;
import kr.co.wayplus.travel.model.MenuUser;

@Service
public class MenuManageService {
	@Value("${upload.file.path}")
	private String imageUploadPath;
    private final MenuManageMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MenuManageService(MenuManageMapper m) {
        this.mapper = m;
    }

	public Integer selectCountMenuUser(HashMap<String, Object> paramMap) {
		return mapper.selectCountMenuUser(paramMap);
	}
	public ArrayList<MenuUser> selectListMenuUser(HashMap<String, Object> paramMap) {
		return mapper.selectListMenuUser(paramMap);
	}

	public void insertMenuUser(MenuUser menu) throws SQLException {
		mapper.insertMenuUser(menu);
	}

	public void updateMenuUser(MenuUser menu) throws SQLException {
		mapper.updateMenuUser(menu);
	}

	public void deleteMenuUser(MenuUser menu) throws SQLException {
		mapper.deleteMenuUser(menu);
	}

	public ArrayList<ManageMenu> selectManageMenuList(HashMap<String,Object> param) {
		return mapper.selectManageMenuList(param);
	}

	public MenuUser selectOneMenuUser(String menuUrl) {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("menuUrl",menuUrl);
		return mapper.selectOneMenuUser(paramMap);
	}

	public MenuUser selectOneMenuUser(HashMap<String,Object> paramMap) {
		return mapper.selectOneMenuUser(paramMap);
	}

	public void selectList(HashMap<String, Object> paramMap, HashMap<String, Object> retMap) {
		ArrayList<MenuUser> listTopMenu = new ArrayList<MenuUser>(); /*최상위 메뉴 뽑기*/
		HashMap<String,MenuUser> _map = new HashMap<String, MenuUser>(); /*색인용 Map*/
		HashMap<String,ArrayList<MenuUser>> _mapUpper = new HashMap<String, ArrayList<MenuUser>>(); /*상위 메뉴 색인용 Map*/
		ArrayList<MenuUser> list = this.selectListMenuUser(paramMap);

		for (MenuUser _menu : list) {
			_map.put(_menu.getMenuId().toString(), _menu);

			if(_menu.getUpperMenuId() != null) {
				ArrayList<MenuUser> subList = null;
				if( _mapUpper.containsKey(_menu.getUpperMenuId().toString()) ) {
					subList = _mapUpper.get( _menu.getUpperMenuId().toString() );
				} else {
					subList = new ArrayList<MenuUser>();
				}
				_mapUpper.put( _menu.getUpperMenuId().toString(), subList );
				subList.add( _menu );
			}
		}

		for (MenuUser _tmenu : list) {
			Long key = _tmenu.getMenuId();

			if(key != null)
    			if( _mapUpper.containsKey( key.toString() ) ) {
    				ArrayList<MenuUser> menuList = _mapUpper.get( key.toString());
    				Collections.sort( menuList );
    				_tmenu.setListChildMenuL( menuList );
    			}
		}

		for (MenuUser _menu : list) {
			if(_menu.getUpperMenuId() == null) {
				listTopMenu.add(_menu);
			}
		}
		Collections.sort( listTopMenu );

		retMap.put("data",listTopMenu);
		retMap.put("list",list);

	}
}
