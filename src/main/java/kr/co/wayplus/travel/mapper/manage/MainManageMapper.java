package kr.co.wayplus.travel.mapper.manage;

import kr.co.wayplus.travel.model.MainAttachImage;
import kr.co.wayplus.travel.model.MainNoticePopup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface MainManageMapper {

    int selectPopupListCount(HashMap<String, Object> param);

    ArrayList<MainNoticePopup> selectPopupList(HashMap<String, Object> param);

    void insertMainAttachImage(MainAttachImage attachImage);

    void insertMainNoticePopup(MainNoticePopup noticePopup);

    void updateMainNoticePopupStatus(HashMap<String, Object> param);

    void updateMainNoticePopupDelete(HashMap<String, Object> param);

    MainNoticePopup selectMainNoticePopup(HashMap<String, Object> param);

    void updateMainNoticePopup(MainNoticePopup noticePopup);

    void updateMainNoticePopupOrder(HashMap<String, Object> param);
}
