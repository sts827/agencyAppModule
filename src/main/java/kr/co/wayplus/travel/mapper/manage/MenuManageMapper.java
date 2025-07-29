package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.wayplus.travel.model.ManageMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.MenuUser;

@Mapper
@Repository
public interface MenuManageMapper {

	Integer selectCountMenuUser(HashMap<String, Object> paramMap);

	ArrayList<MenuUser> selectListMenuUser(HashMap<String, Object> paramMap);

	void insertMenuUser(MenuUser menu) throws SQLException;

	void updateMenuUser(MenuUser menu) throws SQLException;

	void deleteMenuUser(MenuUser menu) throws SQLException;

	ArrayList<ManageMenu> selectManageMenuList(HashMap<String, Object> param);

    MenuUser selectOneMenuUser(HashMap<String,Object> paramMap);

}
