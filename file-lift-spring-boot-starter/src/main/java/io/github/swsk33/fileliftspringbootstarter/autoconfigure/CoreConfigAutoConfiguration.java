package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 自动配置核心的配置对象
 */
@AutoConfiguration(before = UploadFileServiceAutoConfiguration.class)
@EnableConfigurationProperties(CoreProperties.class)
public class CoreConfigAutoConfiguration {

	@Bean
	public CoreConfig createFileConfig(CoreProperties coreProperties) {
		return CoreConfig.builder()
				.storageMethod(coreProperties.getStorageMethod())
				.allowedFormats(coreProperties.getAllowedFormats())
				.sizeLimit(coreProperties.getSizeLimit())
				.override(coreProperties.isOverride())
				.autoRename(coreProperties.isAutoRename())
				.autoRenameFormat(coreProperties.getAutoRenameFormat())
				.build();
	}

}