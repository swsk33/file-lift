package io.github.swsk33.fileliftcore.validator.impl;

import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.validator.FileValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于校验文件大小的校验器
 */
public class FileSizeValidator extends FileValidator {

	@Override
	public FileValidateResult validateFile(MultipartFile file) {
		// 获取配置大小
		int sizeLimit = FileConfig.getInstance().getSizeLimit();
		// 若配置的大小小于等于0，则直接通过到下一步
		if (sizeLimit <= 0) {
			return passToNext(file);
		}
		// 获取文件大小比较
		if (file.getSize() <= sizeLimit * 1024L) {
			return passToNext(file);
		}
		// 否则，大小校验不通过
		return FileValidateResult.resultFailed("文件超出允许的上传大小！最大允许的上传大小：" + sizeLimit + "KB");
	}

}