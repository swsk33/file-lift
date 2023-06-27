package io.github.swsk33.fileliftcore.validator.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.FileValidateResult;
import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件格式校验器
 */
public class FileFormatValidator extends FileValidator {

	@Override
	public FileValidateResult validateFile(MultipartFile file) {
		// 校验文件的格式是否是允许的
		String[] allowFormats = FileConfig.getInstance().getAllowedFormats();
		// 若不需要类型判断则执行下一个校验
		if (allowFormats == null || allowFormats.length == 0) {
			return passToNext(file);
		}
		// 若需要类型判断，则检查文件类型是否存在于允许的类型列表中
		String fileFormat = FileUtil.extName(file.getOriginalFilename());
		// 如果文件无扩展名，检查配置的格式数组中是否包含?，若是则校验通过
		if (StrUtil.isEmpty(fileFormat) && ArrayUtil.contains(allowFormats, "?")) {
			return passToNext(file);
		}
		// 搜索文件类型是否包含
		if (ArrayUtil.containsIgnoreCase(allowFormats, fileFormat)) {
			return passToNext(file);
		}
		// 否则，校验不通过
		return FileValidateResult.resultFailed("不被允许的文件格式！允许上传的文件格式为：" + ArrayUtil.join(allowFormats, ", "));
	}

}