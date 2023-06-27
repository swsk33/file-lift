package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

/**
 * 用于打印触发自动配置消息的类
 */
@Slf4j
@Configuration
@AutoConfigureBefore({FileSystemAutoConfiguration.class, MongoDBAutoConfiguration.class})
public class LogPrintAutoConfiguration {

	@PostConstruct
	private void logMessage() {
		log.info("FileLift文件上传服务开始自动配置( • ̀ω•́ )✧");
	}

}