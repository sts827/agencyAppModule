package kr.co.wayplus.travel.mapper.front;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.wayplus.travel.model.*;

@Mapper
@Repository
public interface BoardMapper {
//	<!--################################### BoardSetting ###################################-->
	BoardSetting selectOneBoardSetting(HashMap<String, Object> paramMap);

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

	void insertReviewContent(BoardContents ic);
}
