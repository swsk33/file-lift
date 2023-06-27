package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
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
@EnableConfigurationProperties({CoreProperties.class, FileSystemConfig.class})
@ConditionalOnProperty(prefix = "io.github.swsk33", name = "storage-method", havingValue = FileStorageMethods.FILE, matchIfMissing = true)
@AutoConfigureBefore(UploadFileServiceAutoConfiguration.class)
public class FileSystemAutoConfiguration {

	@Bean
	public FileSystemConfig initConfig(CoreProperties coreProperties, FileSystemConfig fileSystemConfig) {
		log.info("使用文件系统本地储存方案");
		FileSystemConfig config = FileSystemConfig.getInstance();
		config.setStorageMethod(coreProperties.getStorageMethod());
		config.setAllowedFormats(coreProperties.getAllowedFormats());
		config.setSizeLimit(coreProperties.getSizeLimit());
		config.setAutoRename(coreProperties.isAutoRename());
		config.setOverride(coreProperties.isOverride());
		config.setAutoRenameFormat(coreProperties.getAutoRenameFormat());
		config.setSaveFolder(fileSystemConfig.getSaveFolder());
		return config;
	}

}