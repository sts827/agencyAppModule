package kr.co.wayplus.travel.web.manage;

import java.io.File;
import java.util.*;

import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.service.manage.MainManageService;
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

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.service.manage.SettingManageService;
import kr.co.wayplus.travel.util.FileInfoUtil;

@Controller
@RequestMapping("/manage")
public class SettingManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

    @Value("${upload.file.max-size}")
    int maxFileSize;

    @Value("${key.api.kakao.rest}")
    private String DAPI_KEY;

    private final String addPath = "images/settings/";

    private final SettingManageService service;
    private final MainManageService mainManageService;
    public SettingManageController(SettingManageService svc1, MainManageService mainManageService) {
        this.service = svc1;
        this.mainManageService = mainManageService;
    }

    @GetMapping("/history/list")
    public ModelAndView history_list(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/history/list");
        return mav;
    }

    @GetMapping("/history/form")
    public ModelAndView history_form(@RequestParam(value = "contentId", defaultValue = "", required = false) String contentId){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/history/form");

        if ( !contentId.equals("") ) {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("content_id", contentId);

            mav.addObject("historyContents", service.selectOneHistoryContent(paramMap));

            mav.setViewName("manage/sub/history/modify-form");
            return mav;
        }
        return mav;


    }

    @PostMapping("/history-list")
    @ResponseBody
    public HashMap<String, Object> history_list_ajax(HttpServletRequest request,
                                                     @RequestParam(value="start", defaultValue="0") int start,
                                                     @RequestParam(value="length", defaultValue="0") int length,
                                                     @Param(value="contentLike") String contentLike){
    	HashMap<String, Object> returnMap = new HashMap<>();

        try {
            HashMap<String, Object> paramMap = new HashMap<>();

            List<SortData> listSort = getListOrder(request);
            paramMap.put("listSort", listSort);

            if(length >= 0) {
	            paramMap.put("itemStartPosition", start);
	            paramMap.put("pagePerSize", length);
            }

            paramMap.put("contentLike", contentLike);

            int totalCount = service.selectCountHistoryContents(paramMap);

            returnMap.put("recordsTotal", totalCount);
            returnMap.put("recordsFiltered", totalCount);
            returnMap.put("data", service.selectListHistoryContents(paramMap));

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @PostMapping("/history-save")
    @ResponseBody
    public HashMap<String, Object> history_save_ajax(SettingHistoryContents historyContents){
    	HashMap<String, Object> returnMap = new HashMap<>();

    	try {
            //연혁 등록
            service.insertHistoryContents(historyContents);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
		}

        return returnMap;
    }

    @PostMapping("/history-delete")
    @ResponseBody
    public HashMap<String, Object> history_delete_ajax(@RequestParam HashMap<String, Object> paramMap){
    	HashMap<String, Object> returnMap = new HashMap<>();

    	try {
            service.deleteHistoryContents(paramMap);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
		}


        return returnMap;
    }

    @PostMapping("/history-restore")
    @ResponseBody
    public HashMap<String, Object> history_restore_ajax(@RequestParam HashMap<String, Object> paramMap){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            service.restoreHistoryContents(paramMap);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @PostMapping("/history-modify")
    @ResponseBody
    public HashMap<String, Object> history_modify_ajax(SettingHistoryContents historyContents) {
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            service.updateHistoryContents(historyContents);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @GetMapping("/awards/list")
    public ModelAndView awards_list(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/awards/list");
        return mav;
    }

    @GetMapping("/awards/form")
    public ModelAndView awards_view(@RequestParam(value = "contentId", defaultValue = "", required = false) String contentId){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/awards/form");

        if ( !contentId.equals("") ) {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("content_id", contentId);

            mav.addObject("awardsContents", service.selectOneAwardsContent(paramMap));

            mav.setViewName("manage/sub/awards/modify-form");
            return mav;
        }
        return mav;
    }

    @PostMapping("/awards-list")
    @ResponseBody
    public HashMap<String, Object> awards_list_ajax(HttpServletRequest request,
                                                    @RequestParam(value="start", defaultValue="0") int start,
                                                    @RequestParam(value="length", defaultValue="0") int length,
                                                    @Param(value="titleLike") String titleLike){
        HashMap<String, Object> retrunMap = new HashMap<>();

        try {
            HashMap<String, Object> paramMap = new HashMap<>();

            List<SortData> listSort = getListOrder(request);
            paramMap.put("listSort", listSort);

            if(length >= 0) {
	            paramMap.put("itemStartPosition", start);
	            paramMap.put("pagePerSize", length);
            }

            paramMap.put("titleLike", titleLike);

            int totalCount = service.selectCountAwardsContents(paramMap);

            retrunMap.put("recordsTotal", totalCount);
            retrunMap.put("recordsFiltered", totalCount);
            retrunMap.put("data", service.selectListAwardsContents(paramMap));

            retrunMap.put("result", "success");
            retrunMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            retrunMap.put("result", "fail");
            retrunMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return retrunMap;
    }

    @PostMapping("/awards-save")
    @ResponseBody
    public HashMap<String, Object> awards_save_ajax(MultipartHttpServletRequest request){
    	HashMap<String, Object> returnMap = new HashMap<>();

        try {
            //상장 등록
            SettingAwardsContents awardsContents = new SettingAwardsContents();
            awardsContents.setRegDate(request.getParameter("regDttm"));
            awardsContents.setTitle(request.getParameter("title"));
            awardsContents.setUseYn(request.getParameter("use_yn"));

            service.insertAwardsContents(awardsContents);

            if(request.getFile("attach") != null) {
                List<MultipartFile> multipartFiles = request.getFiles("attach");
                if (request.getFile("attach").getSize() > 0) {
                    //저장 디렉토리 생성 확인
                    File file = new File(externalImageUploadPath);
                    if (!file.exists()) file.mkdirs();
                    //업로드 된 파일들 복사

                    for (MultipartFile multipartFile : multipartFiles) {
                        if (!multipartFile.getContentType().contains("jpg") &&
                        	!multipartFile.getContentType().contains("jpeg") &&
                        	!multipartFile.getContentType().contains("png") ) {
                            throw new Exception("jpg, jpeg, png 파일을 첨부해주세요.");
                        }

                        if (multipartFile.getSize() > maxFileSize) {
                            throw new Exception((maxFileSize / 1024 / 1024) + "MB 이하의 파일만 첨부 가능합니다.");
                        }

                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());


                        SettingAwardsImage awardsImage = new SettingAwardsImage();
                        awardsImage.setId(awardsContents.getId());
                        awardsImage.setServiceType("awards");
                        awardsImage.setUploadPath(externalImageUploadPath);
                        awardsImage.setUploadFilename(uploadName);
                        awardsImage.setOriginFilename(multipartFile.getOriginalFilename());
                        awardsImage.setFileSize((int) multipartFile.getSize());
                        awardsImage.setFileMimetype(multipartFile.getContentType());
                        if (multipartFile.getOriginalFilename().contains(".")) {
                            awardsImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                        }

//                        if (loginUser != null) {
//                            awardsImage.setCreate_id(String.valueOf(loginUser.getUser_email()));
//                        }

                        service.insertAwardsAttachFile(awardsImage);
                    }
                }
            }

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }


        return returnMap;
    }

    @PostMapping("/awards-delete")
    @ResponseBody
    public HashMap<String, Object> awards_delete_ajax(@RequestParam HashMap<String, Object> paramMap){
    	HashMap<String, Object> returnMap = new HashMap<>();

    	try {
            service.deleteAwardsContents(paramMap);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return returnMap;
    }

    @PostMapping("/awards-restore")
    @ResponseBody
    public HashMap<String, Object> awards_restore_ajax(@RequestParam HashMap<String, Object> paramMap){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            service.restoreAwardsContents(paramMap);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
        }

        return returnMap;
    }

    @PostMapping("/awards-modify")
    @ResponseBody
    public HashMap<String, Object> awards_modify_ajax(
    		MultipartHttpServletRequest request) {
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            //상장 등록
            SettingAwardsContents awardsContents = new SettingAwardsContents();
            awardsContents.setId( Integer.parseInt( request.getParameter("content_id") ) );
            awardsContents.setRegDate( request.getParameter("regDttm") );
            awardsContents.setType( request.getParameter("type") );
            awardsContents.setTitle( request.getParameter("title") );
            awardsContents.setUseYn( request.getParameter("use_yn") );
            service.updateAwardsContents(awardsContents);

            if (request.getFile("attach") != null) {
                List<MultipartFile> multipartFiles = request.getFiles("attach");
                if (request.getFile("attach").getSize() > 0) {
                    //저장 디렉토리 생성 확인
                    File file = new File(externalImageUploadPath);
                    if (!file.exists()) file.mkdirs();
                    //업로드 된 파일들 복사
                    for (MultipartFile multipartFile : multipartFiles) {
                    	System.out.println("jpg ?," + multipartFile.getContentType());

                        if (!multipartFile.getContentType().contains("jpg") &&
                        	!multipartFile.getContentType().contains("jpeg") &&
                        	!multipartFile.getContentType().contains("png")) {
                            throw new Exception("jpg, jpeg, png 파일을 첨부해주세요.");
                        }

                        if (multipartFile.getSize() > maxFileSize) {
                            throw new Exception((maxFileSize / 1024 / 1024) + "MB 이하의 파일만 첨부 가능합니다.");
                        }

                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());

                        SettingAwardsImage awardsImage = new SettingAwardsImage();
                        awardsImage.setId(awardsContents.getId());
                        awardsImage.setServiceType("awards");
                        awardsImage.setUploadPath(externalImageUploadPath);
                        awardsImage.setUploadFilename(uploadName);
                        awardsImage.setOriginFilename(multipartFile.getOriginalFilename());
                        awardsImage.setFileSize((int) multipartFile.getSize());
                        awardsImage.setFileMimetype(multipartFile.getContentType());
                        if (multipartFile.getOriginalFilename().contains(".")) {
                            awardsImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                        }

//                        if (loginUser != null) {
//                            awardsImage.setCreate_id(String.valueOf(loginUser.getUser_email()));
//                        }
                        awardsImage.setUploadId("none");//시큐리티 붙으면 삭제

                        HashMap<String, Object> paramMap = new HashMap<>();
                        paramMap.clear();
                        paramMap.put("id", awardsContents.getId());

                        SettingAwardsImage _old = service.selectOneAwardsAttachFile( paramMap );
                        if(_old != null)
                        	FileInfoUtil.deleteImageFile_real(_old); /*실제 이미지 파일 제거*/

                        service.deleteAwardsAttachFile(awardsImage);
                        service.insertAwardsAttachFile(awardsImage);
                    }
                }
                else if ( request.getFile("attach").getSize() <= 0 && request.getParameter("imageDeleteFlag").equals("imageDelete") ) {
                    SettingAwardsImage awardsImage = new SettingAwardsImage();
                    awardsImage.setId(awardsContents.getId());
                    service.deleteAwardsAttachFile(awardsImage);
                }
            }


            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @GetMapping("/alliance/list")
    public ModelAndView alliance_list(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/alliance/list");
        return mav;
    }

    @GetMapping("/alliance/form")
    public ModelAndView alliance_form(@RequestParam(value = "contentId", defaultValue = "", required = false) String contentId){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/alliance/form");

        if ( !contentId.equals("") ) {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("content_id", contentId);

            mav.addObject("allianceContents", service.selectOneAllianceContent(paramMap));

            mav.setViewName("manage/sub/alliance/modify-form");
            return mav;
        }
        return mav;

    }

    @PostMapping("/alliance-list")
    @ResponseBody
    public HashMap<String, Object> alliance_list_ajax(HttpServletRequest request,
                                                      @RequestParam(value="start", defaultValue="0") int start,
                                                      @RequestParam(value="length", defaultValue="0") int length,
                                                      @Param(value="titleLike") String titleLike){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            HashMap<String, Object> paramMap = new HashMap<>();

            List<SortData> listSort = getListOrder(request);
            paramMap.put("listSort", listSort);

            if(length >= 0) {
	            paramMap.put("itemStartPosition", start);
	            paramMap.put("pagePerSize", length);
            }

            paramMap.put("titleLike", titleLike);

            int totalCount = service.selectCountAllianceContents(paramMap);

            returnMap.put("recordsTotal", totalCount);
            returnMap.put("recordsFiltered", totalCount);
            returnMap.put("data", service.selectListAllianceContents(paramMap));

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @PostMapping("/alliance-save")
    @ResponseBody
    public HashMap<String, Object> alliance_save_ajax(MultipartHttpServletRequest request){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            //상장 등록
            SettingAllianceContents allianceContents = new SettingAllianceContents();
            allianceContents.setRegDate(request.getParameter("regDttm"));
            allianceContents.setTitle(request.getParameter("title"));
            allianceContents.setLink(request.getParameter("link"));
            allianceContents.setUseYn(request.getParameter("use_yn"));

            service.insertAllianceContents(allianceContents);

            if(request.getFile("attach") != null) {
                List<MultipartFile> multipartFiles = request.getFiles("attach");
                if (request.getFile("attach").getSize() > 0) {
                    //저장 디렉토리 생성 확인
                    File file = new File(externalImageUploadPath);
                    if (!file.exists()) file.mkdirs();
                    //업로드 된 파일들 복사
                    for (MultipartFile multipartFile : multipartFiles) {
                        if (!multipartFile.getContentType().contains("jpg") &&
                        	!multipartFile.getContentType().contains("jpeg") &&
                        	!multipartFile.getContentType().contains("png")) {
                            throw new Exception("jpg, jpeg, png 파일을 첨부해주세요.");
                        }

                        if (multipartFile.getSize() > maxFileSize) {
                            throw new Exception((maxFileSize / 1024 / 1024) + "MB 이하의 파일만 첨부 가능합니다.");
                        }

                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());


                        SettingAllianceImage allianceImage = new SettingAllianceImage();
                        allianceImage.setId(allianceContents.getId());
                        allianceImage.setServiceType("awards");
                        allianceImage.setUploadPath(externalImageUploadPath);
                        allianceImage.setUploadFilename(uploadName);
                        allianceImage.setOriginFilename(multipartFile.getOriginalFilename());
                        allianceImage.setFileSize((int) multipartFile.getSize());
                        allianceImage.setFileMimetype(multipartFile.getContentType());
                        if (multipartFile.getOriginalFilename().contains(".")) {
                            allianceImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                        }

//                        if (loginUser != null) {
//                            awardsImage.setCreate_id(String.valueOf(loginUser.getUser_email()));
//                        }
                        allianceImage.setUploadId("none");//시큐리티 붙으면 삭제

                        service.insertAllianceAttachFile(allianceImage);
                    }
                }
            }

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return returnMap;
    }

    @PostMapping("/alliance-delete")
    @ResponseBody
    public HashMap<String, Object> alliance_delete_ajax(@RequestParam HashMap<String, Object> paramMap){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            service.deleteAllianceContents(paramMap);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
        }

        return returnMap;
    }

    @PostMapping("/alliance-restore")
    @ResponseBody
    public HashMap<String, Object> alliance_restore_ajax(@RequestParam HashMap<String, Object> paramMap){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            service.restoreAllianceContents(paramMap);

            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
        }

        return returnMap;
    }

    @PostMapping("/alliance-modify")
    @ResponseBody
    public HashMap<String, Object> alliance_modify_ajax(MultipartHttpServletRequest request) {
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            //상장 등록
            SettingAllianceContents allianceContents = new SettingAllianceContents();
            allianceContents.setId( Integer.parseInt( request.getParameter("content_id") ) );
            allianceContents.setRegDate( request.getParameter("regDttm") );
            allianceContents.setTitle( request.getParameter("title") );
            allianceContents.setLink( request.getParameter("link") );
            allianceContents.setUseYn( request.getParameter("use_yn") );
            service.updateAllianceContents(allianceContents);

            if (request.getFile("attach") != null) {
                List<MultipartFile> multipartFiles = request.getFiles("attach");
                if (request.getFile("attach").getSize() > 0) {
                	HashMap<String, Object> paramMap = new HashMap<>();
                    //저장 디렉토리 생성 확인
                    File file = new File(externalImageUploadPath);
                    if (!file.exists()) file.mkdirs();
                    //업로드 된 파일들 복사
                    for (MultipartFile multipartFile : multipartFiles) {
                        if (!multipartFile.getContentType().contains("jpg") &&
                        	!multipartFile.getContentType().contains("jpeg") &&
                        	!multipartFile.getContentType().contains("png")) {
                            throw new Exception("jpg, jpeg, png 파일을 첨부해주세요.");
                        }

                        if (multipartFile.getSize() > maxFileSize) {
                            throw new Exception((maxFileSize / 1024 / 1024) + "MB 이하의 파일만 첨부 가능합니다.");
                        }

                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());

                        SettingAllianceImage allianceImage = new SettingAllianceImage();
                        allianceImage.setId(allianceContents.getId());
                        allianceImage.setServiceType("awards");
                        allianceImage.setUploadPath(externalImageUploadPath);
                        allianceImage.setUploadFilename(uploadName);
                        allianceImage.setOriginFilename(multipartFile.getOriginalFilename());
                        allianceImage.setFileSize((int) multipartFile.getSize());
                        allianceImage.setFileMimetype(multipartFile.getContentType());
                        if (multipartFile.getOriginalFilename().contains(".")) {
                            allianceImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                        }

//                        if (loginUser != null) {
//                            awardsImage.setCreate_id(String.valueOf(loginUser.getUser_email()));
//                        }
                        allianceImage.setUploadId("none");//시큐리티 붙으면 삭제

                        paramMap.clear();
                        paramMap.put("id", allianceContents.getId());

                        SettingAllianceImage _old = service.selectOneAllianceAttachFile( paramMap );
                        if(_old != null)
                        	FileInfoUtil.deleteImageFile_real(_old); /*실제 이미지 파일 제거*/

                        service.deleteAllianceAttachFile(allianceImage);
                        service.insertAllianceAttachFile(allianceImage);
                    }
                }
                else if ( request.getFile("attach").getSize() <= 0 && request.getParameter("imageDeleteFlag").equals("imageDelete") ) {
                    SettingAllianceImage allianceImage = new SettingAllianceImage();
                    allianceImage.setId(allianceContents.getId());
                    service.deleteAllianceAttachFile(allianceImage);
                }
            }


            returnMap.put("result", "success");
            returnMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            returnMap.put("result", "fail");
            returnMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }


        return returnMap;
    }

    @GetMapping("/company/list")
    public ModelAndView company(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/company/list");
        mav.addObject("DAPI_KEY", DAPI_KEY);
        mav.addObject("info", service.selectCompanyInfo());
        return mav;
    }

    @PostMapping("/company/info")
    @ResponseBody
    public HashMap<String, Object> companyInfo(@ModelAttribute SettingCompanyInfo companyInfo) {
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            companyInfo.setCreateId(user.getUserEmail());
            service.insertCompanyInfo(companyInfo);
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


    @GetMapping("/navbar/")
    public ModelAndView navbar(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("navbar", service.selectNavbar());
        mav.addObject("menuList", service.selectNavMenuList());
        mav.setViewName("manage/sub/navbar/form");
        return mav;
    }

    @PostMapping("/navbar/save")
    @ResponseBody
    public HashMap<String, Object> navbarSave(@ModelAttribute SettingNavbar navbar, BindingResult bindingResult,
                                              MultipartHttpServletRequest request) {
        HashMap<String, Object> resultMap = new HashMap<>();

        try{
            LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<MultipartFile> multipartFiles;
            if(request.getFile("ci-image") != null) {
                multipartFiles = request.getFiles("ci-image");
                File file = new File(externalImageUploadPath + addPath);
                if (!file.exists()) file.mkdirs();

                for (MultipartFile multipartFile : multipartFiles) {
                    if(multipartFile.getSize() == 0) continue;
                    String uploadName = UUID.randomUUID().toString();
                    multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
                    logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());
                    MainAttachImage attachImage = new MainAttachImage();
                    attachImage.setServiceType("navbar");
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
                    mainManageService.writeMainAttachImage(attachImage);
                    navbar.setCiImageId(attachImage.getFileId());
                    navbar.setCiImageUrl(addPath+uploadName);
                }
            }
            if(navbar.getCiImageId() == 0 || navbar.getCiImageUrl().isEmpty()){
                resultMap.put("result", "fail");
                resultMap.put("message", "파일이 없습니다.");
                return resultMap;
            }

            navbar.setCreateId(user.getUserEmail());
            service.insertNavbar(navbar);

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

    @GetMapping("/setting/alarm/mail")
    public ModelAndView settingAlarmMail(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/alarm/mail/form");
        SettingMailServer setting = service.getAlarmMailServer("inquiry");
        mav.addObject("setting", setting);
        if(setting != null){
            mav.addObject("receiverList", service.getAlarmMailReceiverList(setting.getId()));
        }

        return mav;
    }

    @PostMapping("/setting/alarm/mail-server")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMailServerCreate(SettingMailServer mailServer){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            mailServer.setCreateId(user.getUserEmail());
            service.writeAlarmMailServer(mailServer);
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

    @PutMapping("/setting/alarm/mail-server")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMailServerUpdate(SettingMailServer mailServer){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            mailServer.setLastUpdateId(user.getUserEmail());
            service.updateAlarmMailServer(mailServer);
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

    @PostMapping("/setting/alarm/mail-receiver")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMailReceiverCreate(SettingMailReceiver mailReceiver){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            mailReceiver.setCreateId(user.getUserEmail());
            service.writeAlarmMailReceiver(mailReceiver);
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

    @PutMapping("/setting/alarm/mail-receiver")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMailReceiverUpdate(SettingMailReceiver mailReceiver){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            mailReceiver.setLastUpdateId(user.getUserEmail());
            if(mailReceiver.getUseYn() == null) mailReceiver.setUseYn("N");
            service.modifyAlarmMailReceiver(mailReceiver);

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

    @DeleteMapping("/setting/alarm/mail-receiver")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMailReceiverDelete(SettingMailReceiver mailReceiver){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            mailReceiver.setDeleteId(user.getUserEmail());
            service.deleteAlarmMailReceiver(mailReceiver);

            resultMap.put("result", "success");
            resultMap.put("message", "삭제됐습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }

    @GetMapping("/setting/alarm/message")
    public ModelAndView settingAlarmMessage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/alarm/message/form");
        SettingMessage setting = service.getAlarmMessage("inquiry");
        mav.addObject("setting", setting);
        if(setting != null){
            mav.addObject("receiverList", service.getAlarmMessageReceiverList(setting.getId()));
        }

        return mav;
    }

    @PutMapping("/setting/alarm/message")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMessageUpdate(SettingMessage settingMessage){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            settingMessage.setLastUpdateId(user.getUserEmail());
            if(settingMessage.getUseYn() == null) settingMessage.setUseYn("N");
            service.updateAlarmMessage(settingMessage);
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

    @PostMapping("/setting/alarm/message-receiver")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMessageReceiverCreate(SettingMessageReceiver messageReceiver){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            messageReceiver.setCreateId(user.getUserEmail());
            service.writeAlarmMessageReceiver(messageReceiver);
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

    @PutMapping("/setting/alarm/message-receiver")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMessageReceiverUpdate(SettingMessageReceiver messageReceiver){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            messageReceiver.setLastUpdateId(user.getUserEmail());
            if(messageReceiver.getUseYn() == null) messageReceiver.setUseYn("N");
            service.modifyAlarmMessageReceiver(messageReceiver);

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

    @DeleteMapping("/setting/alarm/message-receiver")
    @ResponseBody
    public HashMap<String, Object> settingAlarmMessageReceiverDelete(SettingMessageReceiver messageReceiver){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            messageReceiver.setDeleteId(user.getUserEmail());
            service.deleteAlarmMessageReceiver(messageReceiver);

            resultMap.put("result", "success");
            resultMap.put("message", "삭제됐습니다.");
        } catch (Exception e) {
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return resultMap;
    }



}
