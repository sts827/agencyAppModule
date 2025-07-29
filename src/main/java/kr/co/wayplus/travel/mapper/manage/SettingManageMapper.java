package kr.co.wayplus.travel.mapper.manage;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.wayplus.travel.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SettingManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */

//	<!--################################### SettingHistoryContents ###################################-->
	int selectCountHistoryContents(HashMap<String, Object> paramMap);
	ArrayList<SettingHistoryContents> selectListHistoryContents(HashMap<String, Object> paramMap);
	SettingHistoryContents selectOneHistoryContent(HashMap<String, Object> paramMap);
	void insertHistoryContents(SettingHistoryContents SettingHistoryContents);
	void updateHistoryContents(SettingHistoryContents SettingHistoryContents);
	void restoreHistoryContents(HashMap<String, Object> paramMap);
	void deleteHistoryContents(HashMap<String, Object> paramMap);
//	<!--################################### SettingAwardsContents ###################################-->
	int selectCountAwardsContents(HashMap<String, Object> paramMap);
	ArrayList<SettingAwardsContents> selectListAwardsContents(HashMap<String, Object> paramMap);
	SettingAwardsContents selectOneAwardsContent(HashMap<String, Object> paramMap);
	void insertAwardsContents(SettingAwardsContents SettingAwardsContents);
	void updateAwardsContents(SettingAwardsContents SettingAwardsContents);
	void deleteAwardsContents(HashMap<String, Object> paramMap);
	void restoreAwardsContents(HashMap<String, Object> paramMap);
//	<!--################################### SettingAwardsImage ###################################-->
	SettingAwardsImage selectOneAwardsAttachFile(HashMap<String, Object> paramMap);
	void insertAwardsAttachFile(SettingAwardsImage SettingAwardsImage);
	void deleteAwardsAttachFile(SettingAwardsImage SettingAwardsImage);
	//	<!--################################### SettingAllianceContents ###################################-->
	int selectCountAllianceContents(HashMap<String, Object> paramMap);
	ArrayList<SettingAllianceContents> selectListAllianceContents(HashMap<String, Object> paramMap);
	SettingAllianceContents selectOneAllianceContent(HashMap<String, Object> paramMap);
	void insertAllianceContents(SettingAllianceContents SettingAllianceContents);
	void updateAllianceContents(SettingAllianceContents SettingAllianceContents);
	void restoreAllianceContents(HashMap<String, Object> paramMap);
	void deleteAllianceContents(HashMap<String, Object> paramMap);
//	<!--################################### SettingAllianceImage ###################################-->
	SettingAllianceImage selectOneAllianceAttachFile(HashMap<String, Object> paramMap);
	void insertAllianceAttachFile(SettingAllianceImage SettingAllianceImage);
	void deleteAllianceAttachFile(SettingAllianceImage SettingAllianceImage);


    SettingCompanyInfo selectCompanyInfo();

	void insertCompanyInfo(SettingCompanyInfo companyInfo);

    SettingNavbar selectNavbar();

	ArrayList<MenuUser> selectNavMenuList();

	void insertNavbar(SettingNavbar navbar);

    void insertAlarmMailServer(SettingMailServer mailServer);

	void updateAlarmMailServer(SettingMailServer mailServer);

	SettingMailServer selectAlarmMailServer(String type);

	void insertAlarmMailReceiver(SettingMailReceiver mailReceiver);

	ArrayList<SettingMailReceiver> selectAlarmMailReceiverList(int serverId);

	void updateAlarmMailReceiver(SettingMailReceiver mailReceiver);

	void updateAlarmMailReceiverDelete(SettingMailReceiver mailReceiver);

    SettingMessage selectAlarmMessage(String type);

	ArrayList<SettingMessageReceiver> selectAlarmMessageReceiverList(int messageId);

	void updateAlarmMessage(SettingMessage settingMessage);

	void insertAlarmMessageReceiver(SettingMessageReceiver messageReceiver);

	void updateAlarmMessageReceiver(SettingMessageReceiver messageReceiver);

	void updateAlarmMessageReceiverDelete(SettingMessageReceiver messageReceiver);
}
