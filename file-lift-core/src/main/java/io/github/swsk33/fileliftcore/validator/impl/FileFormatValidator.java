package io.github.swsk33.fileliftcore.validator.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件格式校验器
 */
public class FileFormatValidator extends FileValidator {

	/**
	 * 核心框架配置
	 */
	private final CoreConfig config;

	/**
	 * 扩展名校验器构造函数
	 * @param config 核心框架配置
	 */
	public FileFormatValidator(CoreConfig config) {
		this.config = config;
	}

	@Override
	public FileValidateResult validateFile(MultipartFile file, String name) {
		// 校验文件的格式是否是允许的
		String[] allowFormats = config.getAllowedFormats();
		// 若不需要类型判断则执行下一个校验
		if (allowFormats == null || allowFormats.length == 0) {
			return passToNext(file, name);
		}
		// 若需要类型判断，则检查文件类型是否存在于允许的类型列表中
		String fileFormat = FileUtil.extName(file.getOriginalFilename());
		// 如果文件无扩展名，检查配置的格式数组中是否包含?，若是则校验通过
		if (StrUtil.isEmpty(fileFormat) && ArrayUtil.contains(allowFormats, "?")) {
			return passToNext(file, name);
		}
		// 搜索文件类型是否包含
		if (ArrayUtil.containsIgnoreCase(allowFormats, fileFormat)) {
			return passToNext(file, name);
		}
		// 否则，校验不通过
		return FileValidateResult.resultFailed("不被允许的文件格式！允许上传的文件格式为：" + ArrayUtil.join(allowFormats, ", ").replace("?", "无扩展名"));
	}

}