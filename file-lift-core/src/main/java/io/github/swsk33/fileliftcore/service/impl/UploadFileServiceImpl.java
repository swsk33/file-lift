package io.github.swsk33.fileliftcore.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.FileLiftCoreConfig;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.strategy.context.FileNameStrategyContext;
import io.github.swsk33.fileliftcore.strategy.context.FileProcessStrategyContext;
import io.github.swsk33.fileliftcore.validator.context.FileValidatorContext;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传实现类
 */
public class UploadFileServiceImpl implements UploadFileService {

	/**
	 * 全局配置（需要注入）
	 */
	@Setter
	private FileLiftCoreConfig config;

	@Override
	public FileResult<UploadFile> upload(MultipartFile uploadFile) {
		// 根据策略，判断是沿用原始文件名还是生成文件名
		return uploadForceName(uploadFile, config.isAutoRename() ? FileNameStrategyContext.generateFileName(config.getAutoRenameFormat()) : FileNameUtil.mainName(uploadFile.getOriginalFilename()));
	}

	@Override
	public FileResult<UploadFile> uploadForceName(MultipartFile uploadFile, String name) {
		// 上传文件之前，先校验文件
		FileValidateResult validateResult = FileValidatorContext.validateFile(uploadFile, name);
		if (!validateResult.isSuccess()) {
			return FileResult.resultFailed(validateResult.getMessage());
		}
		// 调用文件存储策略完成文件存储
		return FileResult.resultSuccess("上传文件完成！", FileProcessStrategyContext.saveFile(config.getStorageMethod(), uploadFile, name));
	}

	@Override
	public FileResult<Void> delete(String filename) {
		FileProcessStrategyContext.deleteFile(config.getStorageMethod(), filename);
		return FileResult.resultSuccess("删除文件完成！");
	}

	@Override
	public FileResult<UploadFile> findByMainName(String filename) {
		UploadFile getFile = FileProcessStrategyContext.findFileByMainName(config.getStorageMethod(), filename);
		return getFile == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("查找文件完成！", getFile);
	}

	@Override
	public FileResult<UploadFile> findByFullName(String fullName) {
		UploadFile getFile = FileProcessStrategyContext.findFileByFullName(config.getStorageMethod(), fullName);
		return getFile == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("查找文件完成！", getFile);
	}

	@Override
	public FileResult<BinaryContent> downloadFileByMainName(String filename) {
		BinaryContent content = FileProcessStrategyContext.downloadFileByMainName(config.getStorageMethod(), filename);
		return content == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("已获取到文件流！", content);
	}

	@Override
	public FileResult<BinaryContent> downloadFileByFullName(String fullName) {
		BinaryContent content = FileProcessStrategyContext.downloadFileByFullName(config.getStorageMethod(), fullName);
		return content == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("已获取到文件流！", content);
	}

}