package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;

/**
 * 适用于文件系统储存的配置（单例）
 */
@Data
public class FileSystemConfig extends FileConfig {

	/**
	 * 私有化构造器
	 */
	private FileSystemConfig() {

	}

	/**
	 * 获取唯一的单例
	 *
	 * @return 唯一单例
	 */
	public static FileSystemConfig getInstance() {
		// 双检锁延迟初始化
		if (INSTANCE == null) {
			synchronized (FileSystemConfig.class) {
				if (INSTANCE == null) {
					INSTANCE = new FileSystemConfig();
				}
			}
		}
		return (FileSystemConfig) INSTANCE;
	}

	/**
	 * 存放路径，值为文件夹
	 * 可以以相对路径进行配置，也可以是绝对路径
	 */
	private String saveFolder = "file-data";

}