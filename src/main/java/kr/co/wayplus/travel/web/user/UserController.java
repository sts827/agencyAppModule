package kr.co.wayplus.travel.web.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.model.LoginUser;
import kr.co.wayplus.travel.model.LoginUserSession;
import kr.co.wayplus.travel.model.MenuUser;
import kr.co.wayplus.travel.model.PagingDTO;
import kr.co.wayplus.travel.model.Policy;
import kr.co.wayplus.travel.service.front.PageService;
import kr.co.wayplus.travel.service.user.UserPointService;
import kr.co.wayplus.travel.service.user.UserService;
import kr.co.wayplus.travel.util.CookieUtil;
import kr.co.wayplus.travel.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserPointService userPointService;
    private final PageService pageService;
    private final CookieUtil cookieUtil;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${key.crypto.encrypt}")
    private String encrypt;
    @Value("${key.crypto.iv}")
    private String iv;

    @Value("${cookie-set.domain}")
    private String cookieDomain;
    @Value("${cookie-set.prefix}")
    private String cookiePrefix;
    @Value("${cookie-set.tracking-age}")
    private int trackerMaxAge;

    @Autowired
    public UserController(UserService userService, UserPointService userPointService, PageService pageService, CookieUtil cookieUtil) {
        this.userService = userService;
        this.userPointService = userPointService;
        this.pageService = pageService;
        this.cookieUtil = cookieUtil;
    }

    /**
     * 로그인 페이지 호출
     * @param error 로그인 실패시 전달 받은 오류 메시지
     * @param code 로그인 실패시 전달 받은 오류 코드
     * @return Login Page
     */
    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request,
          HttpSession session,
          @RequestParam(value="error", defaultValue="n") String error,
          @RequestParam(value="code", defaultValue="n") String code){
        logger.debug("Get Login Page.");

        ModelAndView mav = new ModelAndView();

        if (checkLogin(session)) {
            return new ModelAndView(new RedirectView("/"));
        }

        // 로그인 전문 전송시 사용할 AES 암호화 키 생성
        setSessionEncryptKey(session);

        // 아이디 저장 사용 시 쿠키에 저장된 아이디 읽어옴
        Cookie savedLoginIdCookie = cookieUtil.getCookieByName(request, "login.id");
        if(savedLoginIdCookie != null){
            mav.addObject("loginId", savedLoginIdCookie.getValue());
        }

        // 로그인 에러 메시지 처리
        if(error.equals("y")){
            switch (code) {
                case "NON" -> mav.addObject("message", "존재하지 않는 사용자입니다.");
                case "PAS" -> mav.addObject("message", "비밀번호가 일치하지 않습니다.");
                case "LOC" -> mav.addObject("message", "잠긴 계정입니다.");
                case "DIS" -> mav.addObject("message", "비활성화 된 계정입니다.");
                case "EXD" -> mav.addObject("message", "만료된 계정입니다.");
                case "EXP" -> mav.addObject("message", "비밀번호가 만료된 계정입니다.");
                default -> mav.addObject("message", "로그인에 실패했습니다.");
            }
        }


        mav.setViewName("user/login");
        return mav;

    }

    /**
     * 사용자 로그 아웃 입력 처리
     * @return Redirect Index Page
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session){
        logger.debug("User Action Logout");
        try {
            SecurityContextHolder.clearContext();
            session.invalidate();

            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                //로그인 정보가 있을 경우 기록 작성
                LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                LoginUserSession loginUserSession = new LoginUserSession();
                loginUserSession.setLoginSession(session.getId());
                loginUserSession.setUserEmail(user.getUserEmail());
                loginUserSession.setLogoutType("ACTION");
                userService.updateUserSessionLogout(loginUserSession);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return new ModelAndView(new RedirectView("/user/login"));
    }

    /**
     * 사용자 세션 만료 처리
     * @return Redirect Index Page
     */
    @RequestMapping("/session-expired")
    public ModelAndView sessionExpired(HttpSession session){
        logger.debug("User Session Expired.");
        session.invalidate();
        return new ModelAndView(new RedirectView("/"));
    }

    /**
     * 회원 가입 페이지 호출
     * @return User Join Page
     */
    @GetMapping("/join")
    public ModelAndView join(HttpSession session){
        logger.debug("User Join Page");
        if (checkLogin(session)) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView mav = new ModelAndView();

        // 로그인 전문 전송시 사용할 AES 암호화 키 생성
        setSessionEncryptKey(session);

        mav.setViewName("user/join");
        return mav;
    }

    /**
     * 회원 아이디 찾기 페이지 호출
     * @param session 사용자 세션
     * @return User Find ID Page
     */
    @GetMapping("/find-id")
    public ModelAndView findId(HttpSession session){
        logger.debug("User Find ID Page");
        if (checkLogin(session)) {
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/find-id");
        return mav;
    }

    /**
     * 회원 비밀번호 찾기 페이지 호출
     * @param session 사용자 세션
     * @return User Find Password Page
     */
    @GetMapping("/find-password")
    public ModelAndView findIdResult(HttpSession session){
        logger.debug("User Find Password Page");
        if (checkLogin(session)) {
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView mav = new ModelAndView();

        // 로그인 전문 전송시 사용할 AES 암호화 키 생성
        setSessionEncryptKey(session);

        mav.setViewName("user/find-password");
        return mav;
    }

    /**
     * 아이디 중복 체크
     * @param id 사용자 아이디
     * @return 해당 아이디 사용자 검색 결과 수
     */
    @ResponseBody
    @GetMapping("/check/{id}")
    public Map<String, Object> checkId(@PathVariable String id){
        logger.debug("User Exist Check Start");
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            int count = userService.findUserCountById(id);

            if(count > 0) resultMap.put("duplicate", true);
            else resultMap.put("duplicate", false);
            resultMap.put("result", "success");

        }catch (Exception e){
            logger.debug("User Exist Check Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Exist Check End");
        return resultMap;
    }

    /**
     * 회원 가입 처리
     * @param encrypted 사용자 암호 평문 암호화 여부
     * @param user 등록할 사용자 정보
     * @return 결과 JSON {result: "처리 결과", message: "처리 내용"}
     */
    @ResponseBody
    @PostMapping("/register")
    public Map<String, Object> register(HttpSession session,
                                        HttpServletResponse response,
                                        @RequestParam(value="encrypted", defaultValue="true") String encrypted,
                                        @ModelAttribute LoginUser user, BindingResult bindingResult){
        logger.debug("User Register Start");
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            //필수 정보 확인
            if(user == null
                    || user.getUserEmail() == null
                    || user.getUserPassword() == null
                    || user.getUserName() == null
            ) {
                throw new Exception("사용자 정보를 확인 해 주세요.");
            }

            if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
            if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
            user.setEncrypt(encrypt);
            user.setIv(iv);

            //새로 생성된 ID 트래킹 값 갱신 처리(중복 방지)
            user.setUserTokenId(String.valueOf(UUID.randomUUID()));
            response.addCookie(cookieUtil.createCookie("tracker.id", user.getUserTokenId(), trackerMaxAge));

            userService.addUser(user, Boolean.parseBoolean(encrypted));
            userPointService.createJoinPoint(user);

            resultMap.put("result", "success");
            resultMap.put("message", "등록 완료");
        }catch (Exception e){
            logger.debug("User Register Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }
        logger.debug("User Register End");
        return resultMap;
    }

    /**
     * 회원 탈퇴 처리(DB 분리 보관)
     * @param encrypted 사용자 암호 평문 암호화 처리 여부
     * @param user 탈퇴 사용자 정보
     * @return 결과 JSON {result: "처리 결과", message: "처리 내용"}
     */
    @ResponseBody
    @PostMapping("/withdrawal")
    public Map<String, Object> withdrawal(HttpSession session,
                                        @RequestParam(value="encrypted", defaultValue="true") String encrypted,
                                        @ModelAttribute LoginUser user, BindingResult bindingResult){
        logger.debug("User Withdrawal Start");
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            //필수 정보 확인
            if(user == null
                    || user.getUserEmail() == null
                    || user.getUserPassword() == null
            ) {
                throw new Exception("사용자 정보를 확인 해 주세요.");
            }

            if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
            if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
            user.setEncrypt(encrypt);
            user.setIv(iv);

            userService.withdrawalUser(user, Boolean.parseBoolean(encrypted));

            resultMap.put("result", "success");
            resultMap.put("message", "탈퇴 완료");
        }catch (Exception e){
            logger.debug("User Withdrawal Error");
            resultMap.put("result", "error");
            resultMap.put("message", e.getMessage());
        }
        logger.debug("User Withdrawal End");
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/find-id")
    public Map<String, Object> findId(@RequestParam(value="user_name", defaultValue="") String name,
                                      @RequestParam(value="user_mobile", defaultValue="") String mobile){
        logger.debug("User Find Start");

        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            ArrayList<LoginUser> users = userService.findUserListByUserName(name, mobile);
            resultMap.put("list", users);
            resultMap.put("result", "success");
        }catch (Exception e){
            logger.debug("User Find Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Find End");
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/find-password")
    public Map<String, Object> findPassword(@RequestParam(value="user_email", defaultValue="") String email,
                                            @RequestParam(value="user_name", defaultValue="") String name,
                                            @RequestParam(value="user_mobile", defaultValue="") String mobile){
        logger.debug("User Find Password Start");

        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            HashMap<String, Object> param = new HashMap<>();
            param.put("userEmail", email);
            param.put("userName", name);
            param.put("userMobile", mobile);
            LoginUser user = userService.findRePasswordUserByUserInfo(param);
            resultMap.put("user", user);
            resultMap.put("result", "success");
        }catch (Exception e){
            logger.debug("User Find Password Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Find Password End");
        return resultMap;
    }

    @ResponseBody
    @PutMapping("/find-password-change")
    public Map<String, Object> findPassword(HttpSession session,
            @RequestParam(value="encrypted", defaultValue="true") String encrypted,
            @ModelAttribute LoginUser user, BindingResult bindingResult){
        logger.debug("User Password Update Start");

        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
            if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
            user.setEncrypt(encrypt);
            user.setIv(iv);
            userService.updateUserPasswordByLost(user, Boolean.parseBoolean(encrypted));
            resultMap.put("result", "success");
        }catch (Exception e){
            logger.debug("User Password Update Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Password Update End");
        return resultMap;
    }

    @ResponseBody
    @PutMapping("/update")
    public Map<String, Object> updateUser(HttpSession session,
            @RequestParam(value="encrypted", defaultValue="true") String encrypted,
            @ModelAttribute LoginUser user, BindingResult bindingResult){
        logger.debug("User Update Start");
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            if(user == null
                    || user.getUserEmail() == null
                    || user.getUserPassword() == null
                    || user.getUserName() == null
            ) {
                throw new Exception("사용자 정보를 확인 해 주세요.");
            }
            if(session.getAttribute("encrypt") != null) encrypt = (String) session.getAttribute("encrypt");
            if(session.getAttribute("iv") != null) iv = (String) session.getAttribute("iv");
            user.setEncrypt(encrypt);
            user.setIv(iv);
            user.setUserTokenId(loginUser.getUserTokenId());
            userService.updateUserInfo(user, Boolean.parseBoolean(encrypted));
            user.setUserPassword(null);
            session.setAttribute("login", user);
            resultMap.put("result", "success");
            resultMap.put("message", "저장됐습니다.");
        }catch (Exception e){
            logger.debug("User Update Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Update End");
        return resultMap;
    }

    /**
     * 사용자의 포인트 목록을 조회한다.
     * @param userEmail
     * @param userToken
     * @return
     */
    @ResponseBody
    @GetMapping("/point/list")
    public Map<String, Object> pointList(@RequestParam(value="userEmail", defaultValue="") String userEmail,
                                         @RequestParam(value="userToken", defaultValue="") String userToken,
                                         @RequestParam(value="page",defaultValue="1") int page,
                                         @RequestParam(value="pageSize",defaultValue="10") int pageSize){
        logger.debug("User Point List Get Start");

        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            HashMap<String, Object> param = new HashMap<>();
            param.put("userEmail", userEmail);
            param.put("userToken", userToken);
            LoginUser user = userService.findUserByIdToken(param);
            if(user != null) {
                int totalCount = userPointService.getUserPointRecordListCount(param);
                PagingDTO paging = new PagingDTO(totalCount, page, 0, pageSize);
                param.put("itemStartPosition", paging.getItemStartPosition());
                param.put("pagePerSize", paging.getPagePerSize());
                resultMap.put("paging", paging);
                resultMap.put("point", userPointService.getUserPointRecordSummary(param));
                resultMap.put("pointList", userPointService.getUserPointRecordList(param));
                resultMap.put("result", "success");
                resultMap.put("message", "조회 완료.");
            }else {
                resultMap.put("result", "fail");
                resultMap.put("message", "사용자 정보를 찾을 수 없습니다.");
            }
        }catch (Exception e){
            logger.debug("User Point List Get Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Point List Get");
        return resultMap;
    }

    /**
     * 사용자 포인트를 사용한다.
     * @param userEmail
     * @param userToken
     * @param usedType
     * @param usedPoint
     * @return
     */
    @ResponseBody
    @PutMapping("/point/use")
    public Map<String, Object> pointUse(@RequestParam(value="userEmail", defaultValue="") String userEmail,
                                        @RequestParam(value="userToken", defaultValue="") String userToken,
                                        @RequestParam(value="usedType", defaultValue="") String usedType,
                                        @RequestParam(value="usedPoint", defaultValue="0") int usedPoint){
        logger.debug("User Point Use Start");

        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            HashMap<String, Object> param = new HashMap<>();
            param.put("userEmail", userEmail);
            param.put("userToken", userToken);
            LoginUser userCheck = userService.findUserByIdToken(param);
            if(userCheck != null) {
                LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                userPointService.useUserPoint(userEmail, usedType, usedPoint, user.getUserEmail());
                resultMap.put("result", "success");
                resultMap.put("message", "저장됐습니다.");
            }else {
                resultMap.put("result", "fail");
                resultMap.put("message", "사용자 정보를 찾을 수 없습니다.");
            }
        }catch (Exception e){
            logger.debug("User Point Use Error");
            logger.error(e.getMessage());
            resultMap.put("result", "error");
            resultMap.put("message", "오류가 발생했습니다.");
        }

        logger.debug("User Point Use End");
        return resultMap;
    }







    /**
     * 세션에 encrypt 와 iv 가 없을 경우 설정
     * @param session 사용자 세션
     */
    private void setSessionEncryptKey(HttpSession session){
        if(session.getAttribute("encrypt") == null || session.getAttribute("iv") == null){
            CryptoUtil cryptoUtil = new CryptoUtil();
            session.setAttribute("encrypt", cryptoUtil.generateRandomEncryptKey(""));
            session.setAttribute("iv", cryptoUtil.generateRandomIv(""));
        }
    }

    /**
     * 로그인 상태 값을 확인
     * @param session 사용자 세션
     * @return 로그인 상태
     */
    private boolean checkLogin(HttpSession session) {
        if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return true;
        }else{
            if(session.getAttribute("login") != null){
                session.removeAttribute("login");
            }
            return false;
        }
    }

}
