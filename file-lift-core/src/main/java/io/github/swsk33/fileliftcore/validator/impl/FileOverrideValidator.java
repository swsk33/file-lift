package io.github.swsk33.fileliftcore.validator.impl;

import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.strategy.context.FileProcessStrategyContext;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于检查文件覆盖的校验器
 */
public class FileOverrideValidator extends FileValidator {

	@Override
	public FileValidateResult validateFile(MultipartFile file) {
		// 获取配置
		FileConfig config = FileConfig.getInstance();
		// 如果自动重命名打开或者配置为允许覆盖，则校验直接通过
		if (config.isAutoRename() || config.isOverride()) {
			return passToNext(file);
		}
		// 检查文件是否存在，不存在则校验通过
		if (!FileProcessStrategyContext.fileExists(config.getStorageMethod(), file.getOriginalFilename())) {
			return passToNext(file);
		}
		// 否则，校验不通过
		return FileValidateResult.resultFailed("上传的文件名已存在！请设置配置中autoRename为true或者override为true以关闭覆盖检查！");
	}

}