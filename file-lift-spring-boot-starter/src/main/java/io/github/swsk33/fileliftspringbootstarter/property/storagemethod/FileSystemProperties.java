package io.github.swsk33.fileliftspringbootstarter.property.storagemethod;

import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 获取文件系统储存方案配置
 */
@Data
@ConfigurationProperties(prefix = "io.github.swsk33.file-lift.filesystem")
public class FileSystemProperties {

	/**
	 * 存放路径，值为文件夹
	 * 可以以相对路径进行配置，也可以是绝对路径
	 */
	private String saveFolder = "file-data";

	/**
	 * 传入配置对象，将这个类中的获取到的配置值设定到配置对象中去
	 *
	 * @param config 配置对象
	 */
	public void setConfigValues(FileSystemConfig config) {
		config.setSaveFolder(saveFolder);
	}

}