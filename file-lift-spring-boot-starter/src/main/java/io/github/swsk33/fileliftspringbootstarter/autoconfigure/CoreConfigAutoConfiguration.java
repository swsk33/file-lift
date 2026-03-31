package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置核心的配置对象
 */
@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@AutoConfigureBefore(UploadFileServiceAutoConfiguration.class)
public class CoreConfigAutoConfiguration {

	@Bean
	public CoreConfig createFileConfig(CoreProperties coreProperties) {
		CoreConfig config = CoreConfig.getInstance();
		config.setStorageMethod(coreProperties.getStorageMethod());
		config.setAllowedFormats(coreProperties.getAllowedFormats());
		config.setSizeLimit(coreProperties.getSizeLimit());
		config.setOverride(coreProperties.isOverride());
		config.setAutoRename(coreProperties.isAutoRename());
		config.setAutoRenameFormat(coreProperties.getAutoRenameFormat());
		return config;
	}

}