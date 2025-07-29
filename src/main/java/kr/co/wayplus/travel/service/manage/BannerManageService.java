package kr.co.wayplus.travel.service.manage;

import kr.co.wayplus.travel.mapper.manage.BannerManageMapper;
import kr.co.wayplus.travel.model.MainBannerImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class BannerManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BannerManageMapper bannerManageMapper;

    public BannerManageService(BannerManageMapper bannerManageMapper) {
        this.bannerManageMapper = bannerManageMapper;
    }

    public int getMainBannerImageListCount(HashMap<String, Object> param) {
        return bannerManageMapper.selectMainBannerImageListCount(param);
    }

    public ArrayList<MainBannerImage> getMainBannerImageList(HashMap<String, Object> param) {
        return bannerManageMapper.selectMainBannerImageList(param);
    }

    public void writeMainBannerImage(MainBannerImage bannerImage) {
        bannerManageMapper.insertMainBannerImage(bannerImage);
    }

    public void modifyMainBannerDelete(HashMap<String, Object> param) {
        bannerManageMapper.updateMainBannerDelete(param);
    }

    public void modifyMainBannerOrder(HashMap<String, Object> param) {
        bannerManageMapper.updateMainBannerOrder(param);
    }

    public MainBannerImage getMainBannerImage(HashMap<String, Object> param) {
        return bannerManageMapper.selectMainBannerImage(param);
    }

    public void updateMainBannerImage(MainBannerImage bannerImage) {
        bannerManageMapper.updateMainBannerImage(bannerImage);
    }

    public ArrayList<MainBannerImage> getMainBannerPreviewList(HashMap<String, Object> param) {
        return  bannerManageMapper.selectMainBannerPreviewList(param);
    }

}
