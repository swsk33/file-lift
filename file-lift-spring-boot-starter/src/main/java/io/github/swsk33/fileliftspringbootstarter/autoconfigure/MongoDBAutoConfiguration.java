package io.github.swsk33.fileliftspringbootstarter.autoconfigure;

import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于自动配置MongoDB GridFS储存方案配置的配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CoreProperties.class})
@ConditionalOnProperty(prefix = "io.github.swsk33", name = "storage-method", havingValue = FileStorageMethods.MONGO)
@AutoConfigureBefore(UploadFileServiceAutoConfiguration.class)
public class MongoDBAutoConfiguration {

	// 下面注入一些MongoDB的配置值

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port:27017")
	private int port;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Value("${spring.data.mongodb.username}")
	private String username;

	@Value("${spring.data.mongodb.password}")
	private String password;

	@Value("${spring.data.mongodb.authentication-database:admin}")
	private String authDatabase;

	@Bean
	public MongoConfig mongoConfig(CoreProperties coreProperties) {
		log.info("使用MongoDB GridFS储存方案");
		MongoConfig config = MongoConfig.getInstance();
		config.setStorageMethod(coreProperties.getStorageMethod());
		config.setAllowedFormats(coreProperties.getAllowedFormats());
		config.setSizeLimit(coreProperties.getSizeLimit());
		config.setAutoRename(coreProperties.isAutoRename());
		config.setOverride(coreProperties.isOverride());
		config.setAutoRenameFormat(coreProperties.getAutoRenameFormat());
		config.setHost(host);
		config.setPort(port);
		config.setDatabase(database);
		config.setUsername(username);
		config.setPassword(password);
		config.setAuthDatabase(authDatabase);
		return config;
	}

}