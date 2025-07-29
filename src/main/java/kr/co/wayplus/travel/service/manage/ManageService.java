package kr.co.wayplus.travel.service.manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wayplus.travel.mapper.manage.ManageMapper;
import kr.co.wayplus.travel.model.CodeInfo;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.ManageMenu;
import kr.co.wayplus.travel.model.MenuUser;
import kr.co.wayplus.travel.model.ProductAttachFile;
import kr.co.wayplus.travel.model.ProductCategory;
import kr.co.wayplus.travel.model.ProductDetailSchedule;
import kr.co.wayplus.travel.model.ProductDetailScheduleImage;
import kr.co.wayplus.travel.model.ProductInfo;
import kr.co.wayplus.travel.model.ProductInventory;
import kr.co.wayplus.travel.model.ProductPostJson;
import kr.co.wayplus.travel.model.ProductPriceOption;
import kr.co.wayplus.travel.model.ProductPriceOption.DayList;
import kr.co.wayplus.travel.model.ProductPriceOption.FixPriceList;
import kr.co.wayplus.travel.model.ProductTemplate;
import kr.co.wayplus.travel.model.ProductTemplateFile;
import kr.co.wayplus.travel.model.ProductTourImages;
import kr.co.wayplus.travel.model.ProductTourLink;
import kr.co.wayplus.travel.model.RentCarOption;

@Service
public class ManageService {

	@Value("${upload.file.path}")
	private String imageUploadPath;
    private final ManageMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ManageService(ManageMapper mapper) {
        this.mapper = mapper;
    }

	public ArrayList<ManageMenu> selectListManageMenu(HashMap<String, Object> paramMap) {
		return mapper.selectListManageMenu(paramMap);
	}

	public void insertManageMenu(ManageMenu menu) throws SQLException {
		mapper.insertManageMenu(menu);
	}

	public void updateManageMenu(ManageMenu menu) throws SQLException {
		mapper.updateManageMenu(menu);
	}

	public void deleteManageMenu(ManageMenu menu) throws SQLException {
		mapper.deleteManageMenu(menu);
	}

	/*################################################statistics################################################*/
	public ArrayList<Map<String, Object>> selectListStatisticDate(Map<String, Object> paramMap) {
		return mapper.selectListStatisticDate(paramMap);
	}
	public Map<String, Object> selectListStatisticInfo(Map<String, Object> paramMap) {
		return mapper.selectListStatisticInfo(paramMap);
	}

	/*################################################product################################################*/
	public List<ProductInfo> selectProductList(HashMap<String, Object> paramMap) {
		return mapper.selectProductList(paramMap);
	}

	public ArrayList<ProductCategory> getProductCategories(){
		return mapper.selectProductCategoryList();
	}

