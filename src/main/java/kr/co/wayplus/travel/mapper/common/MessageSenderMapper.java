package kr.co.wayplus.travel.mapper.common;

import kr.co.wayplus.travel.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface MessageSenderMapper {

    SettingMailServer selectMailServer(String alarmType);

    SettingMailFormat selectMailFormat(int formatId);

    ArrayList<SettingMailReceiver> selectMailReceiverList(int serverId);

    SettingMessage selectMessageSetting(String alarmType);

    SettingMessageFormat selectMessageFormat(int formatId);

    ArrayList<SettingMessageReceiver> selectMessageReceiverList(int id);

    void insertMessageQueue(MessageQueue message);

    int selectPendingMessageQueueCount(String messageType);

    ArrayList<MessageQueue> selectPendingMessageQueue(String messageType);

    ArrayList<MessageGroupCount> selectPendingMessageQueueGroupCount(String s);

    void updatePendingSummaryMessageSendComplete(HashMap<String, Object> paramMap);

    void updatePendingNormalMessageSendComplete(HashMap<String, Object> paramMap);
}
