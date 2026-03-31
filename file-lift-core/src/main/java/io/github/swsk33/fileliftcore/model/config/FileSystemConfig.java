package io.github.swsk33.fileliftcore.model.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * 适用于本地文件系统储存的配置
 */
@Value
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FileSystemConfig extends StorageConfig {

	/**
	 * 存放路径，值为文件夹
	 * 可以以相对路径进行配置，也可以是绝对路径
	 * 默认：file-data
	 */
	@Builder.Default
	String saveFolder = "file-data";

}