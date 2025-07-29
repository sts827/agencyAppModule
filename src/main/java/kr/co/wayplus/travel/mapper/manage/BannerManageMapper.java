package kr.co.wayplus.travel.mapper.manage;

import kr.co.wayplus.travel.model.MainBannerImage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface BannerManageMapper {

    int selectMainBannerImageListCount(HashMap<String, Object> param);

    ArrayList<MainBannerImage> selectMainBannerImageList(HashMap<String, Object> param);

    void insertMainBannerImage(MainBannerImage bannerImage);

    void updateMainBannerDelete(HashMap<String, Object> param);

    void updateMainBannerOrder(HashMap<String, Object> param);

    MainBannerImage selectMainBannerImage(HashMap<String, Object> param);

    void updateMainBannerImage(MainBannerImage bannerImage);

    ArrayList<MainBannerImage> selectMainBannerPreviewList(HashMap<String, Object> param);

}
