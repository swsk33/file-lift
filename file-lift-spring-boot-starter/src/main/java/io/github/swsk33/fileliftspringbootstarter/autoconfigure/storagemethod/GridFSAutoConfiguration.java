package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 使用MongoDB GridFS方案时，对该方案进行自动配置
 */
@Slf4j
@AutoConfiguration(after = CoreConfigAutoConfiguration.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.MONGO)
public class GridFSAutoConfiguration {

	private static final String MONGO_PROPERTIES_CLASS_NAME = "org.springframework.boot.autoconfigure.mongo.MongoProperties";

	/**
	 * 向下兼容Spring Boot 2.x - 3.x的MongoDB自动配置，使用内部类配合类加载条件进行
	 * 这里使用全限定类名，而不是import，因为在4.x环境下MongoProperties类型不存在，会导致无法加载类报错
	 */
	@AutoConfiguration
	@ConditionalOnClass(name = MONGO_PROPERTIES_CLASS_NAME)
	@EnableConfigurationProperties(org.springframework.boot.autoconfigure.mongo.MongoProperties.class)
	static class Boot3MongoConfig {

		@Bean
		public MongoConfig lowerMongoConfig(org.springframework.boot.autoconfigure.mongo.MongoProperties properties) {
			// 获取配置绑定
			MongoConfig.MongoConfigBuilder builder = MongoConfig.builder();
			if (!StrUtil.isEmpty(properties.getUri())) {
				builder.uri(properties.getUri());
			} else {
				builder.host(properties.getHost())
						.port(properties.getPort())
						.username(properties.getUsername())
						.password(String.valueOf(properties.getPassword()))
						.database(properties.getDatabase())
						.authDatabase(properties.getAuthenticationDatabase())
						.bucketName(properties.getGridfs().getBucket());
			}
			log.info("使用MongoDB GridFS储存方案（从MongoProperties类型获取配置）");
			return builder.build();
		}

	}

	/**
	 * 用于新的Spring Boot 4.x的加载内部类
	 */
	@AutoConfiguration
	@ConditionalOnMissingClass(MONGO_PROPERTIES_CLASS_NAME)
	static class Boot4MongoConfig {

		/**
		 * 自动配置 MongoDB 存储方案，适用于新的Spring Boot 4.x版本
		 *
		 * @param environment 环境变量对象
		 * @return MongoDB 存储配置
		 */
		@Bean
		public MongoConfig newerMongoConfig(Environment environment) {
			// 配置前缀
			String configPrefix = "spring.mongodb.";
			String dataConfigPrefix = "spring.data.mongodb.";
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
						.bucketName(environment.getProperty(dataConfigPrefix + "gridfs.bucket", String.class, "fs"));
			}
			log.info("使用MongoDB GridFS储存方案（从配置环境变量获取配置）");
			return builder.build();
		}

	}

}