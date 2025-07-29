package kr.co.wayplus.travel.web.manage;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.service.front.PageService;
import kr.co.wayplus.travel.service.manage.BannerManageService;
import kr.co.wayplus.travel.service.manage.MainManageService;
import kr.co.wayplus.travel.service.manage.MenuManageService;
import kr.co.wayplus.travel.util.LoggerUtil;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/manage")
public class BannerManageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MainManageService manageService;
    private final BannerManageService bannerManageService;
    private final MenuManageService menuService;

    private final PageService pageService;

    @Value("${upload.file.path}")
    String externalImageUploadPath;
    @Value("${upload.file.max-size}")
    int maxFileSize;
    final String addPath = "images/banner/";

    public BannerManageController(
            MainManageService manageService,
            BannerManageService bannerManageService,
            MenuManageService menuService, PageService pageService) {
        this.manageService       = manageService;
        this.bannerManageService = bannerManageService;
        this.menuService         = menuService;
        this.pageService = pageService;
    }

    @GetMapping(value={"/banner/list", "/menu/user/list-banner"})
    public ModelAndView bannerList(@RequestParam(value="bannerType",defaultValue="MAIN") String bannerType,
                                   @RequestParam(value="useYn",defaultValue="ALL") String useYn,
                                   @RequestParam(value="page",defaultValue="1") int page,
                                   @RequestParam(value="pageSize",defaultValue="10") int pageSize,
                                   @RequestParam(value="searchKey",defaultValue="") String searchKey,
                                   @Param(value="menuId") Integer menuId){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> param = new HashMap<>();
        param.put("bannerType", bannerType);
        param.put("useYn", useYn);
        param.put("searchKey", searchKey);
        if(menuId != null)
        	param.put("menuId", menuId);

        int totalCount = bannerManageService.getMainBannerImageListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("bannerList", bannerManageService.getMainBannerImageList(param));
        mav.addObject("paging", paging);

        param.put("bannerType", "INTRO");
        param.put("upperCode", "langType");
        param.put("useYn", "Y");
        mav.addObject("codeList", pageService.selectListCodeItem(param));
        mav.addObject("introBannerList", bannerManageService.getMainBannerImageList(param));

        mav.setViewName("manage/sub/banner/list");
        return mav;
    }

    @GetMapping(value={"/banner/add","/menu/user/banner-add"})
    public ModelAndView bannerAdd(@RequestParam(value="type",defaultValue="MAIN") String type,
                                  @RequestParam(value="id", defaultValue="0") int id,
                                  @Param(value="menuId") Integer menuId){
        ModelAndView mav = new ModelAndView();
        if(menuId != null) {
        	HashMap<String, Object> param = new HashMap<>();
        	param.put("menuId",menuId);

        	MenuUser menu = menuService.selectOneMenuUser(param);
        	mav.addObject("menuUser", menu);
        }
        switch (type){
            case "MAIN":
            case "menuBanner":
            	mav.setViewName("manage/sub/banner/form"); break;
        }
        return mav;
    }

    @GetMapping(value={"/banner/modify","/menu/user/banner-modify"})
    public ModelAndView bannerModify(@RequestParam(value="type",defaultValue="MAIN") String type,
                                     @RequestParam(value="id", defaultValue="0") int id,
                                     @Param(value="menuId") Integer menuId){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> param = new HashMap<>();
        param.put("bannerType", type);
        param.put("id", id);
        MainBannerImage banner = bannerManageService.getMainBannerImage(param);
        mav.addObject("banner", banner);
        if(menuId != null) {
        	param.clear();
        	param.put("menuId",menuId);

        	MenuUser menu = menuService.selectOneMenuUser(param);
        	mav.addObject("menuUser", menu);
        }
        switch (type){
            case "MAIN":
            case "menuBanner":
            	mav.setViewName("manage/sub/banner/form"); break;
        }
        return mav;
    }

    @PostMapping("/banner/add")
    @ResponseBody
    public HashMap<String, Object> bannerCreate(@ModelAttribute MainBannerImage bannerImage, BindingResult bindingResult,
                                               MultipartHttpServletRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


            List<MultipartFile> multipartFiles;

            if(bannerImage.getVisibleYnPc()!= null && bannerImage.getVisibleYnPc().equals("Y")
                    && request.getFile("image_pc") != null) {
                multipartFiles = request.getFiles("image_pc");
                File file = new File(externalImageUploadPath + addPath);
                if (!file.exists()) file.mkdirs();

                for (MultipartFile multipartFile : multipartFiles) {
                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                    logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                    MainAttachImage attachImage = new MainAttachImage();
                    attachImage.setServiceType("main_banner_pc");
                    attachImage.setUploadPath(externalImageUploadPath+ addPath);
                    attachImage.setUploadFilename(addPath+uploadName);
                    attachImage.setOriginFilename(multipartFile.getOriginalFilename());
                    attachImage.setFileSize((int) multipartFile.getSize());
                    attachImage.setFileMimetype(multipartFile.getContentType());
                    if (multipartFile.getOriginalFilename().contains(".")) {
                        attachImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                    }
                    if (user != null) {
                        attachImage.setUploadId(String.valueOf(user.getUserEmail()));
                    }
                    manageService.writeMainAttachImage(attachImage);
                    bannerImage.setImageIdPc(attachImage.getFileId());
                    bannerImage.setImageUrlPc(addPath+uploadName);
                }
            }

            if(bannerImage.getVisibleYnMobile()!= null && bannerImage.getVisibleYnMobile().equals("Y")
                &&request.getFile("image_mobile") != null) {
                multipartFiles = request.getFiles("image_mobile");
                File file = new File(externalImageUploadPath + addPath);
                if (!file.exists()) file.mkdirs();

                for (MultipartFile multipartFile : multipartFiles) {
                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                    logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                    MainAttachImage attachImage = new MainAttachImage();
                    attachImage.setServiceType("main_banner_mobile");
                    attachImage.setUploadPath(externalImageUploadPath+ addPath);
                    attachImage.setUploadFilename(addPath+uploadName);
                    attachImage.setOriginFilename(multipartFile.getOriginalFilename());
                    attachImage.setFileSize((int) multipartFile.getSize());
                    attachImage.setFileMimetype(multipartFile.getContentType());
                    if (multipartFile.getOriginalFilename().contains(".")) {
                        attachImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                    }
                    if (user != null) {
                        attachImage.setUploadId(String.valueOf(user.getUserEmail()));
                    }
                    manageService.writeMainAttachImage(attachImage);
                    bannerImage.setImageIdMobile(attachImage.getFileId());
                    bannerImage.setImageUrlMobile(addPath+uploadName);
                }
            }

            bannerImage.setCreateId(user.getUserEmail());
            bannerManageService.writeMainBannerImage(bannerImage);

            resultMap.put("result", "success");
            resultMap.put("message", "저장됐습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @PutMapping("/banner/update")
    @ResponseBody
    public HashMap<String, Object> bannerUpdate(@ModelAttribute MainBannerImage bannerImage, BindingResult bindingResult,
                                               MultipartHttpServletRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<MultipartFile> multipartFiles;
            if(bannerImage.getVisibleYnPc().equals("Y")
                    && request.getFile("image_pc") != null) {
                multipartFiles = request.getFiles("image_pc");
                File file = new File(externalImageUploadPath + addPath);
                if (!file.exists()) file.mkdirs();

                for (MultipartFile multipartFile : multipartFiles) {
                    if(multipartFile.getSize() == 0) continue;
                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                    logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                    MainAttachImage attachImage = new MainAttachImage();
                    attachImage.setServiceType("main_banner_pc");
                    attachImage.setUploadPath(externalImageUploadPath+ addPath);
                    attachImage.setUploadFilename(addPath+uploadName);
                    attachImage.setOriginFilename(multipartFile.getOriginalFilename());
                    attachImage.setFileSize((int) multipartFile.getSize());
                    attachImage.setFileMimetype(multipartFile.getContentType());
                    if (multipartFile.getOriginalFilename().contains(".")) {
                        attachImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                    }
                    if (user != null) {
                        attachImage.setUploadId(String.valueOf(user.getUserEmail()));
                    }
                    manageService.writeMainAttachImage(attachImage);
                    bannerImage.setImageIdPc(attachImage.getFileId());
                    bannerImage.setImageUrlPc(addPath+uploadName);
                }
            }

            if(bannerImage.getVisibleYnMobile().equals("Y")
                &&request.getFile("image_mobile") != null) {
                multipartFiles = request.getFiles("image_mobile");
                File file = new File(externalImageUploadPath + addPath);
                if (!file.exists()) file.mkdirs();

                for (MultipartFile multipartFile : multipartFiles) {
                    if(multipartFile.getSize() == 0) continue;
                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                    logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                    MainAttachImage attachImage = new MainAttachImage();
                    attachImage.setServiceType("main_banner_mobile");
                    attachImage.setUploadPath(externalImageUploadPath+ addPath);
                    attachImage.setUploadFilename(addPath+uploadName);
                    attachImage.setOriginFilename(multipartFile.getOriginalFilename());
                    attachImage.setFileSize((int) multipartFile.getSize());
                    attachImage.setFileMimetype(multipartFile.getContentType());
                    if (multipartFile.getOriginalFilename().contains(".")) {
                        attachImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                    }
                    if (user != null) {
                        attachImage.setUploadId(String.valueOf(user.getUserEmail()));
                    }
                    manageService.writeMainAttachImage(attachImage);
                    bannerImage.setImageIdMobile(attachImage.getFileId());
                    bannerImage.setImageUrlMobile(addPath+uploadName);
                }
            }

            if(bannerImage.getVisibleYnPc() == null) bannerImage.setVisibleYnPc("N");
            if(bannerImage.getVisibleYnMobile() == null) bannerImage.setVisibleYnMobile("N");
            bannerImage.setLastUpdateId(user.getUserEmail());
            bannerManageService.updateMainBannerImage(bannerImage);

            resultMap.put("result", "success");
            resultMap.put("message", "저장됐습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @DeleteMapping("/banner/delete")
    @ResponseBody
    public HashMap<String, Object> bannerDelete(@RequestParam(value="id", defaultValue="0") int id){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(id == 0) throw new Exception("삭제 항목을 찾을 수 없습니다.");

            HashMap<String, Object> param = new HashMap<>();
            param.put("deleteId", user.getUserEmail());
            param.put("id", id);
            bannerManageService.modifyMainBannerDelete(param);

            resultMap.put("result", "success");
            resultMap.put("message", "저장됐습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }


    @PutMapping("/banner/orderly")
    @ResponseBody
    public HashMap<String, Object> bannerOrderly(HttpServletRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(request.getParameter("ids") == null){
                throw new Exception("파라미터가 없습니다.");
            }

            String[] ids = null;
            Enumeration enumeration = request.getParameterNames();
            while(enumeration.hasMoreElements()) {
                String parameterName = enumeration.nextElement().toString();
                if(parameterName.equals("ids")) ids = request.getParameterValues(parameterName);
            }

            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            HashMap<String, Object> param = new HashMap<>();
            param.put("lastUpdateId", user.getUserEmail());
            for(int i=0; i<ids.length; i++){
                param.put("id", ids[i]);
                param.put("orderNumber", 100+i);
                bannerManageService.modifyMainBannerOrder(param);
            }

            resultMap.put("result", "success");
            resultMap.put("message", "저장됐습니다.");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    @GetMapping("/banner/preview/list")
    @ResponseBody
    public HashMap<String, Object>  bannerPreviewList(
    		@RequestParam(value="bannerType",defaultValue="MAIN") String bannerType,
            @Param(value="menuId") Integer menuId){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            HashMap<String, Object> param = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
            param.put("now", sdf.format(new Date()));
            param.put("bannerType", bannerType);
            if(menuId != null) param.put("menuId",menuId);
            param.put("type", "PC");
            resultMap.put("pc", bannerManageService.getMainBannerPreviewList(param));

            param.put("type", "MO");
            resultMap.put("mobile", bannerManageService.getMainBannerPreviewList(param));

            resultMap.put("result", "success");
            resultMap.put("message", "조회완료");
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

}
