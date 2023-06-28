package io.github.swsk33.fileliftcore.validator;

import io.github.swsk33.fileliftcore.model.result.FileValidateResult;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件校验器抽象类（责任链模式）
 */
@Data
public abstract class FileValidator {

	/**
	 * 下一个校验器
	 */
	private FileValidator next;

	/**
	 * 检验文件的方法
	 *
	 * @param file 上传的文件对象
	 * @return 校验结果
	 */
	public abstract FileValidateResult validateFile(MultipartFile file);

	/**
	 * 若当前校验通过，则可以调用该方法传递文件给下一个校验器
	 * 当前校验通过后，若当前校验器是最后一个，则直接返回校验成功结果
	 *
	 * @param file 上传的文件对象
	 * @return 校验结果
	 */
	protected FileValidateResult passToNext(MultipartFile file) {
		if (next == null) {
			return FileValidateResult.resultSuccess("全部文件规则校验通过！");
		}
		return next.validateFile(file);
	}

}