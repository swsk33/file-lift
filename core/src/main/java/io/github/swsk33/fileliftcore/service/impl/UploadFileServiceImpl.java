package io.github.swsk33.fileliftcore.service.impl;

import io.github.swsk33.fileliftcore.model.FileResult;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传实现类
 */
public class UploadFileServiceImpl implements UploadFileService {

	@Override
	public FileResult<Void> upload(MultipartFile uploadFile) {
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
	public FileResult<UploadFile> find(String filename) {
		return null;
	}

}