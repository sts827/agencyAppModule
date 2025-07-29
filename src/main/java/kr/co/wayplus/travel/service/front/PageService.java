package kr.co.wayplus.travel.service.front;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.front.PageMapper;
import kr.co.wayplus.travel.model.*;

@Service
public class PageService {

    private final PageMapper mapper;

    @Autowired
    public PageService(PageMapper mapper) {
        this.mapper = mapper;
    }

    public SettingCompanyInfo getUserPageFooterInfo() {
        return mapper.selectUserPageFooterInfo();
    }

    public ArrayList<MainNoticePopup> getUserPageNoticePopupList(HashMap<String, Object> param) {
        return mapper.selectMainNoticePopupLayerList(param);
    }

    public ArrayList<MainNoticePopup>  getUserPageNoticeBarList(HashMap<String, Object> param) {
        return mapper.selectMainNoticePopupBarList(param);
    }

    public SettingNavbar getNavbar() {
        return mapper.selectNavbar();
    }

    public ArrayList<MenuUser> getUserMenuList() {
        return mapper.selectUserMenuList();
    }

//	<!--################################### Policy ###################################-->
	public Policy selectOnePolicy(HashMap<String, Object> paramMap) {
		return mapper.selectOnePolicy(paramMap);
	}

//	<!--################################### Inquiry ###################################-->
	public int selectCountInquiryContent(HashMap<String, Object> paramMap) {
		return mapper.selectCountInquiryContent(paramMap);
	}
	public ArrayList<InquiryContent> selectListInquiryContent(HashMap<String, Object> paramMap) {
		return mapper.selectListInquiryContent(paramMap);
	}
	public InquiryContent selectOneInquiryContent(HashMap<String, Object> paramMap) {
		return mapper.selectOneInquiryContent(paramMap);
	}
	public void saveInquiryContent(InquiryContent ic) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", ic.getId());

		if( this.selectCountInquiryContent(paramMap) == 0) {
			mapper.insertInquiryContent(ic);
		} else {
			mapper.updateInquiryContent(ic);
		}
	}
	public void insertInquiryContent(InquiryContent ic) throws SQLException {
		mapper.insertInquiryContent(ic);
	}
	public void updateInquiryContent(InquiryContent ic) throws SQLException {
		mapper.updateInquiryContent(ic);
	}
	public void deleteInquiryContent(InquiryContent ic) throws SQLException {
		mapper.deleteInquiryContent(ic);
	}
//	<!--################################### InquiryCategory ###################################-->
	public int selectCountInquiryCategory(HashMap<String, Object> paramMap) {
		return mapper.selectCountInquiryCategory(paramMap);
	}
	public InquiryCategory selectOneInquiryCategory(HashMap<String, Object> paramMap) {
		return mapper.selectOneInquiryCategory(paramMap);
	}
	public ArrayList<InquiryCategory> selectListInquiryCategory(HashMap<String, Object> paramMap) {
		return mapper.selectListInquiryCategory(paramMap);
	}
//	<!--################################### CodeItem ###################################-->
	public int selectCountCodeItem(HashMap<String, Object> paramMap) {
		return mapper.selectCountCodeItem(paramMap);
	}
	public int selectCountCodeItem(CodeItem ci) {
		return mapper.selectCountCodeItem(ci);
	}
	public ArrayList<CodeItem> selectListCodeItem(HashMap<String, Object> paramMap) {
		return mapper.selectListCodeItem(paramMap);
	}
	public ArrayList<CodeItem> selectListCodeItem(CodeItem ci) {
		return mapper.selectListCodeItem(ci);
	}
	public CodeItem selectOneCodeItem(HashMap<String, Object> paramMap) {
		return mapper.selectOneCodeItem(paramMap);
	}

	public ArrayList<MainBannerImage> selectListMainBannerImage(HashMap<String, Object> param) {
		return mapper.selectListMainBannerImage(param);
	}

	public ArrayList<MenuUser> selectListMenuUser(HashMap<String, Object> param) {
		return mapper.selectListMenuUser(param);
	}

	public MenuUser selectOneMenuUser(HashMap<String, Object> param) {
		return mapper.selectOneMenuUser(param);
	}
	
}
