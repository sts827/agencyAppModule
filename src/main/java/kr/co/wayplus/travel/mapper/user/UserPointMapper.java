package kr.co.wayplus.travel.mapper.user;

import kr.co.wayplus.travel.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Mapper
public interface UserPointMapper {

    int selectUserPointSettingListCount(HashMap<String, Object> param);

    ArrayList<UserPointSet> selectUserPointSettingList(HashMap<String, Object> param);

    UserPointSet selectUserPointSettingById(String id);

    void updateUserPointSetting(UserPointSet pointSet);

    int selectUserPointRecordListCount(HashMap<String, Object> param);

    ArrayList<UserPointRecord> selectUserPointRecordList(HashMap<String, Object> param);

    int selectUserPointAccruedListCount(HashMap<String, Object> param);

    ArrayList<UserPointAccrued> selectUserPointAccruedList(HashMap<String, Object> param);

    int selectUserPointUsedListCount(HashMap<String, Object> param);

    ArrayList<UserPointUsed> selectUserPointUsedList(HashMap<String, Object> param);





    UserPointSet selectUserPointSet(String accruedCode);

    ArrayList<UserPointSet> selectUserPointSetByTypeCode(String login);

    int selectUserPointCreateLogCount(UserPointAccrued pointAccrued);

    void insertPointAccruedLog(UserPointAccrued pointAccrued);

    void insertPointRecord(UserPointRecord pointRecord);

    void updateUserPointAccruedCancel(UserPointAccrued pointAccrued);

    void updateUserPointRecordDelete(UserPointRecord pointRecord);

    ArrayList<CodeItem> selectCodeItemList(String pointType);
    
    ArrayList<UserPointRecord> selectUserPointRecord(HashMap<String, Object> param);
    
    UserPointRecord selectUserPointRecordSummary(HashMap<String, Object> param);

    UserPointRecord selectUserExpiringPointRecordSummary(HashMap<String, Object> param);

    void updateUserPointRecordUsed(UserPointRecord pointRecord);

    void insertPointUsedLog(UserPointUsed pointUsed);
}
