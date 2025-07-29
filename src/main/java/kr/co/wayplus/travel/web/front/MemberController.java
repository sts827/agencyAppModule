package kr.co.wayplus.travel.web.front;

import jakarta.servlet.http.HttpSession;
import kr.co.wayplus.travel.service.common.MessageSenderService;
import kr.co.wayplus.travel.service.front.MemberService;
import kr.co.wayplus.travel.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping("/member")
public class MemberController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MemberService memberService;
    private final MessageSenderService messageSenderService;

    public MemberController(MemberService memberService, MessageSenderService messageSenderService) {
        this.memberService = memberService;
        this.messageSenderService = messageSenderService;
    }

    @GetMapping("/myPage")
    public ModelAndView myPage(){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("front/member/myPage");
        return mav;
    }
    
    @GetMapping("/info")
    public ModelAndView info(HttpSession session){
        ModelAndView mav = new ModelAndView();

        if(session.getAttribute("encrypt") == null || session.getAttribute("iv") == null){
            CryptoUtil cryptoUtil = new CryptoUtil();
            session.setAttribute("encrypt", cryptoUtil.generateRandomEncryptKey(""));
            session.setAttribute("iv", cryptoUtil.generateRandomIv(""));
        }

        mav.setViewName("front/member/info");
        return mav;
    }



    @GetMapping("/mail-test")
    @ResponseBody
    public HashMap<String, Object> mailTest(){
        HashMap<String, Object> retrunMap = new HashMap<>();

        try {
            messageSenderService.sendMailFromSet("inquiry", 1);

            retrunMap.put("result", "success");
            retrunMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            retrunMap.put("result", "error");
            retrunMap.put("message", "처리중 문제가 발생했습니다.");
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return retrunMap;
    }

    @GetMapping("/message-test")
    @ResponseBody
    public HashMap<String, Object> messageTest(){
        HashMap<String, Object> retrunMap = new HashMap<>();

        try {
            messageSenderService.pushMessageQueueFromSet("inquiry", 1);

            retrunMap.put("result", "success");
            retrunMap.put("message", "처리가 완료 되었습니다.");
        } catch (Exception e) {
            retrunMap.put("result", "error");
            retrunMap.put("message", "처리중 문제가 발생했습니다.");
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return retrunMap;
    }
}
