package io.github.swsk33.fileliftcore.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.FileResult;
import io.github.swsk33.fileliftcore.model.FileValidateResult;
import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.strategy.context.FileNameStrategyContext;
import io.github.swsk33.fileliftcore.strategy.context.FileProcessStrategyContext;
import io.github.swsk33.fileliftcore.validator.context.FileValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件上传实现类
 */
public class UploadFileServiceImpl implements UploadFileService {

	/**
	 * 全局配置
	 */
	private final FileConfig config = FileConfig.getInstance();

	@Override
	public FileResult<UploadFile> upload(MultipartFile uploadFile) {
		// 上传文件之前，先校验文件
		FileValidateResult validateResult = FileValidatorContext.validateFile(uploadFile);
		if (!validateResult.isSuccess()) {
			return FileResult.resultFailed(validateResult.getMessage());
		}
		// 获取文件名，或者根据配置自动重命名
		String mainName = config.isAutoRename() ? FileNameStrategyContext.generateFileName(config.getAutoRenameFormat()) : FileNameUtil.mainName(uploadFile.getOriginalFilename());
		// 调用文件存储策略完成文件存储
		return FileResult.resultSuccess("上传文件完成！", FileProcessStrategyContext.saveFile(config.getStorageMethod(), uploadFile, mainName));
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