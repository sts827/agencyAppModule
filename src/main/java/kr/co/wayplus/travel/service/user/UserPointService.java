package kr.co.wayplus.travel.service.user;

import com.google.gson.Gson;
import kr.co.wayplus.travel.mapper.user.UserPointMapper;
import kr.co.wayplus.travel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

@Service
public class UserPointService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserPointMapper pointMapper;

    public UserPointService(UserPointMapper pointMapper) {
        this.pointMapper = pointMapper;
    }

    public int getUserPointSettingListCount(HashMap<String, Object> param) {
        return pointMapper.selectUserPointSettingListCount(param);
    }

    public ArrayList<UserPointSet> getUserPointSettingList(HashMap<String, Object> param) {
        return pointMapper.selectUserPointSettingList(param);
    }

    public UserPointSet getUserPointSettingById(String id) {
        return pointMapper.selectUserPointSettingById(id);
    }

    public void updateUserPointSetting(UserPointSet pointSet) {
        pointMapper.updateUserPointSetting(pointSet);
    }

    public int getUserPointRecordListCount(HashMap<String, Object> param) {
        return pointMapper.selectUserPointRecordListCount(param);
    }

    public ArrayList<UserPointRecord> getUserPointRecordList(HashMap<String, Object> param) {
        return pointMapper.selectUserPointRecordList(param);
    }

    public int getUserPointAccruedListCount(HashMap<String, Object> param) {
        return pointMapper.selectUserPointAccruedListCount(param);
    }

    public ArrayList<UserPointAccrued> getUserPointAccruedList(HashMap<String, Object> param) {
        return pointMapper.selectUserPointAccruedList(param);
    }

    public int getUserPointUsedListCount(HashMap<String, Object> param) {
        return pointMapper.selectUserPointUsedListCount(param);
    }

    public ArrayList<UserPointUsed> getUserPointUsedList(HashMap<String, Object> param) {
        return pointMapper.selectUserPointUsedList(param);
    }


    /**
     * 사용자 로그인에 따른 포인트를 기록한다.
     * @param user
     */
    public void createLoginPoint(LoginUser user) {
        logger.debug("Login Point Create Start");
        ArrayList<UserPointSet> pointSetList = pointMapper.selectUserPointSetByTypeCode("login");
        if(pointSetList != null){
            logger.debug("Login Point Enabled");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            UserPointAccrued pointAccrued = new UserPointAccrued();
            pointAccrued.setUserEmail(user.getUserEmail());
            pointAccrued.setAccruedDate(sdf.format(new Date()));
            for (UserPointSet  pointSet : pointSetList) {
                pointAccrued.setAccruedCode(pointSet.getAccruedCode());
                if(pointMapper.selectUserPointCreateLogCount(pointAccrued) > 0){
                    logger.debug(String.format("Already Create User Login Point. User: %s, PointCode: %s", user.getUserEmail(), pointSet.getAccruedCode()));
                }else{
                    logger.debug(String.format("Create Login Point User: %s, PointCode: %s, Point: %s, Expire: %s", user.getUserEmail(), pointSet.getAccruedCode(), pointSet.getAccruedPoint(), pointSet.getExpiredDay()));
                    createUserPoint(user.getUserEmail(), pointSet.getAccruedCode(), pointSet.getAccruedReason(), pointSet.getAccruedPoint(), pointSet.getExpiredDay(), "Login");
                }
            }
        }else{
            logger.debug("Login Point Disabled");
        }
        logger.debug("Login Point Create End");
    }

    /**
     * 사용자 포인트를 생성한다.
     * @param pointAccrued
     */
    @Transactional
    public void createUserPoint(UserPointAccrued pointAccrued) {
        pointMapper.insertPointAccruedLog(pointAccrued);
        UserPointRecord pointRecord = new UserPointRecord();
        pointRecord.setUserEmail(pointAccrued.getUserEmail());
        pointRecord.setAccruedId(pointAccrued.getId());
        pointRecord.setAccruedType(pointAccrued.getAccruedType());
        pointRecord.setExpireDate(pointAccrued.getExpireDate());
        pointRecord.setPointAccrued(pointAccrued.getPointAccrued());
        pointRecord.setPointRemain(pointAccrued.getPointAccrued());
        pointRecord.setCreateId(pointAccrued.getCreateId());
        pointMapper.insertPointRecord(pointRecord);
    }

    /**
     * 사용자 포인트를 생성한다.
     * @param userEmail
     * @param accruedCode
     * @param accruedReason
     * @param accruedPoint
     * @param expiredDay
     * @param createId
     */

    @Transactional
    public void createUserPoint(String userEmail, String accruedCode, String accruedReason, int accruedPoint, String expiredDay, String createId) {
        UserPointAccrued pointAccrued = new UserPointAccrued();
        pointAccrued.setUserEmail(userEmail);
        pointAccrued.setAccruedCode(accruedCode);
        pointAccrued.setAccruedType(accruedReason);
        if(expiredDay != null){
            pointAccrued.setExpireDate(convertExpireDayToDate(expiredDay));
        }
        pointAccrued.setPointAccrued(accruedPoint);
        pointAccrued.setCreateId(createId);
        pointMapper.insertPointAccruedLog(pointAccrued);

        UserPointRecord pointRecord = new UserPointRecord();
        pointRecord.setUserEmail(pointAccrued.getUserEmail());
        pointRecord.setAccruedId(pointAccrued.getId());
        pointRecord.setAccruedType(pointAccrued.getAccruedType());
        pointRecord.setExpireDate(pointAccrued.getExpireDate());
        pointRecord.setPointAccrued(pointAccrued.getPointAccrued());
        pointRecord.setPointRemain(pointAccrued.getPointAccrued());
        pointRecord.setCreateId(pointAccrued.getCreateId());
        pointMapper.insertPointRecord(pointRecord);
    }

    /**
     * 사용자 포인트를 취소 처리한다.
     * @param id
     * @param pointAccrued
     */
    @Transactional
    public void cancelUserPoint(int id, UserPointAccrued pointAccrued) {
        pointMapper.updateUserPointAccruedCancel(pointAccrued);
        UserPointRecord pointRecord = new UserPointRecord();
        pointRecord.setId(id);
        pointRecord.setAccruedId(pointAccrued.getId());
        pointRecord.setDeleteId(pointAccrued.getCancelId());
        pointRecord.setDeleteCode(pointAccrued.getCancelCode());
        pointRecord.setDeleteYn("Y");
        pointMapper.updateUserPointRecordDelete(pointRecord);
    }

    /**
     * 사용자 포인트를 사용한다.
     * 선종선출(먼저 종료되는 것 우선 사용)된다.
     * @param userEmail
     * @param usedType
     * @param usedPoint
     * @param operatorId
     */
    @Transactional
    public void useUserPoint(String userEmail, String usedType, int usedPoint, String operatorId) throws RuntimeException{
        logger.debug("User Point Use Proc Start");
        HashMap<String, Object> param = new HashMap<>();
        param.put("userEmail", userEmail);
        UserPointRecord remainPoint = pointMapper.selectUserPointRecordSummary(param);
        logger.debug(String.format("User: %s Is Remain Point %s Use Request Point %s", userEmail, remainPoint.getPointRemain(), usedPoint));
        if(remainPoint.getPointRemain() < usedPoint){
            logger.debug("User Point Use Proc Error : Not Enough Point ");
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }else{
            param.put("used", "true");
            ArrayList<UserPointRecord> userPointRecords = pointMapper.selectUserPointRecordList(param);
            Gson gson = new Gson();
            int usedRemainPoint = usedPoint;
            HashMap<String, Object> detailRecord = new HashMap<>();
            ArrayList<UserPointRecord> beforeRecords = new ArrayList<>();
            ArrayList<UserPointRecord> afterRecords = new ArrayList<>();
            for (UserPointRecord pointRecord : userPointRecords){
                logger.debug(String.format("Record Point : %s , Used Remain Point : %s", pointRecord.getPointRemain(), usedRemainPoint));
                logger.debug(String.format("Current Record : %s", gson.toJson(pointRecord)));
                pointRecord.setLastUpdateId(operatorId);
                UserPointRecord beforeRecord = new UserPointRecord();
                UserPointRecord afterRecord = new UserPointRecord();
                if(pointRecord.getPointRemain() >= usedRemainPoint){
                    beforeRecord.setId(pointRecord.getId());
                    beforeRecord.setAccruedId(pointRecord.getAccruedId());
                    beforeRecord.setPointRemain(pointRecord.getPointRemain());
                    beforeRecord.setPointUsed(pointRecord.getPointUsed());
                    beforeRecord.setLastUpdateId(pointRecord.getLastUpdateId());
                    beforeRecords.add(beforeRecord);

                    pointRecord.setPointRemain(pointRecord.getPointRemain() - usedRemainPoint);
                    pointRecord.setPointUsed(pointRecord.getPointUsed() + usedRemainPoint);
                    pointMapper.updateUserPointRecordUsed(pointRecord);

                    afterRecord.setId(pointRecord.getId());
                    afterRecord.setAccruedId(pointRecord.getAccruedId());
                    afterRecord.setPointRemain(pointRecord.getPointRemain());
                    afterRecord.setPointUsed(pointRecord.getPointUsed());
                    afterRecord.setLastUpdateId(pointRecord.getLastUpdateId());
                    afterRecords.add(afterRecord);
                    usedRemainPoint = 0;
                    break;
                }else{
                    beforeRecord.setId(pointRecord.getId());
                    beforeRecord.setAccruedId(pointRecord.getAccruedId());
                    beforeRecord.setPointRemain(pointRecord.getPointRemain());
                    beforeRecord.setPointUsed(pointRecord.getPointUsed());
                    beforeRecord.setLastUpdateId(pointRecord.getLastUpdateId());
                    beforeRecords.add(beforeRecord);

                    usedRemainPoint = usedRemainPoint - pointRecord.getPointRemain();
                    pointRecord.setPointUsed(pointRecord.getPointUsed() + pointRecord.getPointRemain());
                    pointRecord.setPointRemain(0);
                    pointMapper.updateUserPointRecordUsed(pointRecord);

                    afterRecord.setId(pointRecord.getId());
                    afterRecord.setAccruedId(pointRecord.getAccruedId());
                    afterRecord.setPointRemain(pointRecord.getPointRemain());
                    afterRecord.setPointUsed(pointRecord.getPointUsed());
                    afterRecord.setLastUpdateId(pointRecord.getLastUpdateId());
                    afterRecords.add(afterRecord);
                }
            }
            detailRecord.put("before", beforeRecords);
            detailRecord.put("after", afterRecords);

            UserPointUsed pointUsed = new UserPointUsed();
            pointUsed.setUserEmail(userEmail);
            pointUsed.setUsedType(usedType);
            pointUsed.setPointUsed(usedPoint);
            pointUsed.setCreateId(operatorId);
            pointUsed.setDetailRecord(gson.toJson(detailRecord));
            pointMapper.insertPointUsedLog(pointUsed);
        }
        logger.debug("User Point Use Proc End");
    }

    public UserPointRecord getUserExpiringPointRecordSummary(HashMap<String, Object> param) {
        return pointMapper.selectUserExpiringPointRecordSummary(param);
    }

    private String convertExpireDayToDate(String expiredDay) {
        if(Pattern.matches("^\\d+[dDwWmMyY]{1}$", expiredDay)){
            int add = Integer.parseInt(expiredDay.substring(0, expiredDay.length()-1));
            String dayFormat = expiredDay.substring(expiredDay.length()-1, expiredDay.length());
            logger.debug(String.format("set day format $s", dayFormat));
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            switch (dayFormat){
                case "d": case "D":
                    cal.add(Calendar.DATE, add);
                    break;
                case "w": case "W":
                    cal.add(Calendar.WEEK_OF_YEAR, add);
                    break;
                case "m": case "M":
                    cal.add(Calendar.MONTH, add);
                    break;
                case "y": case "Y":
                    cal.add(Calendar.YEAR, add);
                    break;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(cal.getTime());
        }else{
            logger.debug("Expire Day Expression Error");
            return null;
        }
    }

    public ArrayList<CodeItem> getCodeItemList(String pointType) {
        return pointMapper.selectCodeItemList(pointType);
    }

    public ArrayList<UserPointRecord> getUserPointRecord(HashMap<String, Object> param) {
        return pointMapper.selectUserPointRecord(param);
    }
    public UserPointRecord getUserPointRecordSummary(HashMap<String, Object> param) {
        return pointMapper.selectUserPointRecordSummary(param);
    }

    public void createJoinPoint(LoginUser user) {
        logger.debug("Join Point Create Start");
        ArrayList<UserPointSet> pointSetList = pointMapper.selectUserPointSetByTypeCode("join");
        if(pointSetList != null){
            logger.debug("Join Point Enabled");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            UserPointAccrued pointAccrued = new UserPointAccrued();
            pointAccrued.setUserEmail(user.getUserEmail());
            pointAccrued.setAccruedDate(sdf.format(new Date()));
            for (UserPointSet  pointSet : pointSetList) {
                pointAccrued.setAccruedCode(pointSet.getAccruedCode());
                if(pointMapper.selectUserPointCreateLogCount(pointAccrued) > 0){
                    logger.debug(String.format("Already Create User Join Point. User: %s, PointCode: %s", user.getUserEmail(), pointSet.getAccruedCode()));
                }else{
                    logger.debug(String.format("Create Join Point User: %s, PointCode: %s, Point: %s, Expire: %s", user.getUserEmail(), pointSet.getAccruedCode(), pointSet.getAccruedPoint(), pointSet.getExpiredDay()));
                    createUserPoint(user.getUserEmail(), pointSet.getAccruedCode(), pointSet.getAccruedReason(), pointSet.getAccruedPoint(), pointSet.getExpiredDay(), "Join");
                }
            }
        }else{
            logger.debug("Join Point Disabled");
        }
        logger.debug("Join Point Create End");
    }

    public void createJoinPoint(OAuthUser user) {
        logger.debug("Join Point Create Start");
        ArrayList<UserPointSet> pointSetList = pointMapper.selectUserPointSetByTypeCode("join");
        if(pointSetList != null){
            logger.debug("Join Point Enabled");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            UserPointAccrued pointAccrued = new UserPointAccrued();
            pointAccrued.setUserEmail(user.getUserEmail());
            pointAccrued.setAccruedDate(sdf.format(new Date()));
            for (UserPointSet  pointSet : pointSetList) {
                pointAccrued.setAccruedCode(pointSet.getAccruedCode());
                if(pointMapper.selectUserPointCreateLogCount(pointAccrued) > 0){
                    logger.debug(String.format("Already Create User Join Point. User: %s, PointCode: %s", user.getUserEmail(), pointSet.getAccruedCode()));
                }else{
                    logger.debug(String.format("Create Join Point User: %s, PointCode: %s, Point: %s, Expire: %s", user.getUserEmail(), pointSet.getAccruedCode(), pointSet.getAccruedPoint(), pointSet.getExpiredDay()));
                    createUserPoint(user.getUserEmail(), pointSet.getAccruedCode(), pointSet.getAccruedReason(), pointSet.getAccruedPoint(), pointSet.getExpiredDay(), "Join");
                }
            }
        }else{
            logger.debug("Join Point Disabled");
        }
        logger.debug("Join Point Create End");
    }


    public interface createCancelPoint {}
    public interface usedPoint {}
    public interface usedCancelPoint {}
    public interface expirePoint {}
}
