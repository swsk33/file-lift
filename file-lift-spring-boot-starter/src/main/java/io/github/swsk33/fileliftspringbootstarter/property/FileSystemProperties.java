package io.github.swsk33.fileliftspringbootstarter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件系统储存方案配置读取
 */
@Data
@Component
@ConfigurationProperties(prefix = "io.github.swsk33.file-lift.filesystem")
public class FileSystemProperties {

	/**
	 * 存放路径，值为文件夹
	 * 可以以相对路径进行配置，也可以是绝对路径
	 */
	private String saveFolder = "file-data";

}