package kr.co.wayplus.travel.model;

import java.util.ArrayList;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MenuUser extends CommonDataSet implements Comparable<MenuUser> {
	private Long rownum;
	private Long menuId;	//메뉴ID
	private Long upperMenuId;	//상위 메뉴ID
	private String upperMenuName;	//상위 메뉴 이름
	private String menuName;	//메뉴 이름
	private String menuAcronym;	//메뉴 이름
	private String menuUrl;	//메뉴URL
	private String menuType;	//메뉴 사용 아이콘(font athum
	private String menuSubType;	//메뉴 사용 아이콘(font athum
	private String menuSubCategory; // 하위 메뉴 카테고리
	private Integer menuSort;	//메뉴순서
	private String menuDesc;	//메뉴 설명
	private String navbarYn;	//네비게이션바 노출여부
	private String useYn;	//사용여부
	private String mainExposeYn;	//노출여부
	private String mainExposeType;	//노출코드
	private String langType;
	private int depth;

	private int subMenuCount;
	private String subMenuUrl;
	private String menuTypeName;	//노출여부
	private String menuSubTypeName;	//노출코드

//	private String deleteYn;	//삭제여부

//	private ManageMenu upperMenu;
	private ArrayList<MenuUser> listChildMenuL; //하위 메뉴 목록(재귀)

	public MenuUser addMenuId(Long menuId) {
		this.menuId = menuId;
		return this;
	}
	public MenuUser addMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
		return this;
	}
	public MenuUser addupperMenuId(Long upperMenuId) {
		this.upperMenuId = upperMenuId;
		return this;
	}
	@Override
	public int compareTo(MenuUser o) {
		return this.menuSort.compareTo(o.menuSort);
	}
}
