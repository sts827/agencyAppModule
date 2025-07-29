package kr.co.wayplus.travel.base.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonFileInfo {
	private Integer fileId;

	private String fileExtension;	//파일 확장자명
	private Integer fileSize;	//파일 크기(Byte)
	private String fileMimetype;	//파일 마임타입
	private String originFilename;	//원본 파일명

	private String serviceType;	//서비스 구분
	private String uploadPath;	//저장 경로
	private String uploadFilename;	//저장 파일명
	private String uploadId;	//저장id
	private String uploadDate;	//저장일시
	private boolean isDelete;


	public String getRealFilePath() {
		return this.getUploadPath()
			 + this.getUploadFilename();
	}
}
