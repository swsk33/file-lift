package io.github.swsk33.fileliftcore.model.result;

import lombok.Data;

/**
 * 文件操作结果
 */
@Data
public class FileResult<T> {

	/**
	 * 是否操作成功
	 */
	private boolean success;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 返回的文件数据
	 */
	private T data;

	/**
	 * 创建一个成功的结果，但是不包含文件对象
	 *
	 * @param message 结果消息
	 * @return 结果对象
	 */
	public static FileResult<Void> resultSuccess(String message) {
		FileResult<Void> result = new FileResult<>();
		result.setSuccess(true);
		result.setMessage(message);
		return result;
	}

	/**
	 * 创建一个成功的结果
	 *
	 * @param message 结果消息
	 * @param data    结果包含的数据例如文件对象等等
	 * @return 结果对象
	 */
	public static <T> FileResult<T> resultSuccess(String message, T data) {
		FileResult<T> result = new FileResult<>();
		result.setSuccess(true);
		result.setMessage(message);
		result.setData(data);
		return result;
	}

	/**
	 * 创建一个失败的结果
	 *
	 * @param message 结果消息
	 * @return 结果对象
	 */
	public static <T> FileResult<T> resultFailed(String message) {
		FileResult<T> result = new FileResult<>();
		result.setSuccess(false);
		result.setMessage(message);
		return result;
	}

}