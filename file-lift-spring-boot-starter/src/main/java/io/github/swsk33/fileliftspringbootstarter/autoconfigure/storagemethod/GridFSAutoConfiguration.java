package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 使用MongoDB GridFS方案时，对该方案进行自动配置
 */
@Slf4j
@AutoConfiguration(after = CoreConfigAutoConfiguration.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.MONGO)
public class GridFSAutoConfiguration {

	/**
	 * 自动配置 MongoDB 存储方案
	 *
	 * @param environment 环境变量对象，从配置环境中读取而非MongoProperties，兼容多个版本
	 * @return MongoDB 存储配置
	 */
	@Bean
	public MongoConfig createMongoConfig(Environment environment) {
		// 配置前缀
		String configPrefix = "spring.data.mongodb.";
		// 获取配置绑定
		MongoConfig.MongoConfigBuilder builder = MongoConfig.builder();
		// 读取配置并构建
		String uri = environment.getProperty(configPrefix + "uri", String.class);
		if (!StrUtil.isEmpty(uri)) {
			builder.uri(uri);
		} else {
			builder.host(environment.getProperty(configPrefix + "host", String.class, "127.0.0.1"))
					.port(environment.getProperty(configPrefix + "port", Integer.class, 27017))
					.username(environment.getProperty(configPrefix + "username", String.class))
					.password(environment.getProperty(configPrefix + "password", String.class))
					.database(environment.getProperty(configPrefix + "database", String.class))
					.authDatabase(environment.getProperty(configPrefix + "authentication-database", String.class, "admin"))
					.bucketName(environment.getProperty(configPrefix + "gridfs.bucket", String.class, "fs"));
		}
		log.info("使用MongoDB GridFS储存方案");
		return builder.build();
	}

}