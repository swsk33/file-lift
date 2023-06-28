package io.github.swsk33.fileliftcore.validator.context;

import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
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
	private static final FileValidator entrypoint;

	// 初始化全部校验器对象
	// 校验器顺序：大小 -> 格式 -> 覆盖检查
	static {
		// 实例化全部校验器
		FileValidator sizeValidator = new FileSizeValidator();
		FileValidator formatValidator = new FileFormatValidator();
		FileValidator overrideValidator = new FileOverrideValidator();
		// 设定顺序
		sizeValidator.setNext(formatValidator);
		formatValidator.setNext(overrideValidator);
		// 设定入口校验器
		entrypoint = sizeValidator;
	}

	/**
	 * 根据责任链校验规则，校验上传的文件
	 *
	 * @param file 上传的文件对象
	 * @return 校验结果
	 */
	public static FileValidateResult validateFile(MultipartFile file) {
		return entrypoint.validateFile(file);
	}

}