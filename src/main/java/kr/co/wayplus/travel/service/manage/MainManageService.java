package kr.co.wayplus.travel.service.manage;

import kr.co.wayplus.travel.mapper.manage.MainManageMapper;
import kr.co.wayplus.travel.model.MainAttachImage;
import kr.co.wayplus.travel.model.MainNoticePopup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MainManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MainManageMapper manageMapper;

    public MainManageService(MainManageMapper manageMapper) {
        this.manageMapper = manageMapper;
    }

    public int getPopupListCount(HashMap<String, Object> param) {
       return manageMapper.selectPopupListCount(param);
    }

    public ArrayList<MainNoticePopup> getPopupList(HashMap<String, Object> param) {
        return manageMapper.selectPopupList(param);
    }

    public void writeMainAttachImage(MainAttachImage attachImage) {
        manageMapper.insertMainAttachImage(attachImage);
    }

    public void writeMainNoticePopup(MainNoticePopup noticePopup) {
        manageMapper.insertMainNoticePopup(noticePopup);
    }

    public void modifyMainNoticePopupStatus(HashMap<String, Object> param) {
        manageMapper.updateMainNoticePopupStatus(param);
    }

    public void modifyMainNoticePopupDelete(HashMap<String, Object> param) {
        manageMapper.updateMainNoticePopupDelete(param);
    }

    public MainNoticePopup getMainNoticePopup(HashMap<String, Object> param) {
        return manageMapper.selectMainNoticePopup(param);
    }

    public void modifyMainNoticePopup(MainNoticePopup noticePopup) {
        manageMapper.updateMainNoticePopup(noticePopup);
    }

    public void modifyMainNoticePopupOrder(HashMap<String, Object> param) {
        manageMapper.updateMainNoticePopupOrder(param);
    }
}
