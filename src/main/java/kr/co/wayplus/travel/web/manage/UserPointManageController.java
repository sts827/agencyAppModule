package kr.co.wayplus.travel.web.manage;

import jakarta.websocket.server.PathParam;
import kr.co.wayplus.travel.model.*;
import kr.co.wayplus.travel.service.user.UserPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Controller
@RequestMapping("/manage/point")
public class UserPointManageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserPointService userPointService;

    public UserPointManageController(UserPointService userPointService) {
        this.userPointService = userPointService;
    }

    @GetMapping("/setting")
    public ModelAndView pointSetting(@RequestParam(value="startDate",defaultValue="") String startDate,
                                  @RequestParam(value="endDate",defaultValue="") String endDate,
                                  @RequestParam(value="page",defaultValue="1") int page,
                                  @RequestParam(value="pageSize",defaultValue="10") int pageSize){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);

        int totalCount = userPointService.getUserPointSettingListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("paging", paging);
        mav.addObject("pointSetList", userPointService.getUserPointSettingList(param));
        mav.setViewName("manage/sub/user/point/setting");
        return mav;
    }

    @GetMapping("/setting/{id}")
    public ModelAndView pointSettingDetail(@PathVariable(value="id") String id){
        ModelAndView mav = new ModelAndView();
        mav.addObject("point", userPointService.getUserPointSettingById(id));
        mav.setViewName("manage/sub/user/point/setting-form");
        return mav;
    }

    @PutMapping("/setting/update")
    @ResponseBody
    public HashMap<String, Object> pointSettingUpdate(@ModelAttribute UserPointSet pointSet, BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            pointSet.setLastUpdateId(user.getUserEmail());
            if(!pointSet.getExpiredDay().isEmpty() && !pointSet.getExpiredDayType().isEmpty()) {
                pointSet.setExpiredDay(pointSet.getExpiredDay() + pointSet.getExpiredDayType());
            }else{
                pointSet.setExpiredDay(null);
            }
            if(pointSet.getStartDate() != null && pointSet.getStartDate().isEmpty()) pointSet.setStartDate(null);
            if(pointSet.getExpireDate() != null && pointSet.getExpireDate().isEmpty()) pointSet.setExpireDate(null);
            if(pointSet.getUseYn() == null) pointSet.setUseYn("N");
            userPointService.updateUserPointSetting(pointSet);

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

    @GetMapping("/record/list")
    public ModelAndView pointRecordList(@RequestParam(value="accruedType",defaultValue="ALL") String accruedType,
                                  @RequestParam(value="searchType",defaultValue="") String searchType,
                                  @RequestParam(value="startDate",defaultValue="") String startDate,
                                  @RequestParam(value="endDate",defaultValue="") String endDate,
                                  @RequestParam(value="page",defaultValue="1") int page,
                                  @RequestParam(value="pageSize",defaultValue="10") int pageSize,
                                  @RequestParam(value="searchKey",defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> param = new HashMap<>();
        param.put("accruedType", accruedType);
        param.put("searchType", searchType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("searchKey", searchKey);

        int totalCount = userPointService.getUserPointRecordListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("paging", paging);
        mav.addObject("pointList", userPointService.getUserPointRecordList(param));
        mav.setViewName("manage/sub/user/point/record");
        return mav;
    }

    @GetMapping("/record/form")
    public ModelAndView pointRecordForm(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("pointCode",  userPointService.getCodeItemList("pointType"));
        mav.setViewName("manage/sub/user/point/record-form");
        return mav;
    }

    @PostMapping("/record/create")
    @ResponseBody
    public HashMap<String, Object> pointRecordCreate(@ModelAttribute UserPointAccrued pointAccrued, BindingResult bindingResult){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            pointAccrued.setCreateId(user.getUserEmail());
            userPointService.createUserPoint(pointAccrued);
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

    @PutMapping("/record/cancel")
    @ResponseBody
    public HashMap<String, Object> pointRecordCancel(@RequestParam(value="id", defaultValue="0") int id,
                                                     @RequestParam(value="accruedId", defaultValue="0") int accruedId,
                                                     @RequestParam(value="cancelCode", defaultValue="0") String code){
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserPointAccrued pointAccrued = new UserPointAccrued();
            pointAccrued.setCancelId(user.getUserEmail());
            pointAccrued.setCancelCode(code);
            pointAccrued.setId(accruedId);
            userPointService.cancelUserPoint(id, pointAccrued);

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

    @GetMapping("/record/user")
    public ModelAndView pointRecordUser(@RequestParam(value="page",defaultValue="1") int page,
                                        @RequestParam(value="pageSize",defaultValue="10") int pageSize,
                                        @RequestParam(value="userEmail",defaultValue="") String userEmail,
                                        @RequestParam(value="userName",defaultValue="") String userName){
        ModelAndView mav = new ModelAndView();

        if(!userEmail.isEmpty()) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("userEmail", userEmail);
            param.put("userName", userName);

            int totalCount = userPointService.getUserPointRecordListCount(param);
            PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
            param.put("itemStartPosition", paging.getItemStartPosition());
            param.put("pagePerSize", paging.getPagePerSize());
            mav.addObject("p", param);
            mav.addObject("paging", paging);
            mav.addObject("point", userPointService.getUserPointRecordSummary(param));
            mav.addObject("pointList", userPointService.getUserPointRecordList(param));
        }
        mav.setViewName("manage/sub/user/point/record-user");
        return mav;
    }

    @GetMapping("/accrued")
    public ModelAndView pointAccruedList(@RequestParam(value="searchType",defaultValue="") String searchType,
                                  @RequestParam(value="startDate",defaultValue="") String startDate,
                                  @RequestParam(value="endDate",defaultValue="") String endDate,
                                  @RequestParam(value="page",defaultValue="1") int page,
                                  @RequestParam(value="pageSize",defaultValue="10") int pageSize,
                                  @RequestParam(value="searchKey",defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("searchKey", searchKey);

        int totalCount = userPointService.getUserPointAccruedListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("paging", paging);
        mav.addObject("pointList", userPointService.getUserPointAccruedList(param));
        mav.setViewName("manage/sub/user/point/accrued");
        return mav;
    }

    @GetMapping("/used")
    public ModelAndView pointUsedList(@RequestParam(value="searchType",defaultValue="") String searchType,
                                  @RequestParam(value="startDate",defaultValue="") String startDate,
                                  @RequestParam(value="endDate",defaultValue="") String endDate,
                                  @RequestParam(value="page",defaultValue="1") int page,
                                  @RequestParam(value="pageSize",defaultValue="10") int pageSize,
                                  @RequestParam(value="searchKey",defaultValue="") String searchKey){
        ModelAndView mav = new ModelAndView();

        HashMap<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("searchKey", searchKey);

        int totalCount = userPointService.getUserPointUsedListCount(param);
        PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
        param.put("itemStartPosition", paging.getItemStartPosition());
        param.put("pagePerSize", paging.getPagePerSize());
        mav.addObject("p", param);
        mav.addObject("paging", paging);
        mav.addObject("pointList", userPointService.getUserPointUsedList(param));
        mav.setViewName("manage/sub/user/point/used");
        return mav;
    }

}
