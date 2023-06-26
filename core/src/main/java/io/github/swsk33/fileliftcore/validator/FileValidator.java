package io.github.swsk33.fileliftcore.validator;

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
	 * @param file 传入上传的文件对象
	 * @return 是否校验通过
	 */
	public abstract boolean validateFile(MultipartFile file);

}