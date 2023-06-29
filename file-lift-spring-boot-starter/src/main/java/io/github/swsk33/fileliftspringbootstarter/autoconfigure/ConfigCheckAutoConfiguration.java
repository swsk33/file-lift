package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
import io.github.swsk33.fileliftspringbootstarter.property.storagemethod.FileSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于检查配置单例是否被正确初始化的自动配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CoreProperties.class, FileSystemProperties.class})
@AutoConfigureBefore(UploadFileServiceAutoConfiguration.class)
public class ConfigCheckAutoConfiguration {

	/**
	 * 如果配置类未被成功注入，可能是配置文件内容错误，进行默认的配置初始化
	 */
	@Bean
	@ConditionalOnMissingBean(FileConfig.class)
	public FileConfig defaultConfig(CoreProperties coreProperties, FileSystemProperties fileSystemProperties) {
		log.warn("检测到配置未初始化！可能配置文件内容错误！初始化为默认的文件系统储存方案配置！");
		FileSystemConfig config = FileSystemConfig.getInstance();
		coreProperties.setConfigValues(config);
		fileSystemProperties.setConfigValues(config);
		return config;
	}

}