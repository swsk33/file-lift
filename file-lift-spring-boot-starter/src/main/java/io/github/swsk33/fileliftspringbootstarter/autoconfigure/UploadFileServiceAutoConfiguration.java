package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.service.UploadFileService;
import io.github.swsk33.fileliftcore.service.impl.UploadFileServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传服务的自动配置类
 */
@Configuration
public class UploadFileServiceAutoConfiguration {

	@Bean
	public UploadFileService uploadFileService() {
		return new UploadFileServiceImpl();
	}

}