package com.miniProject.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class UpLoadFilesUtil {
	public String uploadFiles(MultipartFile file,String path){
		String fileName=file.getOriginalFilename();
		System.out.println("文件路径："+path);
		File targetFile = new File(path, fileName);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
		try {
			file.transferTo(targetFile);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
