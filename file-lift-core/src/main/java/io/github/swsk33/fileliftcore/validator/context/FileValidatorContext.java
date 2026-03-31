package io.github.swsk33.fileliftcore.validator.context;

import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import io.github.swsk33.fileliftcore.validator.impl.FileFormatValidator;
import io.github.swsk33.fileliftcore.validator.impl.FileOverrideValidator;
import io.github.swsk33.fileliftcore.validator.impl.FileSizeValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 控制文件校验责任链的上下文
 */
public class FileValidatorContext {

	/**
	 * 第一个校验器（入口）
	 */
	private final FileValidator entrypoint;

	/**
	 * 文件校验器上下文构造函数
	 *
	 * @param config   框架核心配置
	 * @param strategy 文件处理策略对象
	 */
	public FileValidatorContext(CoreConfig config, FileProcessStrategy strategy) {
		// 实例化全部校验器
		FileValidator sizeValidator = new FileSizeValidator(config);
		FileValidator formatValidator = new FileFormatValidator(config);
		FileValidator overrideValidator = new FileOverrideValidator(config, strategy);
		// 设定顺序
		sizeValidator.setNext(formatValidator);
		formatValidator.setNext(overrideValidator);
		// 设定入口校验器
		entrypoint = sizeValidator;
	}

	/**
	 * 进行校验
	 *
	 * @param file 传入校验文件
	 * @param name 上传后的文件名，不带扩展名
	 * @return 校验结果
	 */
	public FileValidateResult validate(MultipartFile file, String name) {
		return entrypoint.validateFile(file, name);
	}

}