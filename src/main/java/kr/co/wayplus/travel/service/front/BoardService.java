package kr.co.wayplus.travel.service.front;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kr.co.wayplus.travel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.wayplus.travel.mapper.front.BoardMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class BoardService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${upload.file.path}")
	String externalImageUploadPath;
	@Value("${upload.file.max-size}")
	int maxFileSize;
	final String addPath = "images/";

    private final BoardMapper mapper;

    @Autowired
    public BoardService(BoardMapper mapper) {
        this.mapper = mapper;
    }

//	<!--################################### BoardContents ###################################-->
	public int selectCountBoardContents(HashMap<String, Object> paramMap) {
		return mapper.selectCountBoardContents(paramMap);
	}
	public int selectCountBoardContents(BoardContents bc) {
		return mapper.selectCountBoardContents(bc);
	}
	public ArrayList<BoardContents> selectListBoardContents(HashMap<String, Object> paramMap) {
		return mapper.selectListBoardContents(paramMap);
	}
//	public ArrayList<BoardContents> selectListBoardContents(BoardContents bc) {
//		return mapperWayplusManage.selectListBoardContents(bc);
//	}
	public BoardContents selectOneBoardContents(HashMap<String, Object> paramMap) {
		return mapper.selectOneBoardContents(paramMap);
	}
	public void saveBoardContents(BoardContents bc) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", bc.getId());

		if( this.selectCountBoardContents(paramMap) == 0) {
			mapper.insertBoardContents(bc);
		} else {
			mapper.updateBoardContents(bc);
		}

	}
	public void insertBoardContents(BoardContents bc) throws SQLException {
		mapper.insertBoardContents(bc);
	}
	public void updateBoardContents(BoardContents bc) throws SQLException {
		mapper.updateBoardContents(bc);
	}
	public void restoreBoardContents(BoardContents bc) throws SQLException {
		mapper.restoreBoardContents(bc);
	}
	public void deleteBoardContents(BoardContents bc) throws SQLException {
		mapper.deleteBoardContents(bc);
	}

//	<!--################################### BoardComment ###################################-->
	public void updateBoardContent_CommentCount(BoardComment data) throws SQLException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("contentId", data.getContentId());

		mapper.updateBoardContent_CommentCount(param);
	}

	public BoardComment selectOneBoardComment(HashMap<String, Object> paramMap) {
		return mapper.selectOneBoardComment(paramMap);
	}

	public void insertBoardComment(BoardComment data) throws SQLException {
		mapper.insertBoardComment(data);

		this.updateBoardContent_CommentCount(data);
	}
	public void updateBoardComment(BoardComment data) throws SQLException {
		mapper.updateBoardComment(data);

		this.updateBoardContent_CommentCount(data);
	}

//	<!--################################### BoardSetting ###################################-->
	public BoardSetting selectOneBoardSetting(HashMap<String, Object> paramMap) {
		return mapper.selectOneBoardSetting(paramMap);
	}

//	<!--################################### BoardAttachFile ###################################-->제휴사
	public ArrayList<BoardAttachFile> selectListBoardAttachFile(HashMap<String, Object> paramMap) {
		return mapper.selectListBoardAttachFile(paramMap);
	}
	public BoardAttachFile selectOneBoardAttachFile(HashMap<String, Object> paramMap) {
		return mapper.selectOneBoardAttachFile(paramMap);
	}
	public void insertBoardAttachFile(BoardAttachFile baf) {
		mapper.insertBoardAttachFile(baf);
	}
	public void deleteBoardAttachFile(BoardAttachFile baf) {
		mapper.deleteBoardAttachFile(baf);
	}

	public void insertReviewContent(BoardContents bc, LoginUser user, MultipartHttpServletRequest request) throws IOException {
		mapper.insertReviewContent(bc);

		if( request.getFile("attach") != null) {
			List<MultipartFile> multipartFiles = request.getFiles("attach");
			File file = new File(externalImageUploadPath + addPath);
			if (!file.exists()) file.mkdirs();

			for (MultipartFile multipartFile : multipartFiles) {
				System.out.println("multipartFile : " + multipartFile.getOriginalFilename());
				System.out.println("multipartFile : " + multipartFile.getSize());
				System.out.println("multipartFile : " + multipartFile.getContentType());
				System.out.println("multipartFile : " + multipartFile);
				String uploadName = UUID.randomUUID().toString();
				multipartFile.transferTo(new File(externalImageUploadPath+ addPath + uploadName));
				logger.debug("User Question File Uploaded : " + multipartFile.getOriginalFilename());

				BoardAttachFile attachImage = new BoardAttachFile();
				attachImage.setBoardId(bc.getId());
				attachImage.setServiceType("review");//등록 타입 설정
				attachImage.setUploadPath(externalImageUploadPath+ addPath);
				attachImage.setUploadFilename(uploadName);
				attachImage.setOriginFilename(multipartFile.getOriginalFilename());
				attachImage.setFileSize((int) multipartFile.getSize());
				attachImage.setFileMimetype(multipartFile.getContentType());
				if (multipartFile.getOriginalFilename().contains(".")) {
					attachImage.setFileExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
				}
				if (user != null) {
					attachImage.setUploadId(String.valueOf(user.getUserEmail()));
				}
				this.insertBoardAttachFile(attachImage);
			}
		}
	}
}
