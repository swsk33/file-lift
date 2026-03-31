package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;

/**
 * 适用于文件系统储存的配置（单例）
 */
@Data
public class FileSystemConfig {

	/**
	 * 存放路径，值为文件夹
	 * 可以以相对路径进行配置，也可以是绝对路径
	 */
	private String saveFolder = "file-data";

}