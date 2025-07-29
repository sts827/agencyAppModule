package kr.co.wayplus.travel.web.manage;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.service.manage.MainManageService;
import kr.co.wayplus.travel.util.LoggerUtil;
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
@RequestMapping("/manage/main")
public class MainManageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MainManageService manageService;

    @Value("${upload.file.path}")
    String externalImageUploadPath;
    @Value("${upload.file.max-size}")
    int maxFileSize;
    final String addPath = "images/main/";

    public MainManageController(MainManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/popup/list")
    public ModelAndView popupList(@RequestParam(value="type",defaultValue="layer") String type,
                                  @RequestParam(value="useYn",defaultValue="ALL") String useYn,
                                  @RequestParam(value="page",defaultValue="1") int page,
                                  @RequestParam(value="pageSize",defaultValue="10") int pageSize,
                                  @RequestParam(value="searchKey",defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("useYn", useYn);
        param.put("searchKey", searchKey);

        int totalCount = manageService.getPopupListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());

        mav.addObject("p", param);
        mav.addObject("popupList", manageService.getPopupList(param));
        mav.addObject("paging", paging);
        mav.setViewName("manage/sub/popup/list");
        return mav;
    }

    @GetMapping("/popup/add")
    public ModelAndView popupAdd(@RequestParam(value="type",defaultValue="layer") String type){
        ModelAndView mav = new ModelAndView();
        switch (type){
            case "layer": mav.setViewName("manage/sub/popup/form-layer"); break;
            case "notice-bar": mav.setViewName("manage/sub/popup/form-notice-bar"); break;
        }
        return mav;
    }

    @GetMapping("/popup/modify")
    public ModelAndView popupModify(@RequestParam(value="type",defaultValue="layer") String type,
                                    @RequestParam(value="id", defaultValue="0") int id,
                                    @RequestParam(value="timeKey", defaultValue="") String timeKey){
        ModelAndView mav = new ModelAndView();
        HashMap<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("timeKey", timeKey);
        mav.addObject("popup", manageService.getMainNoticePopup(param));
        switch (type){
            case "layer": mav.setViewName("manage/sub/popup/form-layer"); break;
            case "notice-bar": mav.setViewName("manage/sub/popup/form-notice-bar"); break;
        }
        return mav;
    }

    @PostMapping("/popup/add")
    @ResponseBody
    public HashMap<String, Object> popupCreate(@ModelAttribute MainNoticePopup noticePopup, BindingResult bindingResult,
                                               MultipartHttpServletRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(noticePopup.getNoticeType().equals("layer")){
                if(noticePopup.getContentType().equals("image")){
                    //이미지 팝업 파일등록
                    List<MultipartFile> multipartFiles;
                    if(request.getFile("image") != null) {
                        multipartFiles = request.getFiles("image");
                        File file = new File(externalImageUploadPath + addPath);
                        if (!file.exists()) file.mkdirs();

                        for (MultipartFile multipartFile : multipartFiles) {
                            String uploadName = UUID.randomUUID().toString();
                            multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                            logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                            MainAttachImage attachImage = new MainAttachImage();
                            attachImage.setServiceType("popup");
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
                            noticePopup.setImageIdPc(attachImage.getFileId());
                            noticePopup.setImageUrlPc(addPath+uploadName);
                        }
                    }else{
                        resultMap.put("result", "fail");
                        resultMap.put("message", "파일이 없습니다.");
                        return resultMap;
                    }
                }
            }else{
                noticePopup.setContentType("image");
                List<MultipartFile> multipartFiles;
                if(request.getFile("image_pc") != null) {
                    multipartFiles = request.getFiles("image_pc");
                    File file = new File(externalImageUploadPath + addPath);
                    if (!file.exists()) file.mkdirs();

                    for (MultipartFile multipartFile : multipartFiles) {
                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                        MainAttachImage attachImage = new MainAttachImage();
                        attachImage.setServiceType("notice_bar_pc");
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
                        noticePopup.setImageIdPc(attachImage.getFileId());
                        noticePopup.setImageUrlPc(addPath+uploadName);
                    }
                }

                if(request.getFile("image_mobile") != null) {
                    multipartFiles = request.getFiles("image_mobile");
                    File file = new File(externalImageUploadPath + addPath);
                    if (!file.exists()) file.mkdirs();

                    for (MultipartFile multipartFile : multipartFiles) {
                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                        MainAttachImage attachImage = new MainAttachImage();
                        attachImage.setServiceType("notice_bar_mobile");
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
                        noticePopup.setImageIdMobile(attachImage.getFileId());
                        noticePopup.setImageUrlMobile(addPath+uploadName);
                    }
                }
            }

            //보이기 상태 설정
            if(noticePopup.getUseYn() != null && noticePopup.getUseYn().equals("Y")){
                switch (noticePopup.getVisibleCode()){
                    case "ALL":
                        noticePopup.setVisibleYnPc("Y");
                        noticePopup.setVisibleYnMobile("Y");
                        break;
                    case "PC":
                        noticePopup.setVisibleYnPc("Y");
                        noticePopup.setVisibleYnMobile("N");
                        break;
                    case "MOBILE":
                        noticePopup.setVisibleYnPc("N");
                        noticePopup.setVisibleYnMobile("Y");
                        break;
                    default:
                        noticePopup.setVisibleYnPc("N");
                        noticePopup.setVisibleYnMobile("N");
                }
            }else{
                noticePopup.setVisibleYnPc("N");
                noticePopup.setVisibleYnMobile("N");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            noticePopup.setCreateId(user.getUserEmail());
            noticePopup.setOrderNumber(100);
            noticePopup.setTimeKey(sdf.format(new Date()) + String.valueOf(Math.random()).substring(2, 6) );
            manageService.writeMainNoticePopup(noticePopup);

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

    @PutMapping("/popup/update")
    @ResponseBody
    public HashMap<String, Object> popupUpdate(@ModelAttribute MainNoticePopup noticePopup, BindingResult bindingResult,
                                               MultipartHttpServletRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(noticePopup.getNoticeType().equals("layer")) {
                if (noticePopup.getContentType().equals("image")) {
                    //이미지 팝업 파일등록
                    List<MultipartFile> multipartFiles;
                    if (request.getFile("image") != null) {
                        multipartFiles = request.getFiles("image");
                        File file = new File(externalImageUploadPath + addPath);
                        if (!file.exists()) file.mkdirs();
                        for (MultipartFile multipartFile : multipartFiles) {
                            if(multipartFile.getSize() == 0) continue;
                            String uploadName = UUID.randomUUID().toString();
                            multipartFile.transferTo(new File(externalImageUploadPath + addPath + uploadName));
                            logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                            MainAttachImage attachImage = new MainAttachImage();
                            attachImage.setServiceType("popup");
                            attachImage.setUploadPath(externalImageUploadPath + addPath);
                            attachImage.setUploadFilename(addPath + uploadName);
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
                            noticePopup.setImageIdPc(attachImage.getFileId());
                            noticePopup.setImageUrlPc(addPath + uploadName);
                        }
                    }
                }
            }else{
                noticePopup.setContentType("image");
                List<MultipartFile> multipartFiles;
                if (request.getFile("image_pc") != null) {
                    multipartFiles = request.getFiles("image_pc");
                    File file = new File(externalImageUploadPath + addPath);
                    if (!file.exists()) file.mkdirs();
                    for (MultipartFile multipartFile : multipartFiles) {
                        if(multipartFile.getSize() == 0) continue;
                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + addPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                        MainAttachImage attachImage = new MainAttachImage();
                        attachImage.setServiceType("notice_bar_pc");
                        attachImage.setUploadPath(externalImageUploadPath + addPath);
                        attachImage.setUploadFilename(addPath + uploadName);
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
                        noticePopup.setImageIdPc(attachImage.getFileId());
                        noticePopup.setImageUrlPc(addPath + uploadName);
                    }
                }

                if (request.getFile("image_mobile") != null) {
                    multipartFiles = request.getFiles("image_mobile");
                    File file = new File(externalImageUploadPath + addPath);
                    if (!file.exists()) file.mkdirs();
                    for (MultipartFile multipartFile : multipartFiles) {
                        if(multipartFile.getSize() == 0) continue;
                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + addPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                        MainAttachImage attachImage = new MainAttachImage();
                        attachImage.setServiceType("notice_bar_mobile");
                        attachImage.setUploadPath(externalImageUploadPath + addPath);
                        attachImage.setUploadFilename(addPath + uploadName);
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
                        noticePopup.setImageIdMobile(attachImage.getFileId());
                        noticePopup.setImageUrlMobile(addPath + uploadName);
                    }
                }
            }

            //보이기 상태 설정
            if(noticePopup.getUseYn() != null && noticePopup.getUseYn().equals("Y")){
                switch (noticePopup.getVisibleCode()){
                    case "ALL":
                        noticePopup.setVisibleYnPc("Y");
                        noticePopup.setVisibleYnMobile("Y");
                        break;
                    case "PC":
                        noticePopup.setVisibleYnPc("Y");
                        noticePopup.setVisibleYnMobile("N");
                        break;
                    case "MOBILE":
                        noticePopup.setVisibleYnPc("N");
                        noticePopup.setVisibleYnMobile("Y");
                        break;
                    default:
                        noticePopup.setVisibleYnPc("N");
                        noticePopup.setVisibleYnMobile("N");
                }
            }else{
                noticePopup.setVisibleYnPc("N");
                noticePopup.setVisibleYnMobile("N");
                noticePopup.setUseYn("N");
            }

            if(noticePopup.getOneDayYn() == null) noticePopup.setOneDayYn("N");
            if(noticePopup.getOneWeekYn() == null) noticePopup.setOneWeekYn("N");
            noticePopup.setLastUpdateId(user.getUserEmail());
            manageService.modifyMainNoticePopup(noticePopup);

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

    @PutMapping("/popup/status")
    @ResponseBody
    public HashMap<String, Object> popupStatus(@RequestParam(value="id", defaultValue="0") int id,
                                               @RequestParam(value="timeKey", defaultValue="") String timeKey,
                                               @RequestParam(value="type", defaultValue="") String type,
                                               @RequestParam(value="status", defaultValue="N") String status){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(id == 0 || timeKey.isEmpty() || type.isEmpty())
                throw new Exception("업데이트 항목을 찾을 수 없습니다.");

            HashMap<String, Object> param = new HashMap<>();
            param.put("lastUpdateId", user.getUserEmail());
            param.put("id", id);
            param.put("timeKey", timeKey);
            param.put("type", type);
            param.put("status", status);

            manageService.modifyMainNoticePopupStatus(param);

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

    @DeleteMapping("/popup/delete")
    @ResponseBody
    public HashMap<String, Object> popupDelete(@RequestParam(value="id", defaultValue="0") int id,
                                               @RequestParam(value="timeKey", defaultValue="") String timeKey){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(id == 0 || timeKey.isEmpty())
                throw new Exception("삭제 항목을 찾을 수 없습니다.");

            HashMap<String, Object> param = new HashMap<>();
            param.put("deleteId", user.getUserEmail());
            param.put("id", id);
            param.put("timeKey", timeKey);
            manageService.modifyMainNoticePopupDelete(param);

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


    @PutMapping("/popup/orderly")
    @ResponseBody
    public HashMap<String, Object> popupOrderly(HttpServletRequest request){
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
                manageService.modifyMainNoticePopupOrder(param);
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

}
