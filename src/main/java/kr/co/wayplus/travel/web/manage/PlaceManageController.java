package kr.co.wayplus.travel.web.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.model.BoardAttachFile;
import kr.co.wayplus.travel.model.BoardSetting;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.PlaceSpot;
import kr.co.wayplus.travel.model.PlaceSpotImage;
import kr.co.wayplus.travel.model.SortData;
import kr.co.wayplus.travel.service.manage.PlaceManageService;
import kr.co.wayplus.travel.util.FileInfoUtil;

@Controller
@RequestMapping("/manage/place")
public class PlaceManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

	final String addPath = "place/";

    @Value("${upload.file.max-size}")
    int maxFileSize;

    @Value("${key.api.kakao.rest}")
    private String DAPI_KEY;

    private final PlaceManageService svcPlace;
    public PlaceManageController(PlaceManageService svc1) {
        this.svcPlace = svc1;
    }
////<!--################################### placeSpot ###################################-->
    @GetMapping("/spot/list")
    public ModelAndView placeSpot(
    		@RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="pageSize", defaultValue="10") int pageSize,
		    @RequestParam(value="searchType", defaultValue="") String searchType,
		    @RequestParam(value="searchKey", defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> paramMap = new HashMap<String, Object>();

    	mav.setViewName("manage/sub/place/spot/list");
    	return mav;
    }

    @GetMapping("/spot/form")
    public ModelAndView placeSpot_form(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		@RequestParam(value="id", defaultValue="0") String id){
        ModelAndView mav = new ModelAndView();

        if(mode.equals("U")) {
        	HashMap<String, Object> paramMap = new HashMap<>();
        	paramMap.put("tsId", id);
        	mav.addObject("PlaceSpot",  svcPlace.selectOnePlaceSpot(paramMap));
        	mav.addObject("p", paramMap);
        } else {
        	mav.addObject("PlaceSpot",  new PlaceSpot());
        }
        mav.addObject("mode", mode);

        mav.setViewName("manage/sub/place/spot/form");
        return mav;
    }

    @PostMapping("/spot-list")
    @ResponseBody
   	public HashMap<String, Object> placeSpot_list(
   			HttpServletRequest request,
   			PlaceSpot ts,
   			@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="spotId") String spotId,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike){
   		HashMap<String, Object> resultMap = new HashMap<>();

   		try{
   			HashMap<String, Object> paramMap = new HashMap<>();

   			List<SortData> listSort = getListOrder(request);
   			paramMap.put("listSort", listSort);
   			if(length >= 0) {
   				paramMap.put("itemStartPosition", start);
   				paramMap.put("pagePerSize", length);
   			}

   			paramMap.put("tsId", spotId);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

   			int totalCount = svcPlace.selectCountPlaceSpot(paramMap);
   			ArrayList<PlaceSpot> lists = svcPlace.selectListPlaceSpot(paramMap);

	        resultMap.put("recordsTotal", totalCount);
	  		resultMap.put("recordsFiltered", totalCount);
	  		resultMap.put("data", lists);

   			resultMap.put("result", "success");
   			resultMap.put("message", "처리되었습니다.");
   		}catch (Exception e){
   			logger.error(e.getMessage());
   			resultMap.put("result", "error");
//   			resultMap.put("message", "처리중 오류가 발생하였습니다.");
   			resultMap.put("error", e.getMessage());
   			resultMap.put("message", e.getMessage());
   		}

   		return resultMap;
   	}

    @PostMapping("/spot-save")
	@ResponseBody
	public HashMap<String, Object> place_save(
			PlaceSpot ps,
			BindingResult bindingResult,
			MultipartHttpServletRequest request){
		HashMap<String, Object> resultMap = new HashMap<>();
		try{
			LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			HashMap<String, Object> paramMap = new HashMap<>();
			HashMap<String, Integer> mapOrders = new HashMap<>();
			String thumnailUrl = "";

			if (ps.getTsWriteId() == null) {
				ps.setTsWriteId(user.getUserEmail());
			}

			if (ps.getTsId() == null){
				if (ps.getTsUdateId() == null) {
					ps.setTsUdateId(user.getUserEmail());
				}
				svcPlace.insertPlaceSpot(ps);
			}else {
				ps.setTsUdateId(user.getUserEmail());
				svcPlace.updatePlaceSpot(ps);
			}

			List<MultipartFile> multipartFiles = null;

    		if(request.getParameterValues("deletes") != null) {
    			String[] deletes = request.getParameterValues("deletes");

    			for (String strDeletes : deletes) {
    				logger.debug(strDeletes);
    				paramMap.clear();
    				paramMap.put("fileId", strDeletes);

    				PlaceSpotImage _old = svcPlace.selectOnePlaceSpotImage( paramMap );
    				if(_old != null) {
    					FileInfoUtil.deleteImageFile_real(_old); /*실제 이미지 파일 제거*/
    					svcPlace.deletePlaceSpotImage(_old);
    				}
				}
    		}


    		if(request.getParameterValues("orders") != null) {
    			String[] files = request.getParameterValues("orders");
    			//String[] sorts = request.getParameterValues("orders_sort");
    			for( int i = 0; i < files.length;i++ ) {

    				String[] arrOrders = files[i].split(",");

    				if( arrOrders[0].indexOf("N") == -1 ) {
    					//logger.info( orders[i] );
    					PlaceSpotImage psi = new PlaceSpotImage()
    						.addFileId(    Integer.valueOf(arrOrders[0]) )
    						.addSortOrder( Integer.valueOf(arrOrders[1]) );

    					svcPlace.updatePlaceSpotImage(psi);
    				} else {
    					mapOrders.put( arrOrders[2], Integer.valueOf(arrOrders[1]));
    				}
    			}
    		}

    		if(request.getFile("attachs") != null) {
    			multipartFiles = request.getFiles("attachs");

    			MultipartFile multipartFilesThumnail = request.getFile("thumnail");

                if (multipartFiles.size() > 0) {
                	File file = new File(externalImageUploadPath+ addPath);
                	if (!file.exists()) file.mkdirs();


                    for (MultipartFile multipartFile : multipartFiles) {
                        String uploadName = UUID.randomUUID().toString();
                        multipartFile.transferTo(new File(externalImageUploadPath + addPath + uploadName));
                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());

                        PlaceSpotImage psi = new PlaceSpotImage();
                        psi.setTsId(ps.getTsId());
                        psi.setServiceType("place");
                        psi.setUploadPath(externalImageUploadPath+ addPath);
                        psi.setUploadFilename(uploadName);
                        psi.setOriginFilename(multipartFile.getOriginalFilename());
                        psi.setFileSize((int) multipartFile.getSize());
                        psi.setFileMimetype(multipartFile.getContentType());

                        Integer sortOrder = -1;
                        if( mapOrders.containsKey( multipartFile.getOriginalFilename()) ) {
                        	sortOrder = mapOrders.get( multipartFile.getOriginalFilename());
                        }

                        psi.setSortOrder( sortOrder );
                        if (multipartFile.getOriginalFilename().contains(".")) {
                            psi.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
                        }

                        paramMap.clear();
                        //paramMap.put("content_id", ps.getTsId());

                        if (user != null) {
                            psi.setUploadId(String.valueOf(user.getUserEmail()));
                        }

                        svcPlace.insertPlaceSpotImage(psi);

                        if(multipartFilesThumnail != null) {
                        	if(multipartFile.getOriginalFilename().equals( multipartFilesThumnail.getOriginalFilename() )) {
//		                        ps.setTsAppend("/upload/images/"+uploadName);
//		            			svcPlace.updatePlaceSpot(ps);
                        		thumnailUrl = uploadName;
                        	}
                        }
                    }
                }
    		}



    		boolean isThum = false;

    		if(request.getParameter("thumnail") != null ) {
    			isThum = true;
    			thumnailUrl = request.getParameter("thumnail");
    		} else if( thumnailUrl.length() != 0 ) {
    			isThum = true;
    		}

    		if(isThum) {
    			ps.setTsAppend("/upload/"+ addPath +thumnailUrl);
    			svcPlace.updatePlaceSpot(ps);
    		}

			resultMap.put("result", "success");
			resultMap.put("message", "처리되었습니다.");
		}catch (Exception e){
			logger.error(e.getCause().getMessage() );
			resultMap.put("result", "error");
			resultMap.put("message", e.getMessage());

		}

		return resultMap;
	}
    @PostMapping("/spot/delete")
	@ResponseBody
	public HashMap<String, Object> place_delete(PlaceSpot ts){
		HashMap<String, Object> resultMap = new HashMap<>();
		try{
			svcPlace.deletePlaceSpot(ts);

			resultMap.put("result", "success");
			resultMap.put("message", "처리되었습니다.");
		}catch (Exception e){
			logger.error(e.getMessage());
			resultMap.put("result", "error");
			resultMap.put("error", e.getMessage());
		}

		return resultMap;
	}

    @GetMapping("/spot-attchlist")
    @ResponseBody
    public HashMap<String, Object> board_attachlist(@RequestParam(value="id", defaultValue="0") String id){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();
    		paramMap.put("tsId", id);
    		retrunMap.put("listAttach",  svcPlace.selectListPlaceSpotImage(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "error");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }

}
