package io.github.swsk33.fileliftcore.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.config.StorageConfig;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.strategy.context.FileNameStrategyContext;
import io.github.swsk33.fileliftcore.strategy.context.FileProcessStrategyContext;
import io.github.swsk33.fileliftcore.validator.context.FileValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传实现类
 */
public class UploadFileServiceImpl implements UploadFileService {

	/**
	 * 注入核心配置
	 */
	private final CoreConfig config;

	/**
	 * 注入文件处理策略类型
	 */
	private final FileProcessStrategy strategy;

	/**
	 * 注入文件校验责任链上下文
	 */
	private final FileValidatorContext validatorContext;

	/**
	 * 构造函数
	 *
	 * @param config        框架核心配置
	 * @param storageConfig 文件系统配置，需要是核心配置中配置的类型对应的存储配置类型
	 */
	public UploadFileServiceImpl(CoreConfig config, StorageConfig storageConfig) {
		this.config = config;
		this.strategy = FileProcessStrategyContext.createStrategy(config, storageConfig);
		this.validatorContext = new FileValidatorContext(config, strategy);
	}

	@Override
	public FileResult<UploadFile> upload(MultipartFile uploadFile) {
		// 根据策略，判断是沿用原始文件名还是生成文件名
		return uploadForceName(uploadFile, config.isAutoRename() ? FileNameStrategyContext.generateFileName(config.getAutoRenameFormat()) : FileNameUtil.mainName(uploadFile.getOriginalFilename()));
	}

	@Override
	public FileResult<UploadFile> uploadForceName(MultipartFile uploadFile, String name) {
		// 上传文件之前，先校验文件
		FileValidateResult validateResult = validatorContext.validate(uploadFile, name);
		if (!validateResult.isSuccess()) {
			return FileResult.resultFailed(validateResult.getMessage());
		}
		// 调用文件存储策略完成文件存储
		return FileResult.resultSuccess("上传文件完成！", strategy.saveFile(uploadFile, name));
	}

	@Override
	public FileResult<Void> delete(String filename) {
		strategy.deleteFile(filename);
		return FileResult.resultSuccess("删除文件完成！");
	}

	@Override
	public FileResult<UploadFile> findByMainName(String filename) {
		UploadFile getFile = strategy.findFileByMainName(filename);
		return getFile == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("查找文件完成！", getFile);
	}

	@Override
	public FileResult<UploadFile> findByFullName(String fullName) {
		UploadFile getFile = strategy.findFileByFullName(fullName);
		return getFile == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("查找文件完成！", getFile);
	}

	@Override
	public FileResult<BinaryContent> downloadFileByMainName(String filename) {
		BinaryContent content = strategy.downloadFileByMainName(filename);
		return content == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("已获取到文件流！", content);
	}

	@Override
	public FileResult<BinaryContent> downloadFileByFullName(String fullName) {
		BinaryContent content = strategy.downloadFileByFullName(fullName);
		return content == null ? FileResult.resultFailed("文件不存在！") : FileResult.resultSuccess("已获取到文件流！", content);
	}

}