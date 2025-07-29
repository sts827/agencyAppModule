package kr.co.wayplus.travel.web.front;

import java.util.HashMap;
import java.util.List;

import kr.co.wayplus.travel.model.*;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.wayplus.travel.base.web.BaseController;
import kr.co.wayplus.travel.service.front.BoardService;
import kr.co.wayplus.travel.service.front.PageService;

@Controller
@RequestMapping("/board")
public class BoardController  extends BaseController {

    @Value("${cookie-set.domain}")
    private String cookieDomain;
    @Value("${cookie-set.prefix}")
    private String cookieName;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PageService pageService;
    private final BoardService boardService;

    @Autowired
    public BoardController(PageService pageService,BoardService boardService) {
    	this.pageService = pageService;
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public ModelAndView boardList(
    		HttpServletRequest request,
    		BoardContents bc,
    		@RequestParam(value="page", defaultValue="1") int page,
    		@RequestParam(value="pageSize", defaultValue="5") int pageSize,
    		@Param(value="boardId") String boardId,
    		@Param(value="boardUrl") String boardUrl,
    		@Param(value="titleLike") String titleLike,
    		@Param(value="contentLike") String contentLike){
        ModelAndView mav = new ModelAndView();

        try {
    		HashMap<String, Object> paramMap = new HashMap<>();

    		List<SortData> listSort = getListOrder(request);
    		paramMap.put("listSort", listSort);

			paramMap.put("boardId", boardId);
			paramMap.put("boardUrl", boardUrl);
			paramMap.put("titleLike", titleLike);
			paramMap.put("contentLike", contentLike);

    		int totalCount = boardService.selectCountBoardContents(paramMap);

    		PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
			paramMap.put("itemStartPosition", paging.getItemStartPosition());
			paramMap.put("pagePerSize", paging.getPagePerSize());

            mav.addObject("p", paramMap);
            mav.addObject("boardList", boardService.selectListBoardContents(paramMap));
            mav.addObject("paging", paging);

		} catch (Exception e) {
			mav.addObject("boardList", null);
			logger.error(e.getMessage());
		}

        mav.setViewName("test_board_list");
        return mav;
    }

    @GetMapping("/view")
    public ModelAndView boardView(
    		HttpServletRequest request,
    		BoardContents bc,
    		@RequestParam(value="id") Integer id ){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> paramMap = new HashMap<>();

        paramMap.put("id", id);
        paramMap.put("boardId", id);

        mav.addObject("p", paramMap);
        mav.addObject("content", boardService.selectOneBoardContents(paramMap));

        mav.addObject("attachList", boardService.selectListBoardAttachFile(paramMap));

        mav.setViewName("test_board_view");
        return mav;
    }

    @PostMapping("/review-write")
    @ResponseBody
    public HashMap<String, Object> reviewWrite(
            @ModelAttribute BoardContents bc,
            MultipartHttpServletRequest request
    ){
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("typeCode", "review");
            BoardSetting boardSetting = boardService.selectOneBoardSetting(paramMap);

            LoginUser user = new LoginUser();
            if ( SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser") ) {
                bc.setCreateId("guest");
            }
            else {
                user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                bc.setCreateId(user.getUserEmail());
            }

            bc.setCategoryId(boardSetting.getId());
            boardService.insertReviewContent(bc, user, request);

            returnMap.put("result", "success");
            returnMap.put("message", "등록 되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("result", "error");
            returnMap.put("message", "처리중 문제가 발생했습니다.");
            returnMap.put("info", e.getMessage());
            logger.error(e.getCause().getMessage());

        }
        return returnMap;
    }

}
