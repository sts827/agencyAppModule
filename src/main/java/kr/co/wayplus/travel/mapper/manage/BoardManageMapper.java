package kr.co.wayplus.travel.mapper.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.BoardAttachFile;
import kr.co.wayplus.travel.model.BoardComment;
import kr.co.wayplus.travel.model.BoardContents;
import kr.co.wayplus.travel.model.BoardSetting;

@Mapper
@Repository
public interface BoardManageMapper {
	/**
	 * 테이블별로 Select(count,list,one), Insert, Update, Delete 순으로 펑션 정리 희망!!!
	 */
//	<!--################################### BoardSetting ###################################-->
	int selectCountBoardSetting(HashMap<String, Object> paramMap);
	ArrayList<BoardSetting> selectListBoardSetting(HashMap<String, Object> paramMap);
	BoardSetting selectOneBoardSetting(HashMap<String, Object> paramMap);
	void insertBoardSetting(BoardSetting bs) throws SQLException;
	void updateBoardSetting(BoardSetting bs) throws SQLException;
	void restoreBoardSetting(BoardSetting bs) throws SQLException;
	void deleteBoardSetting(BoardSetting bs) throws SQLException;

//	<!--################################### BoardContents ###################################-->
	int selectCountBoardContents(HashMap<String, Object> paramMap);
	int selectCountBoardContents(BoardContents bc);
	ArrayList<BoardContents> selectListBoardContents(HashMap<String, Object> paramMap);
	ArrayList<BoardContents> selectListBoardContents(BoardContents bc);
	BoardContents selectOneBoardContents(HashMap<String, Object> paramMap);
	void insertBoardContents(BoardContents bc)throws SQLException;
	void updateBoardContents(BoardContents bc) throws SQLException;
	void restoreBoardContents(BoardContents bc) throws SQLException;
	void deleteBoardContents(BoardContents bc) throws SQLException;

//	<!--################################### BoardComment ###################################-->
	void updateBoardContent_CommentCount(HashMap<String, Object> param);
	BoardComment selectOneBoardComment(HashMap<String, Object> paramMap);
	void insertBoardComment(BoardComment data) throws SQLException;
	void updateBoardComment(BoardComment data) throws SQLException;


	//	<!--################################### BoardAttachFile ###################################-->
	ArrayList<BoardAttachFile> selectListBoardAttachFile(HashMap<String, Object> paramMap);
	BoardAttachFile selectOneBoardAttachFile(HashMap<String, Object> paramMap);
	void insertBoardAttachFile(BoardAttachFile baf);
	void deleteBoardAttachFile(BoardAttachFile baf);

	void updateBoardContentsStatus(BoardComment data);



/*
//	<!--################################### HistoryContents ###################################-->
	ArrayList<Map<String, Object>> selectListStatisticDate(Map<String, Object> paramMap);
	Map<String, Object> selectListStatisticInfo(Map<String, Object> paramMap);

	void insertHistoryContents(HistoryContents historyContents);
	void updateHistoryContents(HistoryContents historyContents);
	void deleteHistoryContents(HashMap<String, Object> paramMap);
	void restoreHistoryContents(HashMap<String, Object> paramMap);
	int selectCountHistoryContents(HashMap<String, Object> paramMap);
	ArrayList<HistoryContents> selectListHistoryContents(HashMap<String, Object> paramMap);
	HistoryContents selectOneHistoryContent(HashMap<String, Object> paramMap);

	//	<!--################################### AwardsContents ###################################-->
	void insertAwardsContents(AwardsContents awardsContents);
	void updateAwardsContents(AwardsContents awardsContents);
	void deleteAwardsContents(HashMap<String, Object> paramMap);
	void restoreAwardsContents(HashMap<String, Object> paramMap);
	int selectCountAwardsContents(HashMap<String, Object> paramMap);
	ArrayList<AwardsContents> selectListAwardsContents(HashMap<String, Object> paramMap);
	void insertAwardsAttachFile(AwardsImage awardsImage);
	void deleteAwardsAttachFile(AwardsImage awardsImage);
	AwardsContents selectOneAwardsContent(HashMap<String, Object> paramMap);
	//	<!--################################### AllianceContents ###################################-->
	void insertAllianceContents(AllianceContents allianceContents);
	void updateAllianceContents(AllianceContents allianceContents);
	void deleteAllianceContents(HashMap<String, Object> paramMap);
	void restoreAllianceContents(HashMap<String, Object> paramMap);
	int selectCountAllianceContents(HashMap<String, Object> paramMap);
	ArrayList<AllianceContents> selectListAllianceContents(HashMap<String, Object> paramMap);
	void insertAllianceAttachFile(AllianceImage allianceImage);
	void deleteAllianceAttachFile(AllianceImage allianceImage);
	AllianceContents selectOneAllianceContent(HashMap<String, Object> paramMap);

	AllianceImage selectOneAllianceAttachFile(HashMap<String, Object> paramMap);

	AwardsImage selectOneAwardsAttachFile(HashMap<String, Object> paramMap);
*/
}
