package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.ConfigCheckAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
import io.github.swsk33.fileliftspringbootstarter.property.storagemethod.FileSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于自动配置文件系统存储方案配置的配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CoreProperties.class, FileSystemProperties.class})
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.FILE, matchIfMissing = true)
@AutoConfigureBefore(ConfigCheckAutoConfiguration.class)
public class FileSystemAutoConfiguration {

	@Bean
	public FileSystemConfig initFileSystemConfig(CoreProperties coreProperties, FileSystemProperties fileSystemProperties) {
		log.info("使用文件系统本地储存方案");
		FileSystemConfig config = FileSystemConfig.getInstance();
		coreProperties.setConfigValues(config);
		fileSystemProperties.setConfigValues(config);
		return config;
	}

}