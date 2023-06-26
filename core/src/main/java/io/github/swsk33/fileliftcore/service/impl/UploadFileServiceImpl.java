package io.github.swsk33.fileliftcore.service.impl;

import io.github.swsk33.fileliftcore.model.FileResult;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件上传实现类
 */
public class UploadFileServiceImpl implements UploadFileService {

	@Override
	public FileResult<UploadFile> upload(MultipartFile uploadFile) {
		return null;
	}

	@Override
	public FileResult<Void> delete(String filename) {
		return null;
	}

	@Override
	public FileResult<Void> rename(String originName, String newName) {
		return null;
	}

	@Override
	public FileResult<UploadFile> findByMainName(String filename) {
		return null;
	}

	@Override
	public FileResult<UploadFile> findByFullName(String fullName) {
		return null;
	}

	@Override
	public FileResult<InputStream> downloadFileByMainName(String filename) {
		return null;
	}

	@Override
	public FileResult<InputStream> downloadFileByFullName(String fullName) {
		return null;
	}

}