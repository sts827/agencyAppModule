package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.CodeInfo;
import kr.co.wayplus.travel.model.ManageMenu;
import kr.co.wayplus.travel.model.MenuUser;
import kr.co.wayplus.travel.model.ProductAttachFile;
import kr.co.wayplus.travel.model.ProductCategory;
import kr.co.wayplus.travel.model.ProductDetailSchedule;
import kr.co.wayplus.travel.model.ProductDetailScheduleImage;
import kr.co.wayplus.travel.model.ProductInfo;
import kr.co.wayplus.travel.model.ProductInventory;
import kr.co.wayplus.travel.model.ProductPriceOption;
import kr.co.wayplus.travel.model.ProductPriceOption.DayList;
import kr.co.wayplus.travel.model.ProductPriceOption.FixPriceList;
import kr.co.wayplus.travel.model.ProductTemplate;
import kr.co.wayplus.travel.model.ProductTemplateFile;
import kr.co.wayplus.travel.model.ProductTourImages;
import kr.co.wayplus.travel.model.ProductTourLink;
import kr.co.wayplus.travel.model.RentCarOption;

@Mapper
@Repository
public interface ManageMapper {
	ArrayList<ManageMenu> selectListManageMenu(HashMap<String, Object> paramMap);

	void insertManageMenu(ManageMenu menu) throws SQLException;

	void updateManageMenu(ManageMenu menu) throws SQLException;

	void deleteManageMenu(ManageMenu menu) throws SQLException;

	/*################################################statistics################################################*/
	ArrayList<Map<String, Object>> selectListStatisticDate(Map<String, Object> paramMap);

	Map<String, Object> selectListStatisticInfo(Map<String, Object> paramMap);

	/*################################################product################################################*/

	List<ProductInfo> selectProductList(HashMap<String, Object> paramMap);

	void insertProductAttachFile(ProductAttachFile productAt);

	void insertProductTemplate(ProductTemplate productTemplate);

	void updateProductTemplate(ProductTemplate productTemplate);

	List<ProductTemplate> selectProductTemplateList(HashMap<String, Object> paramMap);

	void deleteTemplate(HashMap<String, Object> paramMap);

	List<ProductPriceOption.DayList> selectPriceOptionList(HashMap<String, Object> paramMap);

	void insertProductPriceOption(ProductPriceOption.BasicPriceList basicPriceList);

	void insertProductPriceSet(ProductPriceOption.DayList periodPriceList);

	void insertProductBasicPriceSet(ProductPriceOption.BasicPriceList basicPriceList);

	int selectPriceSequenceNum(ProductPriceOption productPriceOption);

	List<ProductPriceOption> selectAddedPriceSetMinimumList(ProductPriceOption productPriceOption);

	List<ProductPriceOption> selectBasicPriceList(HashMap<String, Object> paramMap);
	List<ProductPriceOption> selectAddedBasicPriceList(HashMap<String, Object> paramMap);

	List<ProductPriceOption> selectAddedPriceSetList(HashMap<String, Object> paramMap);

	void deleteProductPriceSet(ProductPriceOption productPriceOption);

	void insertPriceBasicOption(ProductPriceOption.BasicPriceList basicPriceList);

	void deletePriceSet(HashMap<String, Object> paramMap);

	void deleteBasicPriceOption(HashMap<String, Object> paramMap);

	void deleteCalendarBasicPriceOption(HashMap<String, Object> paramMap);

	void insertFixPriceOption(ProductPriceOption.FixPriceList fixPriceList);

	void insertDetailSchedule(ProductDetailSchedule productDetailSchedule);

	void deleteDetailSchedule(HashMap<String, Object> paramMap);

	void insertDetailScheduleAttachFile(ProductDetailScheduleImage productDetailScheduleImage);

	void deleteDetailScheduleAttachFile(ProductDetailSchedule productDetailSchedule);

	void updateDetailScheduleImage(ProductDetailSchedule productDetailSchedule);

	List<ProductDetailSchedule> selectProductDetailScheduleBtnList(HashMap<String, Object> paramMap);

	List<ProductDetailSchedule> selectProductDetailScheduleList(HashMap<String, Object> paramMap);
	List<ProductDetailSchedule> selectProductGolfOptionList(HashMap<String, Object> paramMap);

	List<ProductDetailSchedule.ImageNumList> selectProductDetailScheduleImageList(ProductDetailSchedule productDetailSchedule);

	void updateBasicPriceOptionUseYn(HashMap<String, Object> paramMap);

	void updateProductInfo(ProductInfo productInfo);

	void deleteProductInfo(ProductInfo productInfo);

	List<ProductPriceOption> selectOneProductPriceOptionList(HashMap<String, Object> paramMap);

	List<ProductPriceOption> selectOneProductDayPriceOptionList(HashMap<String, Object> paramMap);



	/*################################################info################################################*/

	void insertProductOption(ProductPriceOption productPriceOption);

	void updateProductOption(ProductPriceOption productPriceOption);

	void deleteProductOption(ProductPriceOption productPriceOption);

	ArrayList<ProductPriceOption> selectProductOptionList(HashMap<String, Object> paramMap);

	int selectProductOptionListCount();

	ProductPriceOption selectProductOptionItem(HashMap<String, Object> paramMap);

	ArrayList<ProductCategory> selectProductCategoryList();

	void updateBasicProductOptionOrder(ProductPriceOption.BasicPriceList basicPriceList);

	void updateProductOrder(ProductInfo productInfo);

	String selectProductGroupCode(HashMap<String, Object> paramMap);

