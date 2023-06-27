package io.github.swsk33.fileliftcore.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文件校验结果类
 */
@Data
@AllArgsConstructor
public class FileValidateResult {

	/**
	 * 是否校验通过
	 */
	private boolean success;

	/**
	 * 校验消息
	 */
	private String message;

	/**
	 * 生成校验成功结果
	 *
	 * @param message 校验消息
	 * @return 成功结果
	 */
	public static FileValidateResult resultSuccess(String message) {
		return new FileValidateResult(true, message);
	}

	/**
	 * 生成校验失败结果
	 *
	 * @param message 校验消息
	 * @return 失败结果
	 */
	public static FileValidateResult resultFailed(String message) {
		return new FileValidateResult(false, message);
	}

}