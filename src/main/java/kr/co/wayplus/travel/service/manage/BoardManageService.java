package kr.co.wayplus.travel.service.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.manage.BoardManageMapper;
import kr.co.wayplus.travel.model.BoardAttachFile;
import kr.co.wayplus.travel.model.BoardComment;
import kr.co.wayplus.travel.model.BoardContents;
import kr.co.wayplus.travel.model.BoardSetting;

@Service
public class BoardManageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BoardManageMapper mapperWayplusManage;

    public BoardManageService(BoardManageMapper waytripManageMapper) {
        this.mapperWayplusManage = waytripManageMapper;
    }

//	<!--################################### BoardSetting ###################################-->
	public int selectCountBoardSetting(HashMap<String, Object> paramMap)  {
		return mapperWayplusManage.selectCountBoardSetting(paramMap);
	}
	public ArrayList<BoardSetting> selectListBoardSetting(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectListBoardSetting(paramMap);
	}
	public BoardSetting selectOneBoardSetting(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectOneBoardSetting(paramMap);
	}
	public void insertBoardSetting(BoardSetting bs) throws SQLException {
		mapperWayplusManage.insertBoardSetting(bs);
	}
	public void updateBoardSetting(BoardSetting bs) throws SQLException {
		mapperWayplusManage.updateBoardSetting(bs);
	}
	public void restoreBoardSetting(BoardSetting bs) throws SQLException {
		mapperWayplusManage.restoreBoardSetting(bs);
	}
	public void deleteBoardSetting(BoardSetting bs) throws SQLException {
		mapperWayplusManage.deleteBoardSetting(bs);
	}

//	<!--################################### BoardContents ###################################-->
	public int selectCountBoardContents(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectCountBoardContents(paramMap);
	}
	public int selectCountBoardContents(BoardContents bc) {
		return mapperWayplusManage.selectCountBoardContents(bc);
	}
	public ArrayList<BoardContents> selectListBoardContents(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectListBoardContents(paramMap);
	}
//	public ArrayList<BoardContents> selectListBoardContents(BoardContents bc) {
//		return mapperWayplusManage.selectListBoardContents(bc);
//	}
	public BoardContents selectOneBoardContents(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectOneBoardContents(paramMap);
	}
	public void saveBoardContents(BoardContents bc) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", bc.getId());

		if( this.selectCountBoardContents(paramMap) == 0) {
			mapperWayplusManage.insertBoardContents(bc);
		} else {
			mapperWayplusManage.updateBoardContents(bc);
		}

	}
	public void insertBoardContents(BoardContents bc) throws SQLException {
		mapperWayplusManage.insertBoardContents(bc);
	}
	public void updateBoardContents(BoardContents bc) throws SQLException {
		mapperWayplusManage.updateBoardContents(bc);
	}
	public void restoreBoardContents(BoardContents bc) throws SQLException {
		mapperWayplusManage.restoreBoardContents(bc);
	}
	public void deleteBoardContents(BoardContents bc) throws SQLException {
		mapperWayplusManage.deleteBoardContents(bc);
	}

//	<!--################################### BoardComment ###################################-->
	public void updateBoardContent_CommentCount(BoardComment data) throws SQLException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("contentId", data.getContentId());

		mapperWayplusManage.updateBoardContent_CommentCount(param);
	}

	public BoardComment selectOneBoardComment(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectOneBoardComment(paramMap);
	}

	public void insertBoardComment(BoardComment data) throws SQLException {
		mapperWayplusManage.insertBoardComment(data);
		mapperWayplusManage.updateBoardContentsStatus(data);
	}
	public void updateBoardComment(BoardComment data) throws SQLException {
		mapperWayplusManage.updateBoardComment(data);
		mapperWayplusManage.updateBoardContentsStatus(data);
	}

	//	<!--################################### BoardAttachFile ###################################-->제휴사
	public ArrayList<BoardAttachFile> selectListBoardAttachFile(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectListBoardAttachFile(paramMap);
	}
	public BoardAttachFile selectOneBoardAttachFile(HashMap<String, Object> paramMap) {
		return mapperWayplusManage.selectOneBoardAttachFile(paramMap);
	}
	public void insertBoardAttachFile(BoardAttachFile baf) {
		mapperWayplusManage.insertBoardAttachFile(baf);
	}
	public void deleteBoardAttachFile(BoardAttachFile baf) {
		mapperWayplusManage.deleteBoardAttachFile(baf);
	}





}
