package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.property.FileSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用文件系统方案时，对该方案进行自动配置
 */
@Slf4j
@Configuration
@AutoConfigureAfter(CoreConfigAutoConfiguration.class)
@EnableConfigurationProperties(FileSystemProperties.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.FILE, matchIfMissing = true)
public class FileSystemAutoConfiguration {

	/**
	 * 初始化文件系统的储存方案配置信息
	 *
	 * @param fileSystemProperties 读取文件系统储存方案的配置
	 * @return 文件系统储存方案配置信息
	 */
	@Bean
	public FileSystemConfig createFileSystemConfig(FileSystemProperties fileSystemProperties) {
		FileSystemConfig config = FileSystemConfig.getInstance();
		config.setSaveFolder(fileSystemProperties.getSaveFolder());
		log.info("使用本地文件系统储存方案");
		return config;
	}

}