	//상품등록시 템플릿 에디터에 있는 html 부분을 이미지로 바꾸고 db에 저장하는 역할
	public void htmlImageConverter(ProductTemplate productTemplate, String userEmail) {
		if ( productTemplate.getImageByte() != null ) {

			byte[] decodedImage = Base64.getDecoder().decode(productTemplate.getImageByte().replace(' ', '+'));

			try {
				//저장 디렉토리 생성 확인
				File file = new File(imageUploadPath);
				if(!file.exists()) file.mkdirs();

				// 이미지를 저장할 경로와 파일 이름
				String uploadName = UUID.randomUUID().toString();
				String filePath = imageUploadPath + uploadName;

				// 이미지 저장
				FileOutputStream fos = new FileOutputStream(filePath);
				fos.write(decodedImage);
				fos.close();

				File savedFile = new File(filePath);

				ProductAttachFile productAttachFile = new ProductAttachFile();
				productAttachFile.setContentId(productTemplate.getTemplateId());
				productAttachFile.setContentType("template");
				productAttachFile.setUploadPath(imageUploadPath);
				productAttachFile.setUploadFilename(uploadName);
//				TODO : (상품등록기능이 완료될시)상품이름 <- 설정하기
				productAttachFile.setOriginFilename("(상품등록기능이 완료될시)상품이름"+"html-image");
				productAttachFile.setFileSize((int) savedFile.length());
				productAttachFile.setFileMimetype("");
				productAttachFile.setFileExtension("");
				productAttachFile.setUploadId(userEmail);

				mapper.insertProductAttachFile(productAttachFile);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//템플릿 등록
	public void insertProductTemplate(ProductTemplate productTemplate) {
		mapper.insertProductTemplate(productTemplate);

		// product-description정보 업데이트
		ProductInfo product = new ProductInfo();
		product.setProductTourId(productTemplate.getProductTourId());
		product.setProductDescription(productTemplate.getTemplateHtml());
		product.setCreateId(productTemplate.getCreateId());
		mapper.updateProductDescription(product);
	}
	public void updateProductTemplate(ProductTemplate productTemplate){
		mapper.updateProductTemplate(productTemplate);
	}

	public void updateProductDescription(ProductInfo productInfo){
		mapper.updateProductDescription(productInfo);
	}


	//템플릿 리스트
	public List<ProductTemplate> selectProductTemplateList(HashMap<String, Object> paramMap) {
		return mapper.selectProductTemplateList(paramMap);
	}

	//템플릭 삭제
	public void deleteTemplate(HashMap<String, Object> paramMap) {
		mapper.deleteTemplate(paramMap);
	}

	//상품 가격 옵션 리스트
	public List<ProductPriceOption.DayList> selectPriceOptionList(HashMap<String, Object> paramMap) {
		return mapper.selectPriceOptionList(paramMap);
	}

	//상품 일정 등록
	@Transactional
	public HashMap<String, Object> insertProductSchedule(LoginUser loginUser, ProductPriceOption productPriceOption) {
		HashMap<String, Object> resultMap = new HashMap<>();

		List<Integer> deleteBasicId = productPriceOption.getDeleteBasicPriceOptionList();
		if ( deleteBasicId != null && deleteBasicId.size() > 0 ) {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("createId", loginUser.getUserEmail());
			paramMap.put("deleteBasicPriceOptionList", productPriceOption.getDeleteBasicPriceOptionList());
			mapper.deleteCalendarBasicPriceOption(paramMap);
		}

		productPriceOption.setCreateId(loginUser.getUserEmail());
		if ( !productPriceOption.getPriceSequence().equals("0") ) {
			mapper.deleteProductPriceSet(productPriceOption);
		}
		int priceSequence = mapper.selectPriceSequenceNum(productPriceOption) + 1;

		List<Integer> priceIdList = new ArrayList<>();
		List<Integer> priceOptionIdList = new ArrayList<>();
		for ( ProductPriceOption.BasicPriceList basicPriceList : productPriceOption.getBasicPriceList() ) {
			basicPriceList.setCreateId(loginUser.getUserEmail());
			mapper.insertProductPriceOption(basicPriceList);
			priceOptionIdList.add(basicPriceList.getPriceOptionId());


			String basicOptionName = basicPriceList.getOptionName();
			Map<Integer, List<ProductPriceOption.DayList>> periodPriceOptions = productPriceOption.getDayList();
			if (periodPriceOptions != null) {
				for (Map.Entry<Integer, List<ProductPriceOption.DayList>> entry : periodPriceOptions.entrySet()) {
					List<ProductPriceOption.DayList> dayList = entry.getValue();
					for (ProductPriceOption.DayList dayListItem : dayList) {
						if ( dayListItem.getOptionName().equals(basicOptionName) ) {
							dayListItem.setCreateId(loginUser.getUserEmail());
							dayListItem.setPriceOptionId(basicPriceList.getPriceOptionId());
							if (dayListItem.getPriceSequence() == 0) {
								dayListItem.setPriceSequence(priceSequence);
							}
							dayListItem.setPriceSale(0);
							mapper.insertProductPriceSet(dayListItem);
							priceIdList.add(dayListItem.getPriceId());
						}
					}
				}
			}
		}

		resultMap.put("priceIdList", priceIdList);
		resultMap.put("basicPriceIdList", priceOptionIdList);

		return resultMap;
	}

	//저장된 상품 가격 정보 리스트(최저값만)
	public List<ProductPriceOption> selectAddedPriceSetMinimumList(ProductPriceOption productPriceOption) {
		return mapper.selectAddedPriceSetMinimumList(productPriceOption);
	}

	//저장된 상품 기본가격 정보 리스트
	public List<ProductPriceOption> selectAddedBasicPriceList(HashMap<String, Object> paramMap) {
		return mapper.selectAddedBasicPriceList(paramMap);
	}

	//저장된 상품 가격 정보 리스트
	public List<ProductPriceOption> selectAddedPriceSetList(HashMap<String, Object> paramMap) {
		return mapper.selectAddedPriceSetList(paramMap);
	}

	//일정 수정시 상품판매 기본가격 정보 생성
	public void insertPriceBasicOption(ProductPriceOption.BasicPriceList basicPriceList) {
		mapper.insertPriceBasicOption(basicPriceList);
	}

	//일정 삭제
	@Transactional
	public void deletePriceSetService(HashMap<String, Object> paramMap) {
		mapper.deleteBasicPriceOption(paramMap);
		mapper.deletePriceSet(paramMap);
	}

	public void insertProductInfoWithProductSerial(ProductInfo productInfo){
		// 임시 저장
		validationDateTimeForProduct(productInfo);
		productInfo.setProductSerial("0");
		mapper.insertProductInfo(productInfo);

		HashMap<String, Object> paramMap = new HashMap<>();
		String generateProductSerialCode = generateProductSerial(String.valueOf(productInfo.getProductTourId()));
		paramMap.put("productSerial", generateProductSerialCode);
		paramMap.put("creatId", productInfo.getCreateId());
		paramMap.put("productTourId", productInfo.getProductTourId());
		mapper.updateProductSerial(paramMap);
	}
	public void insertProductInfo(ProductInfo productInfo){
		validationDateTimeForProduct(productInfo);
		mapper.insertProductInfo(productInfo);
	}

	public void updateProductInfo(ProductInfo productInfo){
		validationDateTimeForProduct(productInfo);
		mapper.updateProductInfo(productInfo);
	}

	public void deleteProductInfo(ProductInfo productInfo){
		mapper.deleteProductInfo(productInfo);
	}

	public MenuUser selectManageMenuByUrl(String url){
		return mapper.selectManageMenuByUrl(url);
	}

	@Transactional
	public int 	updateBasicPriceOptionUseYnService(LoginUser loginUser, ProductPostJson productPostJson) throws SQLException{
		//상품등록완료 되었을때 기본가격정보 use_y처리
		if ( productPostJson.getPriceOptionList().getAddBasicOptionId() != null &&
			!productPostJson.getPriceOptionList().getAddBasicOptionId().isEmpty() ) {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("createId", loginUser.getUserEmail());
			//마지막 "," 제거용
			StringBuilder builder = new StringBuilder(productPostJson.getPriceOptionList().getAddBasicOptionId());
			if (builder.length() > 0 && builder.charAt(builder.length() - 1) == ',') {
				builder.deleteCharAt(builder.length() - 1);
			}
			String modified = builder.toString();
			String[] basicPriceId = modified.split(",");

			paramMap.put("createId", loginUser.getUserEmail());
			paramMap.put("basicPriceId", basicPriceId);
			mapper.updateBasicPriceOptionUseYn(paramMap);
		}

		//상품고정가격 등록
		if ( productPostJson.getPriceOptionList().getFixPriceList() != null &&
			 productPostJson.getPriceOptionList().getFixPriceList().size() > 0 ) {
			this.insertFixPriceOption(loginUser, productPostJson.getPriceOptionList().getFixPriceList());
		}

		if ( productPostJson.getPriceOptionList().getDeleteBasicPriceOptionList() != null &&
			 productPostJson.getPriceOptionList().getDeleteBasicPriceOptionList().size() > 0 ) {
			HashMap<String, Object> basicParamMap = new HashMap<>();
			basicParamMap.put("createId", loginUser.getUserEmail());
			basicParamMap.put("deleteBasicPriceOptionList", productPostJson.getPriceOptionList().getDeleteBasicPriceOptionList());
			mapper.deleteCalendarBasicPriceOption(basicParamMap);
		}

		int productTourId = productPostJson.getProductInfo().getProductTourId();

		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("productTourId", productPostJson.getProductInfo().getProductTourId());
		paramMap.put("createId", loginUser.getUserEmail());

		if ( productPostJson.getProductInfo().getRegisterType().equals("modify") ) {
			paramMap.put("type", "modify");
			//새로운 상품으로 복사
			productTourId = copyProductService(paramMap);
			//기존 상품 relacy_Y처리
			mapper.updateProductRegacyYn(paramMap);
		}

		if ( productPostJson.getProductInfo() != null &&
			 productPostJson.getProductInfo().getProductMenuId() != 0 ) {
			HashMap<String, Object> updateParamMap = new HashMap<>();
			updateParamMap.put("productTourId", productTourId);
			updateParamMap.put("createId", loginUser.getUserEmail());
			updateParamMap.put("productMenuId", productPostJson.getProductInfo().getProductMenuId());
			//골프상품 타입 업데이트
			this.updateProductMenuId(updateParamMap);
		}

		return productTourId;
	}

	@Transactional
	public int addProductDetailScheduleService(LoginUser loginUser, ProductPostJson productPostJson){
		//세부일정 등록
		HashMap<String, Object> paramMap = new HashMap<>();
		int productTourId = productPostJson.getDetailScheduleList().get(0).getProductTourId();
		paramMap.put("productTourId", productPostJson.getDetailScheduleList().get(0).getProductTourId());
		paramMap.put("createId", loginUser.getUserEmail());
		if ( productPostJson.getDetailScheduleList().size() > 0 ) {
			mapper.deleteDetailSchedule(paramMap);
			System.out.println("@@@@@@@ " + productPostJson.getDetailScheduleList().get(0).getDetailType());
			if ( !productPostJson.getDetailScheduleList().get(0).getDetailType().equals("notUse") ) {
				for (ProductDetailSchedule productDetailSchedule : productPostJson.getDetailScheduleList()){
					productDetailSchedule.setCreateId(loginUser.getUserEmail());
					productDetailSchedule.parseAndSetAttachImageNums();
					mapper.insertDetailSchedule(productDetailSchedule);
					mapper.deleteDetailScheduleAttachFile(productDetailSchedule);
					if ( productDetailSchedule.getAttachImageNums().length > 0 ) {
						mapper.updateDetailScheduleImage(productDetailSchedule);
					}
				}
			}
		}
		if ( productPostJson.getDetailScheduleList().get(0).getRegisterType() != null &&
				productPostJson.getDetailScheduleList().get(0).getRegisterType().equals("modify") ) {
			paramMap.put("type", "modify");
			//새로운 상품으로 복사
			productTourId = copyProductService(paramMap);
			//기존 상품 relacy_Y처리
			mapper.updateProductRegacyYn(paramMap);
		}

		if ( productPostJson.getProductInfo() != null && productPostJson.getProductInfo().getProductMenuId() != 0 ) {
			HashMap<String, Object> updateParamMap = new HashMap<>();
			updateParamMap.put("productTourId", productTourId);
			updateParamMap.put("createId", loginUser.getUserEmail());
			updateParamMap.put("productMenuId", productPostJson.getProductInfo().getProductMenuId());
			//골프상품 타입 업데이트
			this.updateProductMenuId(updateParamMap);
		}


		return productTourId;
	}

	public void insertDetailScheduleAttachFile(ProductDetailScheduleImage productDetailScheduleImage) {
		mapper.insertDetailScheduleAttachFile(productDetailScheduleImage);
	}

	public void writeProductSlideImages(ProductTourImages productTourImages){
		mapper.insertProductSlideImages(productTourImages);
	}
	public void uploadEditorImage(ProductTemplateFile productTemplateFile){
		mapper.insertTemplateEditorFile(productTemplateFile);
	}

	public ProductTourImages getProductImagesByKey(HashMap<String, Object> paramMap) {
//		return mapper.deleteProductImagesByKey(paramMap);
		return mapper.selectProductImagesByKey(paramMap);
	}

	public void deleteProductImageByKey(HashMap<String, Object> paramMap){
		mapper.deleteProductImagesByKey(paramMap);
	}

	public void deleteDetailScheduleAttachFile(ProductDetailSchedule productDetailSchedule) {
		mapper.deleteDetailScheduleAttachFile(productDetailSchedule);
	}

	public void updateDetailScheduleImage(ProductDetailSchedule productDetailSchedule) {
		mapper.updateDetailScheduleImage(productDetailSchedule);
	}

	public List<ProductDetailSchedule> selectProductDetailScheduleBtnList(HashMap<String, Object> paramMap) {
		return mapper.selectProductDetailScheduleBtnList(paramMap);
	}

	public List<ProductDetailSchedule> selectProductDetailScheduleList(HashMap<String, Object> paramMap) {
		return mapper.selectProductDetailScheduleList(paramMap);
	}

	public List<ProductDetailSchedule.ImageNumList> selectProductDetailScheduleImageList(ProductDetailSchedule productDetailSchedule) {
		return mapper.selectProductDetailScheduleImageList(productDetailSchedule);
	}

	@Transactional
	public void insertFixPriceOption(LoginUser loginUser, List<ProductPriceOption.FixPriceList> fixPriceList) throws SQLException {
		//기존에 등록되어 있던 기본가격 N처리
		for (ProductPriceOption.FixPriceList item : fixPriceList) {
				ProductPriceOption.BasicPriceList basicPriceList = new ProductPriceOption.BasicPriceList();
				if ( item != null ) {
					basicPriceList.setPriceOptionId(item.getPriceOptionId());
				}
				basicPriceList.setProductTourId(item.getProductTourId());
				basicPriceList.setCreateId(loginUser.getUserEmail());
				basicPriceList.setPriceSequence(0);
				basicPriceList.setOptionName(item.getOptionName());
				basicPriceList.setMaxCapacity(item.getMaxCapacity());
				basicPriceList.setMaxQuantity(item.getMaxQuantity());
				basicPriceList.setOptionGroupCode(item.getOptionGroupCode());
				mapper.insertProductPriceOption(basicPriceList);
				item.setCreateId(loginUser.getUserEmail());
				item.setPriceOptionId(basicPriceList.getPriceOptionId());
				mapper.insertFixPriceOption(item);
		}

	}

	public List<ProductPriceOption> selectOneProductPriceOptionList(HashMap<String, Object> paramMap) {
		return mapper.selectOneProductPriceOptionList(paramMap);
	}

	public List<ProductPriceOption> selectOneProductDayPriceOptionList(HashMap<String, Object> paramMap) {
		return mapper.selectOneProductDayPriceOptionList(paramMap);
	}

	public List<ProductPriceOption.FixPriceList> selectOneProductFixPriceOption(HashMap<String, Object> paramMap) {
		return mapper.selectOneProductFixPriceOption(paramMap);
	}

	public List<ProductTourImages> selectProductInfoImageList(HashMap<String, Object> paramMap) {
		return mapper.selectProductInfoImageList(paramMap);
	}

	/*#######################################info####################################################*/

	public void insertProductOption(ProductPriceOption productPriceOption) {
		//마지막 순서 값을 가져옴
		int lastOptionOrder = mapper.selectOptionLastOrderNum();
		productPriceOption.setOptionOrder(lastOptionOrder+1);
		mapper.insertProductOption(productPriceOption);
	}

	public void updateProductOption(ProductPriceOption productPriceOption) {
		mapper.updateProductOption(productPriceOption);
	}

	public void deleteProductOption(ProductPriceOption productPriceOption) {
		mapper.deleteProductOption(productPriceOption);
	}

	public ArrayList<ProductPriceOption> selectProductOptionList(HashMap<String,Object> paramMap) {
		return mapper.selectProductOptionList(paramMap);
	}

	public int selectProductOptionListCount() {
		return mapper.selectProductOptionListCount();
	}

	public ProductPriceOption selectProductOptionItem(HashMap<String, Object> paramMap) {
		return mapper.selectProductOptionItem(paramMap);
	}

	public List<ProductCategory> selectProductCategoryList() {
		return mapper.selectProductCategoryList();
	}

	public HashMap<String, Object> selectListProductCategoryByMenuId(){
		HashMap<String, Object> map = new HashMap<String, Object>();

		List<ProductCategory> list = this.selectProductCategoryList();

		for (ProductCategory pc : list) {
			ArrayList<ProductCategory> _list = null;

			String key = String.valueOf( pc.getProductMenuId());

			if(map.containsKey( key )) {
				_list = (ArrayList<ProductCategory>) map.get( key );
			} else {
				_list = new ArrayList<ProductCategory>();
			}

			_list.add(pc);
			map.put( key, _list );

		}

		return map;
	}

	public void updateBasicProductOptionOrder(ProductPriceOption.BasicPriceList basicPriceList) {
		mapper.updateBasicProductOptionOrder(basicPriceList);
	}

	public void updateProductOrder(ProductInfo productInfo) {
		mapper.updateProductOrder(productInfo);
	}

	public String selectProductGroupCode(HashMap<String, Object> paramMap) {
		return mapper.selectProductGroupCode(paramMap);
	}


	public void selectList(HashMap<String,Object> paramMap, HashMap<String, Object> retMap) throws Exception {
//		HashMap<String,Object> paramMap = new HashMap<String, Object>();

		ArrayList<ManageMenu> listTopMenu = new ArrayList<ManageMenu>(); /*최상위 메뉴 뽑기*/
		HashMap<String,ManageMenu> _map = new HashMap<String, ManageMenu>(); /*색인용 Map*/
		HashMap<String,ArrayList<ManageMenu>> _mapUpper = new HashMap<String, ArrayList<ManageMenu>>(); /*상위 메뉴 색인용 Map*/
		ArrayList<ManageMenu> list = this.selectListManageMenu(paramMap);

		for (ManageMenu _menu : list) {
			_map.put(_menu.getMenuId().toString(), _menu);

			if(_menu.getUpperMenuId() != null) {
				ArrayList<ManageMenu> subList = null;
				if( _mapUpper.containsKey(_menu.getUpperMenuId().toString()) ) {
					subList = _mapUpper.get( _menu.getUpperMenuId().toString() );
				} else {
					subList = new ArrayList<ManageMenu>();
				}
				_mapUpper.put( _menu.getUpperMenuId().toString(), subList );
				subList.add( _menu );
			}
		}

		for (ManageMenu _tmenu : list) {
			Long key = _tmenu.getMenuId();

			if(key != null)
    			if( _mapUpper.containsKey( key.toString() ) ) {
    				ArrayList<ManageMenu> menuList = _mapUpper.get( key.toString());
    				Collections.sort( menuList );
    				_tmenu.setListChildMenuL( menuList );
    			}
		}

		for (ManageMenu _menu : list) {
			if(_menu.getUpperMenuId() == null) {
				listTopMenu.add(_menu);
			}
		}
		Collections.sort( listTopMenu );

		retMap.put("data",listTopMenu);
		retMap.put("list",list);

	}

	public ArrayList<ProductTemplate> getTemplateList() throws Exception{
		return mapper.selectTemplateList();
	}

	public void validationDateTimeForProduct(ProductInfo product){

		if(product.getProductShowStartDate()!=null && !product.getProductShowStartDate().trim().equals("")){
			product.setProductShowStartDate(product.getProductShowStartDate());
		}else{
			product.setProductShowStartDate(null);
		}
		if(product.getProductShowEndDate()!=null && !product.getProductShowEndDate().trim().equals("")){
			product.setProductShowEndDate(product.getProductShowEndDate());
		}else{
			product.setProductShowEndDate(null);
		}
		if(product.getProductReservStartDate()!=null && !product.getProductReservStartDate().trim().equals("")){
			product.setProductReservStartDate(product.getProductReservStartDate());
		}else{
			product.setProductReservStartDate(null);
		}
		if(product.getProductReservEndDate()!=null && !product.getProductReservEndDate().trim().equals("")){
			product.setProductReservEndDate(product.getProductReservEndDate());
		}else{
			product.setProductReservEndDate(null);
		}
	}

	public ArrayList<ProductTourImages> getProductImagesById(HashMap<String, Object> param) {
		return mapper.selectProductImageLists(param);
	}

	public ProductTemplate getSampleTemplateById(int templateId) {
		return mapper.selectSampleTemplateById(templateId);
	}

	public ProductInfo selectProductInfo(HashMap<String, Object> paramMap) {
		return mapper.selectProductInfo(paramMap);
	}

	public List<ProductCategory> selectProductSubCategoryList(HashMap<String, Object> paramMap) {
		return mapper.selectProductSubCategoryList(paramMap);
	}

	public int getProductListCount(HashMap<String, Object> paramMap) {
		return mapper.selectProductListCount(paramMap);
	}

	public ProductPriceOption.FixPriceList selectOneProductFixPriceList(HashMap<String, Object> paramMap) {
		return mapper.selectOneProductFixPriceList(paramMap);
	}

	public ProductPriceOption.DayList selectOneProductDayPriceList(HashMap<String, Object> paramMap) {
		return mapper.selectOneProductDayPriceList(paramMap);
	}

	@Transactional
	public int copyProductService(HashMap<String, Object> paramMap) {
		/***기본,상품정보 복사***/
		List<ProductInfo> item = selectProductList(paramMap);
		ProductInfo productInfo = item.get(0);
		//상품등록시간 수정
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		productInfo.setCreateDate(dateFormat.format(currentDate));

		if ( paramMap.get("type") != null && paramMap.get("type").equals("modify") ) {
			productInfo.setProductUseYn("Y");
			productInfo.setProductStatus("S");
		}
		else {
			productInfo.setProductTitle(item.get(0).getProductTitle()+"_복사본");
			productInfo.setProductUseYn("N");
		}
		mapper.insertCopyProductInfo(productInfo);
		int productTourId = productInfo.getProductTourId();
		//상품 복사시
		if ( paramMap.get("type").equals("copy") ) {
			HashMap<String, Object> SerialParamMap = new HashMap<>();
			String generateProductSerialCode = generateProductSerial(String.valueOf(productTourId));
			SerialParamMap.put("productSerial", generateProductSerialCode);
			SerialParamMap.put("creatId", productInfo.getCreateId());
			SerialParamMap.put("productTourId", productTourId);
			mapper.updateProductSerial(SerialParamMap);
		}
		//이미지 복사
		if ( productInfo.getProductImages() != null ) {
			String[] images = productInfo.getProductImages().split(",");
			List<Integer> copiedImageNumList = new ArrayList<>();
			for ( String imageItem : images ) {
				paramMap.put("imageId", imageItem);
				ProductTourImages productTourImages = mapper.selectProductImagesByKey(paramMap);
				productTourImages.setProductTourId(productTourId);
				mapper.insertCopyProductImages(productTourImages);
				copiedImageNumList.add(productTourImages.getImageId());
			}
			//복사된 상품과 복사된 이미지 번호 매칭
			if ( copiedImageNumList.size() > 0 ) {
				StringBuilder productImages = new StringBuilder();
				for ( Integer copiedImageNum : copiedImageNumList ) {
					productImages.append(copiedImageNum).append(",");
				}
				paramMap.put("productImages", productImages.toString());
				paramMap.put("copiedProductTourId", productTourId);
				mapper.updateCopiedProductImage(paramMap);
			}
		}

		/***판매가격 복사***/
		//판매 기본가격복사
		List<ProductPriceOption> basicPriceList = mapper.selectBasicPriceList(paramMap);
		String groupCode = generateRandomCode(8);
		int priceSequence = 0;
		for ( ProductPriceOption basicPrice : basicPriceList ) {
			int lastPriceSequence = mapper.selectPriceSetLastSequence();
			paramMap.put("priceOptionId", basicPrice.getPriceOptionId());
			basicPrice.setProductTourId(productTourId);
			basicPrice.setOptionGroupCode(groupCode);
			mapper.insertCopyProductBasicPrice(basicPrice);
			int copiedPriceOptionId = basicPrice.getPriceOptionId();
			//판매 고정가격복사
			List<ProductPriceOption.FixPriceList> fixPriceList = mapper.selectFixPriceOptionForCopy(paramMap);
			for ( ProductPriceOption.FixPriceList fixPriceItem : fixPriceList ) {
				fixPriceItem.setProductTourId(productTourId);
				fixPriceItem.setCopiedPriceOptionId(copiedPriceOptionId);
				fixPriceItem.setOptionGroupCode(groupCode);
				mapper.insertCopyProductFixPrice(fixPriceItem);
			}
			//판매 요일별가격복사
			List<ProductPriceOption.DayList> dayPriceList = mapper.selectDayPriceOptionForCopy(paramMap);
			for ( ProductPriceOption.DayList dayPrice : dayPriceList ) {
				dayPrice.setProductTourId(productTourId);
				dayPrice.setCopiedPriceOptionId(copiedPriceOptionId);
				dayPrice.setOptionGroupCode(groupCode);
				//가격 아이템이 같은 그룹인지 체크
				if ( priceSequence != dayPrice.getPriceSequence() ) {
					priceSequence = dayPrice.getPriceSequence();
					lastPriceSequence += 1;
					dayPrice.setPriceSequence(lastPriceSequence);
				}
				else {
					dayPrice.setPriceSequence(lastPriceSequence);
				}
				mapper.insertCopyProductDayPrice(dayPrice);
			}
		}

		/***세부일정 복사***/
		List<ProductDetailSchedule> productDetailScheduleList = mapper.selectProductDetailScheduleList(paramMap);
		this.copyProductDetailInfo(productDetailScheduleList, paramMap, productTourId);

		/***골프 옵션 정보 복사***/
		List<ProductDetailSchedule> productGolfOptionList = mapper.selectProductGolfOptionList(paramMap);
		this.copyProductDetailInfo(productGolfOptionList, paramMap, productTourId);

		/***객실 정보 복사***/
		List<ProductDetailSchedule> productStayOptionList = mapper.selectProductStayOptionList(paramMap);
		this.copyProductDetailInfo(productStayOptionList, paramMap, productTourId);

		return productTourId;

	}

	public ArrayList<ProductCategory> getProductCategoriesById(int categoryId) {
		return mapper.selectProductCategoriesById(categoryId);
	}

	public ArrayList<ProductCategory> selectListProductCategory(HashMap<String, Object> param) {
		return mapper.selectListProductCategory(param);
	}

	public int updateProductUseYn(HashMap<String, Object> paramMap) {
		return mapper.updateProductUseYn(paramMap);
	}

	public ProductTemplateFile getTemplateEditorImageByKey(HashMap<String, Object> paramMap) {
		return mapper.selectTemplateEditorImageByKey(paramMap);
	}

	public void deleteTemplateEditorImageByKey(HashMap<String, Object> paramMap) {
		mapper.deleteEditorImageByKey(paramMap);
	}

	public List<CodeInfo> selectOptionCodeList(HashMap<String, Object> paramMap) {
		return mapper.selectOptionCodeList(paramMap);
	}

	public int selectOptionCodeListCount(HashMap<String, Object> paramMap) {
		return mapper.selectOptionCodeListCount(paramMap);
	}

	public int selectOptionLastOrderNum() {
		return mapper.selectOptionLastOrderNum();
	}

	public ProductTemplateFile getProductTemplateFileImageByKey(HashMap<String, Object> paramMap) {
		return mapper.selectProductTemplateFileImageByKey(paramMap);
	}

	public ProductTemplate selectProductTemplateById(HashMap<String, Object> paramMap) {
		return mapper.selectProductTemplateById(paramMap);
	}

	public ArrayList<ProductCategory> getSubCategoryListByMenuId(int productMenuId) {
		return mapper.selectSubCategoryListByMenuId(productMenuId);
	}

	@Transactional
	public int updateAndCopyProductInfo(ProductInfo productInfo) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("productTourId", productInfo.getProductTourId());
		paramMap.put("createId", productInfo.getCreateId());
		paramMap.put("type", "modify");
//		mapper.updateProductInfo(productInfo);
		//새로운 상품으로 복사
		int productTourId = copyProductService(paramMap);
		//기존 상품 relacy_Y처리
		mapper.updateProductRegacyYn(paramMap);

		return productTourId;
	}

	public String generateRandomCode(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			result.append(randomChar);
		}

		return result.toString();
	}

//	public ArrayList<ProductCategory> getSubCategoryListByMenuId(HashMap<String,Object> param) {
//
//		param.get("menuSubCategory");
//
//
//		return mapper.selectSubCategoryListByMenuId(param);
//	}


	public ArrayList<MenuUser> getManageSubMenuListById(int menu_id) {
		return mapper.selectManageSubMenuListById(menu_id);
	}

	public List<ProductDetailSchedule> selectProductDetailList(HashMap<String, Object> paramMap) {
		return mapper.selectProductDetailList(paramMap);
	}

//	public List<ProductDetailSchedule> selectProductDetailList(HashMap<String, Object> paramMap) {
//		return mapper.selectProductDetailList(paramMap);
//	}

	public void copyProductDetailInfo(List<ProductDetailSchedule> listItem, HashMap<String, Object> paramMap, int productTourId) {
		for ( ProductDetailSchedule productDetailSchedule : listItem ) {
			productDetailSchedule.setProductTourId(productTourId);
			mapper.insertCopyProductDetailSchedule(productDetailSchedule);
			String copiedProductDetailId = String.valueOf(productDetailSchedule.getDetailId());
			//이미지 복사
			String[] scheduleImages = productDetailSchedule.getAttachImage().split(",");
			List<Integer> copiedScheduleImageNumList = new ArrayList<>();
			for ( String imageItem : scheduleImages ) {
				if ( !imageItem.isEmpty() ) {
					paramMap.put("detailImageId", imageItem);
					ProductDetailScheduleImage copyImage = mapper.selectProductDetailScheduleImageForCopy(paramMap);
					copyImage.setDetailId(productDetailSchedule.getDetailId());
					mapper.insertCopyProductDetailScheduleImage(copyImage);
					copiedScheduleImageNumList.add(copyImage.getDetailImageId());
				}
			}
			//복사된 세부일정과 복사된 이미지 번호 매칭
			if ( copiedScheduleImageNumList.size() > 0 ) {
				StringBuilder matchScheduleImages = new StringBuilder();
				for ( Integer copiedScheduleImageNum : copiedScheduleImageNumList ) {
					matchScheduleImages.append(",").append(copiedScheduleImageNum);
				}
				paramMap.put("attachImages", matchScheduleImages.toString());
				paramMap.put("detailId", copiedProductDetailId);
				mapper.updateCopiedProductDetailScheduleImage(paramMap);
			}
		}
	}

	public void updateProductMenuId(HashMap<String, Object> paramMap) {
		mapper.updateProductMenuId(paramMap);
	}

	public List<RentCarOption> selectRentCarOptionList() {
		return mapper.selectRentCarOptionList();
	}

	@Transactional
	public int updateProductRentCarInfoService(ProductInfo productInfo) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("productTourId", productInfo.getProductTourId());
		paramMap.put("createId", productInfo.getCreateId());
		paramMap.put("type", "modify");

		mapper.updateProductRentCarInfo(productInfo);
//		mapper.updateProductInfo(productInfo);
		//새로운 상품으로 복사
		int productTourId = copyProductService(paramMap);
		//기존 상품 relacy_Y처리
		mapper.updateProductRegacyYn(paramMap);

		return productTourId;
	}

