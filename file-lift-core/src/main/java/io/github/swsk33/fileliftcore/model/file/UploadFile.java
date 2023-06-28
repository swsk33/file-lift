package io.github.swsk33.fileliftcore.model.file;

import io.github.swsk33.fileliftcore.model.BinaryContent;
import lombok.Data;

/**
 * 文件抽象类，存放文件信息，例如文件名路径等等元数据
 */
@Data
public abstract class UploadFile {

	/**
	 * 文件名
	 */
	private String name;

	/**
	 * 文件扩展名（不带.）
	 */
	private String format;

	/**
	 * 根据自身对象信息，转换为二进制文件内容信息对象
	 *
	 * @return 文件的二进制内容相关信息的对象
	 */
	public abstract BinaryContent toBinaryContent();

}