package io.github.swsk33.fileliftcore.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.strategy.context.FileNameStrategyContext;
import io.github.swsk33.fileliftcore.strategy.context.FileProcessStrategyContext;
import io.github.swsk33.fileliftcore.validator.context.FileValidatorContext;
import org.springframework.web.multipart.MultipartFile;

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