package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategory extends CommonDataSet {

    private Integer
	    productCategoryId,
	    productMenuId,
	    sortOrder;
    private int
        menuId ,
        upperMenuId;
    private String categoryTitle
                , categoryNote
                , subcategory
                , menuName
                , menuUrl
                , menuType
                , menuSort
                , menuDesc
                , navbarYn
                , useYn
                , deleteYn
                , createId
                , createDate
                , lastUpdateId
                , lastUpdateDate;
    private int productCount;

	public ProductCategory addProductCategoryId(Integer _productCategoryId) {
		this.setProductCategoryId(_productCategoryId);
		return this;
	}
	public ProductCategory addSortOrder(Integer _sortOrder) {
		this.setSortOrder(_sortOrder);
		return this;
	}
	public ProductCategory addProductMenuId(Integer _productMenuId) {
		this.setProductMenuId(_productMenuId);
		return this;
	}
}
