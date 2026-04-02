package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.config.StorageConfig;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.service.impl.UploadFileServiceImpl;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod.FileSystemAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod.GridFSAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod.S3AutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 文件上传服务的自动配置类
 */
@Slf4j
@AutoConfiguration(after = {FileSystemAutoConfiguration.class, GridFSAutoConfiguration.class, S3AutoConfiguration.class})
public class UploadFileServiceAutoConfiguration {

	/**
	 * 自动配置核心服务类
	 */
	@Bean
	@ConditionalOnMissingBean
	public UploadFileService uploadFileService(CoreConfig coreConfig, StorageConfig storageConfig) {
		UploadFileServiceImpl service = new UploadFileServiceImpl(coreConfig, storageConfig);
		log.info("------- FileLift文件上传服务已完成自动配置( • ̀ω•́ )✧ -------");
		return service;
	}

}