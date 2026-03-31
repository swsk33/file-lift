package io.github.swsk33.fileliftcore.validator.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于检查文件覆盖的校验器
 */
public class FileOverrideValidator extends FileValidator {

	/**
	 * 核心框架配置
	 */
	private final CoreConfig config;

	/**
	 * 文件操作策略对象
	 */
	private final FileProcessStrategy strategy;

	/**
	 * 文件覆盖校验器构造函数
	 *
	 * @param config   核心框架配置
	 * @param strategy 文件操作策略对象
	 */
	public FileOverrideValidator(CoreConfig config, FileProcessStrategy strategy) {
		this.config = config;
		this.strategy = strategy;
	}

	@Override
	public FileValidateResult validateFile(MultipartFile file, String name) {
		// 如果配置为允许覆盖，则校验直接通过
		if (config.isOverride()) {
			return passToNext(file, name);
		}
		// 检查文件是否存在，不存在则校验通过
		// 若"指定上传名称.原始扩展名"的文件存在，则拒绝上传
		// 仅上传名称相同而扩展名不同，也允许上传
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		String filename = formatName.isEmpty() ? name : String.format("%s.%s", name, formatName);
		if (!strategy.fileExists(filename)) {
			return passToNext(file, name);
		}
		// 否则，校验不通过
		return FileValidateResult.resultFailed("上传的文件名已存在！请设置配置中autoRename为true或者override为true以关闭覆盖检查！");
	}

}