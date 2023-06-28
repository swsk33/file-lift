package io.github.swsk33.fileliftcore.model.file;

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

}