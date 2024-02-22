package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用MongoDB GridFS方案时，对该方案进行自动配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
@AutoConfigureAfter(CoreConfigAutoConfiguration.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.MONGO)
public class GridFSAutoConfiguration {

	@Bean
	public MongoConfig createMongoConfig(MongoProperties mongoProperties) {
		MongoConfig config = MongoConfig.getInstance();
		if (!StrUtil.isEmpty(mongoProperties.getUri())) {
			config.setUri(mongoProperties.getUri());
		} else {
			config.setHost(mongoProperties.getHost() != null ? mongoProperties.getHost() : "127.0.0.1");
			config.setPort(mongoProperties.getPort() != null ? mongoProperties.getPort() : 27017);
			config.setUsername(mongoProperties.getUsername());
			config.setPassword(String.valueOf(mongoProperties.getPassword()));
			config.setDatabase(mongoProperties.getDatabase());
			config.setAuthDatabase(mongoProperties.getAuthenticationDatabase() != null ? mongoProperties.getAuthenticationDatabase() : "admin");
			if (!StrUtil.isEmpty(mongoProperties.getGridfs().getBucket())) {
				config.setBucketName(mongoProperties.getGridfs().getBucket());
			}
		}
		log.info("使用MongoDB GridFS储存方案");
		return config;
	}

}