	public String generateProductSerial(String productTourId) {
		// 현재 날짜를 가져옵니다.
		Date currentDate = new Date();

		// 년, 월, 일을 3자리 숫자 형식으로 포맷합니다.
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyy");
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

		String year = yearFormat.format(currentDate);
		String month = monthFormat.format(currentDate);
		String day = dayFormat.format(currentDate);

		// 3자리 숫자를 조합하여 한 문장을 생성합니다.
		StringBuilder sb = new StringBuilder();
		sb.append(year);
		sb.append(month);
		sb.append(day);
		sb.append("-");
		sb.append(productTourId);

		return sb.toString();
	}

	public ArrayList<FixPriceList> selectListProductFixPrice(HashMap<String, Object> param) {
		return mapper.selectListProductFixPrice(param);
	}

	public ArrayList<DayList> selectListProductDayPrice(HashMap<String, Object> param) {
		return mapper.selectListProductDayPrice(param);
	}

	public void saveProductCommonCategory(ProductCategory pc) throws SQLException {
		if(pc == null) {
			throw new SQLException("model is null");
		} else {
			if( pc.getProductCategoryId() == null ) {
				this.insertProductCommonCategory(pc);
			} else {
				this.updateProductCommonCategory(pc);
			}
		}
	}

	public void insertProductCommonCategory(ProductCategory pc) throws SQLException{
		mapper.insertProductCommonCategory(pc);
	}

