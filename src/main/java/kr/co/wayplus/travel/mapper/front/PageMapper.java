package kr.co.wayplus.travel.mapper.front;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.*;

@Mapper
@Repository
public interface PageMapper {

    SettingCompanyInfo selectUserPageFooterInfo();

    ArrayList<MainNoticePopup> selectMainNoticePopupLayerList(HashMap<String, Object> ids);

    ArrayList<MainNoticePopup> selectMainNoticePopupBarList(HashMap<String, Object> ids);

    SettingNavbar selectNavbar();

    ArrayList<MenuUser> selectUserMenuList();
	//	<!--################################### Policy ###################################-->
	Policy selectOnePolicy(HashMap<String, Object> paramMap);


//	<!--################################### Inquiry ###################################-->
	int selectCountInquiryContent(HashMap<String, Object> paramMap);
	ArrayList<InquiryContent> selectListInquiryContent(HashMap<String, Object> paramMap);
	InquiryContent selectOneInquiryContent(HashMap<String, Object> paramMap);
	void insertInquiryContent(InquiryContent ic) throws SQLException;
	void updateInquiryContent(InquiryContent ic) throws SQLException;
	void deleteInquiryContent(InquiryContent ic) throws SQLException;
//	<!--################################### InquiryCategory ###################################-->
	int selectCountInquiryCategory(HashMap<String, Object> paramMap);
	ArrayList<InquiryCategory> selectListInquiryCategory(HashMap<String, Object> paramMap);
	InquiryCategory selectOneInquiryCategory(HashMap<String, Object> paramMap);
//	<!--################################### CodeItem ###################################-->
	int selectCountCodeItem(HashMap<String, Object> paramMap);
	int selectCountCodeItem(CodeItem ci);
	ArrayList<CodeItem> selectListCodeItem(HashMap<String, Object> paramMap);
	ArrayList<CodeItem> selectListCodeItem(CodeItem ci);
	CodeItem selectOneCodeItem(HashMap<String, Object> paramMap);

	ArrayList<MenuUser> selectListMenuUser(HashMap<String, Object> paramMap);

	ArrayList<MainBannerImage> selectListMainBannerImage(HashMap<String, Object> param);

	MenuUser selectOneMenuUser(HashMap<String, Object> param);

}
