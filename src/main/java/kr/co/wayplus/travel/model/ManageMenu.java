package kr.co.wayplus.travel.model;

import java.util.ArrayList;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManageMenu extends CommonDataSet implements Comparable<ManageMenu> {
	private Long rownum;
	private Long menuId;	//메뉴ID
	private Long upperMenuId;	//상위 메뉴ID
	private String menuType;	//메뉴 이름
	private String menuName;	//메뉴 이름
	private String menuUrl;	//메뉴URL
	private String menuIcon;	//메뉴 사용 아이콘(font athum
	private Integer menuSort;	//메뉴순서
	private String menuDesc;	//메뉴 설명
	private String menuPermission;
	private String useYn;	//숨김여부

//	private ManageMenu upperMenu;
	private ArrayList<ManageMenu> listChildMenuL; //하위 메뉴 목록(재귀)

	public ManageMenu addMenuId(Long menuId) {
		this.menuId = menuId;
		return this;
	}
	public ManageMenu addMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
		return this;
	}
	public ManageMenu addupperMenuId(Long upperMenuId) {
		this.upperMenuId = upperMenuId;
		return this;
	}
	@Override
	public int compareTo(ManageMenu menu) {
		return this.menuSort.compareTo(menu.menuSort);
	}
}
