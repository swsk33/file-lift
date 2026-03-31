package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.property.FileSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 使用文件系统方案时，对该方案进行自动配置
 */
@Slf4j
@AutoConfiguration(after = CoreConfigAutoConfiguration.class)
@EnableConfigurationProperties(FileSystemProperties.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.FILE, matchIfMissing = true)
public class FileSystemAutoConfiguration {

	@Bean
	public FileSystemConfig createFileSystemConfig(FileSystemProperties fileSystemProperties) {
		FileSystemConfig config = FileSystemConfig.builder()
				.saveFolder(fileSystemProperties.getSaveFolder())
				.build();
		log.info("使用本地文件系统储存方案");
		return config;
	}

}