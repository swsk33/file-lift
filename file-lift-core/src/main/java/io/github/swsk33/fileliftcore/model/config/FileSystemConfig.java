package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;

/**
 * 适用于文件系统储存的配置（单例）
 */
@Data
public class FileSystemConfig {

	/**
	 * 唯一单例
	 */
	private static volatile FileSystemConfig INSTANCE;

	/**
	 * 获取文件系统方案配置单例
	 */
	public static FileSystemConfig getInstance() {
		if (INSTANCE == null) {
			synchronized (FileSystemConfig.class) {
				if (INSTANCE == null) {
					INSTANCE = new FileSystemConfig();
				}
			}
		}
		return INSTANCE;
	}

	private FileSystemConfig() {

	}

	/**
	 * 存放路径，值为文件夹
	 * 可以以相对路径进行配置，也可以是绝对路径
	 */
	private String saveFolder = "file-data";

}