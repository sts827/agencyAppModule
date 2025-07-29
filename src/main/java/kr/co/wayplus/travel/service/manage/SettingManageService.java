package kr.co.wayplus.travel.service.manage;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.wayplus.travel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.SettingManageMapper;

@Service
public class SettingManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SettingManageMapper mapper;

    public SettingManageService(SettingManageMapper mapper) {
        this.mapper = mapper;
    }

//	<!--################################### SettingHistoryContents ###################################-->연혁
	public int selectCountHistoryContents(HashMap<String, Object> paramMap) {
		return mapper.selectCountHistoryContents(paramMap);
	}
	public ArrayList<SettingHistoryContents> selectListHistoryContents(HashMap<String, Object> paramMap) {
		return mapper.selectListHistoryContents(paramMap);
	}
	public SettingHistoryContents selectOneHistoryContent(HashMap<String, Object> paramMap) {
		return mapper.selectOneHistoryContent(paramMap);
	}
    public void insertHistoryContents(SettingHistoryContents SettingHistoryContents) {
		mapper.insertHistoryContents(SettingHistoryContents);
	}
	public void updateHistoryContents(SettingHistoryContents SettingHistoryContents) {
		mapper.updateHistoryContents(SettingHistoryContents);
	}
	public void restoreHistoryContents(HashMap<String, Object> paramMap) {
		mapper.restoreHistoryContents(paramMap);
	}
	public void deleteHistoryContents(HashMap<String, Object> paramMap) {
		mapper.deleteHistoryContents(paramMap);
	}

//	<!--################################### SettingAwardsContents ###################################-->상장
	public int selectCountAwardsContents(HashMap<String, Object> paramMap) {
		return mapper.selectCountAwardsContents(paramMap);
	}
	public ArrayList<SettingAwardsContents> selectListAwardsContents(HashMap<String, Object> paramMap) {
		return mapper.selectListAwardsContents(paramMap);
	}
	public SettingAwardsContents selectOneAwardsContent(HashMap<String, Object> paramMap) {
		return mapper.selectOneAwardsContent(paramMap);
	}
	public void insertAwardsContents(SettingAwardsContents SettingAwardsContents) {
		mapper.insertAwardsContents(SettingAwardsContents);
	}
	public void updateAwardsContents(SettingAwardsContents SettingAwardsContents) {
		mapper.updateAwardsContents(SettingAwardsContents);
	}
	public void restoreAwardsContents(HashMap<String, Object> paramMap) {
		mapper.restoreAwardsContents(paramMap);
	}
	public void deleteAwardsContents(HashMap<String, Object> paramMap) {
		mapper.deleteAwardsContents(paramMap);
	}
//	<!--################################### SettingAwardsImage ###################################-->제휴사
	public SettingAwardsImage selectOneAwardsAttachFile(HashMap<String, Object> paramMap) {
		return mapper.selectOneAwardsAttachFile(paramMap);
	}
	public void insertAwardsAttachFile(SettingAwardsImage SettingAwardsImage) {
		mapper.insertAwardsAttachFile(SettingAwardsImage);
	}
	public void deleteAwardsAttachFile(SettingAwardsImage SettingAwardsImage) {
		mapper.deleteAwardsAttachFile(SettingAwardsImage);
	}
//	<!--################################### SettingAllianceContents ###################################-->제휴사
	public int selectCountAllianceContents(HashMap<String, Object> paramMap) {
		return mapper.selectCountAllianceContents(paramMap);
	}
	public ArrayList<SettingAllianceContents> selectListAllianceContents(HashMap<String, Object> paramMap) {
		return mapper.selectListAllianceContents(paramMap);
	}
	public SettingAllianceContents selectOneAllianceContent(HashMap<String, Object> paramMap) {
		return mapper.selectOneAllianceContent(paramMap);
	}
	public void insertAllianceContents(SettingAllianceContents SettingAllianceContents) {
		mapper.insertAllianceContents(SettingAllianceContents);
	}
	public void updateAllianceContents(SettingAllianceContents SettingAllianceContents) {
		mapper.updateAllianceContents(SettingAllianceContents);
	}
	public void deleteAllianceContents(HashMap<String, Object> paramMap) {
		mapper.deleteAllianceContents(paramMap);
	}
	public void restoreAllianceContents(HashMap<String, Object> paramMap) {
		mapper.restoreAllianceContents(paramMap);
	}
//	<!--################################### SettingAllianceImage ###################################-->
	public void insertAllianceAttachFile(SettingAllianceImage SettingAllianceImage) {
		mapper.insertAllianceAttachFile(SettingAllianceImage);
	}
	public void deleteAllianceAttachFile(SettingAllianceImage SettingAllianceImage) {
		mapper.deleteAllianceAttachFile(SettingAllianceImage);
	}
	public SettingAllianceImage selectOneAllianceAttachFile(HashMap<String, Object> paramMap) {
		return mapper.selectOneAllianceAttachFile(paramMap);
	}

    public SettingCompanyInfo selectCompanyInfo() {
		return mapper.selectCompanyInfo();
    }

	public void insertCompanyInfo(SettingCompanyInfo companyInfo) {
		mapper.insertCompanyInfo(companyInfo);
	}

    public SettingNavbar selectNavbar() {
		return mapper.selectNavbar();
    }

	public ArrayList<MenuUser> selectNavMenuList() {
		return mapper.selectNavMenuList();
	}

	public void insertNavbar(SettingNavbar navbar) {
		mapper.insertNavbar(navbar);
	}

    public void writeAlarmMailServer(SettingMailServer mailServer) {
		mapper.insertAlarmMailServer(mailServer);
    }

	public void updateAlarmMailServer(SettingMailServer mailServer) {
		mapper.updateAlarmMailServer(mailServer);
	}

	public SettingMailServer getAlarmMailServer(String type) {
		return mapper.selectAlarmMailServer(type);
	}

	public void writeAlarmMailReceiver(SettingMailReceiver mailReceiver) {
		mapper.insertAlarmMailReceiver(mailReceiver);
	}

	public ArrayList<SettingMailReceiver> getAlarmMailReceiverList(int serverId) {
		return mapper.selectAlarmMailReceiverList(serverId);
	}

	public void modifyAlarmMailReceiver(SettingMailReceiver mailReceiver) {
		mapper.updateAlarmMailReceiver(mailReceiver);
	}

	public void deleteAlarmMailReceiver(SettingMailReceiver mailReceiver) {
		mapper.updateAlarmMailReceiverDelete(mailReceiver);
	}

    public SettingMessage getAlarmMessage(String type) {
		return mapper.selectAlarmMessage(type);
    }

	public ArrayList<SettingMessageReceiver> getAlarmMessageReceiverList(int messageId) {
		return mapper.selectAlarmMessageReceiverList(messageId);
	}

	public void updateAlarmMessage(SettingMessage settingMessage) {
		mapper.updateAlarmMessage(settingMessage);
	}

	public void writeAlarmMessageReceiver(SettingMessageReceiver messageReceiver) {
		mapper.insertAlarmMessageReceiver(messageReceiver);
	}

	public void modifyAlarmMessageReceiver(SettingMessageReceiver messageReceiver) {
		mapper.updateAlarmMessageReceiver(messageReceiver);
	}

	public void deleteAlarmMessageReceiver(SettingMessageReceiver messageReceiver) {
		mapper.updateAlarmMessageReceiverDelete(messageReceiver);
	}
}