	public void updateProductCommonCategory(ProductCategory pc) throws SQLException{
		mapper.updateProductCommonCategory(pc);
	}

	public void deleteProductCommonCategory(ProductCategory pc)  throws SQLException{
		mapper.deleteProductCommonCategory(pc);
	}

	public void resotreProductCommonCategory(ProductCategory pc)  throws SQLException {
		mapper.resotreProductCommonCategory(pc);
	}



	/*################################################ProductInventory################################################*/

	public Integer selectCountProductInventory(HashMap<String, Object> paramMap) {
		return mapper.selectCountProductInventory(paramMap);
	}

	public ArrayList<ProductInventory> selectListProductInventory(HashMap<String, Object> paramMap) {
		return mapper.selectListProductInventory(paramMap);
	}

	public ProductInventory selectOneProductInventory(HashMap<String, Object> paramMap) {
		return mapper.selectOneProductInventory(paramMap);
	}

	public void insertProductInventory(ProductInventory data) throws SQLException {
		mapper.insertProductInventory(data);
	}

	public void updateProductInventory(ProductInventory data) throws SQLException {
		mapper.updateProductInventory(data);
	}

	public void deleteProductInventory(ProductInventory data) throws SQLException {
		mapper.deleteProductInventory(data);
	}

	public ProductInventory selectSummaryProductInventory(HashMap<String, Object> paramMap) {
		return mapper.selectSummaryProductInventory(paramMap);
	}

	/*################################################ProductTourLink################################################*/
	public List<ProductTourLink> selectListProductTourLink(HashMap<String, Object> param) {
		return mapper.selectListProductTourLink(param);
	}

	@Transactional
	public void saveProductTourLink(LoginUser loginUser, ProductPostJson productPostJson) throws SQLException{
		//세부일정 등록

		List<ProductTourLink> list = productPostJson.getProductTourLinkList();

		for (ProductTourLink item : list) {

			switch( item.getStatusType().toUpperCase() ) {
				case "N":
					this.insertProductTourLink( item );
					break;
				case "U":
					this.updateProductTourLink( item );
					break;
				case "D":
					this.deleteProductTourLink( item );
					break;
			}

		}
	}

	public void insertProductTourLink(ProductTourLink pc) throws SQLException{
		mapper.insertProductTourLink(pc);
	}

	public void updateProductTourLink(ProductTourLink pc) throws SQLException{
		mapper.updateProductTourLink(pc);
	}

	public void deleteProductTourLink(ProductTourLink pc)  throws SQLException{
		mapper.deleteProductTourLink(pc);
	}
}
