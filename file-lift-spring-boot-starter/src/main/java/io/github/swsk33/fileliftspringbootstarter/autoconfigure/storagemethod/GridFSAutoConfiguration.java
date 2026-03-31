package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 使用MongoDB GridFS方案时，对该方案进行自动配置
 */
@Slf4j
@AutoConfiguration(after = CoreConfigAutoConfiguration.class)
@EnableConfigurationProperties(MongoProperties.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.MONGO)
public class GridFSAutoConfiguration {

	@Bean
	public MongoConfig createMongoConfig(MongoProperties mongoProperties) {
		MongoConfig.MongoConfigBuilder builder = MongoConfig.builder();
		if (!StrUtil.isEmpty(mongoProperties.getUri())) {
			builder.uri(mongoProperties.getUri());
		} else {
			builder.host(mongoProperties.getHost() != null ? mongoProperties.getHost() : "127.0.0.1")
					.port(mongoProperties.getPort() != null ? mongoProperties.getPort() : 27017)
					.username(mongoProperties.getUsername())
					.password(String.valueOf(mongoProperties.getPassword()))
					.database(mongoProperties.getDatabase())
					.authDatabase(mongoProperties.getAuthenticationDatabase() != null ? mongoProperties.getAuthenticationDatabase() : "admin");
			if (mongoProperties.getGridfs() != null && !StrUtil.isEmpty(mongoProperties.getGridfs().getBucket())) {
				builder.bucketName(mongoProperties.getGridfs().getBucket());
			}
		}
		log.info("使用MongoDB GridFS储存方案");
		return builder.build();
	}

}