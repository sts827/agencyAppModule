package kr.co.wayplus.travel.util;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import kr.co.wayplus.travel.base.model.CommonFileInfo;

public class FileInfoUtil {

	public static Object getFileInfo(MultipartFile file, Object image) {
		CommonFileInfo ret = (CommonFileInfo) image;

		ret.setOriginFilename(file.getOriginalFilename());
		ret.setFileSize((int) file.getSize());
		ret.setFileMimetype(file.getContentType());
		if(file.getOriginalFilename().contains(".")) {
			ret.setFileExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
		}

		return image;
	}

    public static CommonFileInfo getFileInfo(MultipartFile file) {
    	CommonFileInfo ret = new CommonFileInfo();

    	ret.setOriginFilename(file.getOriginalFilename());
		ret.setFileSize((int) file.getSize());
		ret.setFileMimetype(file.getContentType());
		if(file.getOriginalFilename().contains(".")) {
			ret.setFileExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
		}

		return ret;
    }

    public static boolean deleteImageFile_real(CommonFileInfo image) {
    	File file = new File(image.getRealFilePath());

    	if(file.exists()) {
    		return file.delete();
    	} else {
    		return false;
    	}
    }

}
