
package com.igomall.service;

import java.util.List;

import com.igomall.FileInfo;
import com.igomall.FileInfo.FileType;
import com.igomall.FileInfo.OrderType;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	boolean isValid(FileType fileType, MultipartFile multipartFile);

	String upload(FileType fileType, MultipartFile multipartFile, boolean async);

	String upload(FileType fileType, MultipartFile multipartFile);

	String uploadLocal(FileType fileType, MultipartFile multipartFile);

	List<FileInfo> browser(String path, FileType fileType, OrderType orderType);

	void delete(String path);

}