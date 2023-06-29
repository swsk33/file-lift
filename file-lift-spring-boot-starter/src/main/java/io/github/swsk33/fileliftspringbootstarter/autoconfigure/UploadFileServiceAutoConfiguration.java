package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.service.impl.UploadFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传服务的自动配置类
 */
@Slf4j
@Configuration
public class UploadFileServiceAutoConfiguration {

	@Autowired
	private FileConfig fileConfig;

	/**
	 * 自动配置核心服务类
	 */
	@Bean
	public UploadFileService uploadFileService() {
		UploadFileServiceImpl service = new UploadFileServiceImpl();
		service.setConfig(fileConfig);
		log.info("------- FileLift文件上传服务已完成自动配置( • ̀ω•́ )✧ -------");
		return service;
	}

}