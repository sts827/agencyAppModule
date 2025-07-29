package kr.co.wayplus.travel.web.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.util.LoggerUtil;
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
import kr.co.wayplus.travel.service.manage.BoardManageService;
import kr.co.wayplus.travel.util.FileInfoUtil;

@Controller
@RequestMapping("/manage/board")
public class BoardManageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;

	final String addPath = "images/";

    @Value("${upload.file.max-size}")
    int maxFileSize;

    private final BoardManageService svcBorad;
    public BoardManageController(BoardManageService svc1) {
        this.svcBorad = svc1;
    }

//	<!--################################### BoardSetting ###################################-->
    @GetMapping("/setting/list")
    public ModelAndView boardSetting_list(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/board/setting/list");
        return mav;
    }

    @GetMapping("/setting/form")
    public ModelAndView boardSetting_view(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		@RequestParam(value="id", defaultValue="0") String id){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/sub/board/setting/form");

        if(mode.equals("U")) {
        	HashMap<String, Object> paramMap = new HashMap<>();
        	paramMap.put("id", id);
        	mav.addObject("board",  svcBorad.selectOneBoardSetting(paramMap));
        	mav.addObject("p", paramMap);
        } else {
        	mav.addObject("board",  new BoardSetting());
        }
        mav.addObject("mode", mode);

        return mav;
    }

    @PostMapping("/setting-list")
    @ResponseBody
    public HashMap<String, Object> boardSetting_list_ajax(HttpServletRequest request, BoardSetting bc,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="boardId") String boardId,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

    		List<SortData> listSort = getListOrder(request);
    		paramMap.put("listSort", listSort);

    		if(length >= 0) {
				paramMap.put("itemStartPosition", start);
				paramMap.put("pagePerSize", length);
    		}

			paramMap.put("boardId", boardId);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

    		int totalCount = svcBorad.selectCountBoardSetting(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", svcBorad.selectListBoardSetting(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/setting-save")
    @ResponseBody
    public HashMap<String, Object> boardSetting_save_ajax(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		BoardSetting bc
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
//        		HashMap<String, Object> paramMap = new HashMap<>();

	    		if(mode.equals("I")) {
	    			bc.setCreateId(user.getUserEmail());
	    			svcBorad.insertBoardSetting(bc);
	    		} else {
	    			bc.setLastUpdateId(user.getUserEmail());
	    			svcBorad.updateBoardSetting(bc);
	    		}

	    		retrunMap.put("result", "success");
	    		retrunMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retrunMap.put("result", "fail");
	    		retrunMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
		} catch (Exception e) {
			retrunMap.put("result", "error");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			retrunMap.put("info", e.getMessage());
			logger.debug(e.getCause().getLocalizedMessage());
		}
        return retrunMap;
    }

    @PostMapping("/setting-restore")
    @ResponseBody
    public HashMap<String, Object> boardSetting_restore_ajax(
    		BoardSetting bs
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		svcBorad.restoreBoardSetting(bs);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}


        return retrunMap;
    }

    @PostMapping("/setting-delete")
    @ResponseBody
    public HashMap<String, Object> boardSetting_delete_ajax(
    		BoardSetting bc
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		svcBorad.deleteBoardSetting(bc);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }
//	<!--################################### BoardContents ###################################-->
    @GetMapping("/contents/list")
    public ModelAndView board_list(@RequestParam(value="id", defaultValue="0") int id){
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("deleteYn","N");
		ArrayList<BoardSetting> boardList = svcBorad.selectListBoardSetting(paramMap);
		mav.addObject("listBoard", boardList);
		if(id == 0 && !boardList.isEmpty()) {
			mav.addObject("board", boardList.get(0));
		}else{
			boolean init = false;
			for(BoardSetting board : boardList){
				if(board.getId() == id) {
					mav.addObject("board", board);
					init = true;
				}
			}
			if(!init && !boardList.isEmpty()) mav.addObject("board", boardList.get(0));
		}

		mav.setViewName("manage/sub/board/contents/list");
		return mav;
    }

    @GetMapping("/contents/form")
	public ModelAndView board_view(
			@RequestParam(value="mode", defaultValue="I") String mode,
			@RequestParam(value="bid", defaultValue="0") int bid,
			@RequestParam(value="id", defaultValue="0") String id){
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("deleteYn","N");
		paramMap.put("boardId", bid);

		ArrayList<BoardSetting> listBoard = svcBorad.selectListBoardSetting(paramMap);
		if(bid == 0 && !listBoard.isEmpty()) {
			mav.addObject("setting", listBoard.get(0));
			mav.setViewName("manage/sub/board/contents/form");
		}else{
			boolean init = false;
			for(BoardSetting board : listBoard){
				if(board.getId() == bid) {
					mav.addObject("setting", board);
					init = true;

					switch (board.getTypeCode()){
						case "qna":
							paramMap.put("contentId", id);
							mav.addObject("comment", svcBorad.selectOneBoardComment(paramMap));
							mav.setViewName("manage/sub/board/contents/qna-form");
							break;
						case "review":
							mav.setViewName("manage/sub/board/contents/review-form");
							break;
						case "faq":
						case "board":
						default:
							mav.setViewName("manage/sub/board/contents/form");
							break;
					}
				}
			}
			if(!init && !listBoard.isEmpty()) mav.addObject("setting", listBoard.get(0));
		}
		mav.addObject("listBoard",  listBoard);

		if(mode.equals("U")) {
			paramMap.put("id", id);
			mav.addObject("board",  svcBorad.selectOneBoardContents(paramMap));
			mav.addObject("p", paramMap);
		} else {
			mav.addObject("board",  new BoardContents());
		}
		mav.addObject("mode", mode);
		return mav;
	}

    @GetMapping("/contents-attchlist")
    @ResponseBody
    public HashMap<String, Object> board_attachlist(@RequestParam(value="id", defaultValue="0") String id){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();
    		paramMap.put("contentId", id);
    		retrunMap.put("listAttach",  svcBorad.selectListBoardAttachFile(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "error");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }

    @GetMapping("/contents-count")
    @ResponseBody
    public HashMap<String, Object> board_count_ajax(HttpServletRequest request, BoardContents bc,
    		@Param(value="boardId") String boardId,
    		@Param(value="boardIdLike") String boardIdLike,
    		@Param(value="isOnlyReview") Boolean isOnlyReview
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

			paramMap.put("boardId", boardId);
			paramMap.put("boardIdLike", boardIdLike);
			paramMap.put("isOnlyReview", isOnlyReview);

    		int totalCount = svcBorad.selectCountBoardContents(paramMap);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }
    @PostMapping("/contents-list")
    @ResponseBody
    public HashMap<String, Object> board_list_ajax(HttpServletRequest request, BoardContents bc,
    		@RequestParam(value="start", defaultValue="0") int start,
    		@RequestParam(value="length", defaultValue="10") int length,
    		@Param(value="boardId") String boardId,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike
    		){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		HashMap<String, Object> paramMap = new HashMap<>();

    		List<SortData> listSort = getListOrder(request);
    		paramMap.put("listSort", listSort);

    		if(length >= 0) {
				paramMap.put("itemStartPosition", start);
				paramMap.put("pagePerSize", length);
    		}

			paramMap.put("boardId", boardId);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

    		int totalCount = svcBorad.selectCountBoardContents(paramMap);

    		retrunMap.put("recordsTotal", totalCount);
    		retrunMap.put("recordsFiltered", totalCount);
    		retrunMap.put("data", svcBorad.selectListBoardContents(paramMap));

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			logger.error(e.getMessage());
		}

        return retrunMap;
    }

    @PostMapping("/contents-save")
    @ResponseBody
    public HashMap<String, Object> board_save_ajax(
    		@RequestParam(value="mode", defaultValue="I") String mode,
    		BoardContents bc,
    		MultipartHttpServletRequest request
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if(_user instanceof LoginUser) {
        		LoginUser user = (LoginUser)_user;
        		HashMap<String, Object> paramMap = new HashMap<>();

        		String thumnailUrl = "";

	    		if(mode.equals("I")) {
	    			bc.setCreateId( user.getUserEmail());
	    			svcBorad.insertBoardContents(bc);
	    		} else {
	    			bc.setLastUpdateId( user.getUserEmail());
	    			svcBorad.updateBoardContents(bc);
	    		}
	    		List<MultipartFile> multipartFiles = null;

	    		Object obj = request.getParameterValues("deletes");

	    		if(request.getParameterValues("deletes") != null) {
	    			String[] deletes = request.getParameterValues("deletes");

	    			for (String strDeletes : deletes) {
	    				logger.debug(strDeletes);
	    				paramMap.clear();
	    				paramMap.put("fileId", strDeletes);

	    				BoardAttachFile _old = svcBorad.selectOneBoardAttachFile( paramMap );
	    				if(_old != null) {
	    					FileInfoUtil.deleteImageFile_real(_old); /*실제 이미지 파일 제거*/
	    					svcBorad.deleteBoardAttachFile(_old);
	    				}
					}
	    		}

	    		if(request.getFile("attachs") != null) {
	    			multipartFiles = request.getFiles("attachs");

	    			MultipartFile multipartFilesThumnail = request.getFile("thumnail");

	                if (multipartFiles.size() > 0) {
	                	File file = new File(externalImageUploadPath + addPath);
	                	if (!file.exists()) file.mkdirs();

	                    for (MultipartFile multipartFile : multipartFiles) {
	                        String uploadName = UUID.randomUUID().toString();
	                        multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
	                        logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());

	                        BoardAttachFile baf = new BoardAttachFile();
	                        baf.setBoardId(bc.getBoardId());
	                        baf.setContentId(bc.getId());
	                        baf.setServiceType("board");
	                        baf.setUploadPath(externalImageUploadPath+ addPath);
	                        baf.setUploadFilename(uploadName);
	                        baf.setOriginFilename(multipartFile.getOriginalFilename());
	                        baf.setFileSize((int) multipartFile.getSize());
	                        baf.setFileMimetype(multipartFile.getContentType());
	                        if (multipartFile.getOriginalFilename().contains(".")) {
	                            baf.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
	                        }

	                        paramMap.clear();
	                        paramMap.put("contentId", bc.getId());

	                        if (user != null) {
	                            baf.setUploadId(String.valueOf(user.getUserEmail()));
	                        }

	                        svcBorad.insertBoardAttachFile(baf);

	                        if(multipartFilesThumnail != null) {
	                        	if(multipartFile.getOriginalFilename().equals( multipartFilesThumnail.getOriginalFilename() )) {
//			                        bc.setThumbnailUrl("/upload/images/"+uploadName);
//			            			svcBorad.updateBoardContents(bc);
			            			thumnailUrl = "/upload"+ addPath +uploadName;
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
        			bc.setThumbnailUrl("/upload/"+ addPath +thumnailUrl);
        			svcBorad.updateBoardContents(bc);
        		}

	    		retrunMap.put("result", "success");
	    		retrunMap.put("message", "처리가 완료 되었습니다.");
        	} else {
        		retrunMap.put("result", "fail");
	    		retrunMap.put("message", "로그인 문제가 발생되었습니다.");
        	}
		} catch (Exception e) {
			retrunMap.put("result", "error");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			retrunMap.put("info", e.getMessage());
			logger.error(e.getCause().getMessage());
		}
        return retrunMap;
    }

    @PostMapping("/contents-restore")
    @ResponseBody
    public HashMap<String, Object> board_restore_ajax(
    		BoardContents bc
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		svcBorad.restoreBoardContents(bc);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}


        return retrunMap;
    }

    @PostMapping("/contents-delete")
    @ResponseBody
    public HashMap<String, Object> board_delete_ajax(
    		BoardContents bc
    	){
    	HashMap<String, Object> retrunMap = new HashMap<>();

    	try {
    		svcBorad.deleteBoardContents(bc);

    		retrunMap.put("result", "success");
    		retrunMap.put("message", "처리가 완료 되었습니다.");
		} catch (Exception e) {
			retrunMap.put("result", "fail");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
		}

        return retrunMap;
    }

	@PostMapping("/comment")
	@ResponseBody
	public HashMap<String, Object> boardCommentSave(
			@RequestParam(value="mode", defaultValue="I") String mode,
			BoardComment bc, BindingResult bindingResult,
			MultipartHttpServletRequest request
	){
		HashMap<String, Object> retrunMap = new HashMap<>();
		try {
			LoggerUtil.writeBindingResultErrorLog(bindingResult, logger);

			Object _user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(_user instanceof LoginUser) {
				LoginUser user = (LoginUser)_user;
				HashMap<String, Object> paramMap = new HashMap<>();
				if(bc.getSecretYn().isEmpty() || bc.getSecretYn().equals("A"))
					bc.setSecretYn(null);

				if(mode.equals("I")) {
					bc.setCreateId( user.getUserEmail());
					svcBorad.insertBoardComment(bc);
				} else {
					bc.setLastUpdateId(user.getUserEmail());
					svcBorad.updateBoardComment(bc);
				}

				retrunMap.put("result", "success");
				retrunMap.put("message", "처리가 완료 되었습니다.");
			} else {
				retrunMap.put("result", "fail");
				retrunMap.put("message", "로그인이 필요합니다.");
			}
		} catch (Exception e) {
			retrunMap.put("result", "error");
			retrunMap.put("message", "처리중 문제가 발생했습니다.");
			retrunMap.put("info", e.getMessage());
			logger.error(e.getCause().getMessage());
		}
		return retrunMap;
	}
}
