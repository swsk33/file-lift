package io.github.swsk33.fileliftcore.validator.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.config.FileLiftCoreConfig;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.strategy.context.FileProcessStrategyContext;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于检查文件覆盖的校验器
 */
public class FileOverrideValidator extends FileValidator {

	@Override
	public FileValidateResult validateFile(MultipartFile file, String name) {
		// 获取配置
		FileLiftCoreConfig config = FileLiftCoreConfig.getInstance();
		// 如果配置为允许覆盖，则校验直接通过
		if (config.isOverride()) {
			return passToNext(file, name);
		}
		// 检查文件是否存在，不存在则校验通过
		// 若"指定上传名称.原始扩展名"的文件存在，则拒绝上传
		// 仅上传名称相同而扩展名不同，也允许上传
		String filename = String.format("%s.%s", name, FileNameUtil.extName(file.getOriginalFilename()));
		if (!FileProcessStrategyContext.fileExists(config.getStorageMethod(), filename)) {
			return passToNext(file, name);
		}
		// 否则，校验不通过
		return FileValidateResult.resultFailed("上传的文件名已存在！请设置配置中autoRename为true或者override为true以关闭覆盖检查！");
	}

}