	void insertProductSlideImages(ProductTourImages productTourImages);

	void deleteProductImagesByKey(HashMap<String, Object> paramMap);

	ProductTourImages selectProductImagesByKey(HashMap<String, Object> paramMap);

	void insertProductInfo(ProductInfo info);

	ArrayList<ProductTemplate> selectTemplateList();

	//void insertProductTempInfo(ProductInfo productInfo);

	MenuUser selectManageMenuByUrl(String register);

	ArrayList<ProductTourImages> selectProductImageLists(HashMap<String, Object> param);

	ProductTemplate selectSampleTemplateById(int templateId);

	List<ProductPriceOption.FixPriceList> selectOneProductFixPriceOption(HashMap<String, Object> paramMap);

	List<ProductTourImages> selectProductInfoImageList(HashMap<String, Object> paramMap);

	ProductInfo selectProductInfo(HashMap<String, Object> paramMap);

	List<ProductCategory> selectProductSubCategoryList(HashMap<String, Object> paramMap);

	int selectProductListCount(HashMap<String, Object> paramMap);

	ProductPriceOption.FixPriceList selectOneProductFixPriceList(HashMap<String, Object> paramMap);

	ProductPriceOption.DayList selectOneProductDayPriceList(HashMap<String, Object> paramMap);

	void insertCopyProductInfo(ProductInfo productInfo);
	void insertCopyProductImages(ProductTourImages productTourImages);

	void updateCopiedProductImage(HashMap<String, Object> paramMap);

	void insertCopyProductBasicPrice(ProductPriceOption productPriceOption);

	List<ProductPriceOption.FixPriceList> selectFixPriceOptionForCopy(HashMap<String, Object> paramMap);

	void insertCopyProductFixPrice(ProductPriceOption.FixPriceList fixPriceList);

	List<ProductPriceOption.DayList> selectDayPriceOptionForCopy(HashMap<String, Object> paramMap);

	void insertCopyProductDayPrice(ProductPriceOption.DayList dayPriceList);

	void insertCopyProductDetailSchedule(ProductDetailSchedule productDetailSchedule);

	ProductDetailScheduleImage selectProductDetailScheduleImageForCopy(HashMap<String, Object> paramMap);

	void insertCopyProductDetailScheduleImage(ProductDetailScheduleImage productDetailScheduleImage);

	void updateCopiedProductDetailScheduleImage(HashMap<String, Object> paramMap);

	ArrayList<ProductCategory> selectListProductCategory(HashMap<String, Object> param);

    ArrayList<ProductCategory> selectProductCategoriesById(int categoryId);

	int updateProductUseYn(HashMap<String, Object> paramMap);

	void insertTemplateEditorFile(ProductTemplateFile productTemplateFile);

	ProductTemplateFile selectTemplateEditorImageByKey(HashMap<String, Object> paramMap);

	void deleteEditorImageByKey(HashMap<String, Object> paramMap);

	List<CodeInfo> selectOptionCodeList(HashMap<String, Object> paramMap);

	int selectOptionCodeListCount(HashMap<String, Object> paramMap);

	int selectOptionLastOrderNum();

	ProductTemplateFile selectProductTemplateFileImageByKey(HashMap<String, Object> paramMap);

	ProductTemplate selectProductTemplateById(HashMap<String, Object> paramMap);

    ArrayList<ProductCategory> selectSubCategoryListByMenuId(int productMenuId);

	ArrayList<ProductCategory> selectSubCategoryListByMenuId(HashMap<String,Object> param);


	ArrayList<MenuUser> selectManageSubMenuListById(int menu_id);

	void updateProductRegacyYn(HashMap<String, Object> paramMap);
	int selectPriceSetLastSequence();

	void updateProductDescription(ProductInfo product);

	List<ProductDetailSchedule> selectProductDetailList(HashMap<String, Object> paramMap);

	void updateProductMenuId(HashMap<String, Object> paramMap);

	List<RentCarOption> selectRentCarOptionList();

	void updateProductRentCarInfo(ProductInfo productInfo);

	List<ProductDetailSchedule> selectProductStayOptionList(HashMap<String, Object> paramMap);

	void updateProductSerial(HashMap<String, Object> paramMap);

	ArrayList<FixPriceList> selectListProductFixPrice(HashMap<String, Object> param);

	ArrayList<DayList> selectListProductDayPrice(HashMap<String, Object> param);

	void insertProductCommonCategory(ProductCategory pc) throws SQLException;

	void updateProductCommonCategory(ProductCategory pc) throws SQLException;

	void deleteProductCommonCategory(ProductCategory pc) throws SQLException;

	void resotreProductCommonCategory(ProductCategory pc) throws SQLException;

	/*################################################ProductInventory################################################*/

	int selectCountProductInventory(HashMap<String, Object> param);

    ArrayList<ProductInventory> selectListProductInventory(HashMap<String, Object> param);

    ProductInventory selectOneProductInventory(HashMap<String, Object> param);

    void insertProductInventory(ProductInventory data) throws SQLException;

    void updateProductInventory(ProductInventory data) throws SQLException;

    void deleteProductInventory(ProductInventory data) throws SQLException;

	ProductInventory deleteProductInventory(HashMap<String, Object> paramMap);

	ProductInventory selectSummaryProductInventory(HashMap<String, Object> paramMap);

/*################################################ProductInventory################################################*/
	List<ProductTourLink> selectListProductTourLink(HashMap<String, Object> param);

	void insertProductTourLink(ProductTourLink pc);

	void updateProductTourLink(ProductTourLink pc);

	void deleteProductTourLink(ProductTourLink pc);
}