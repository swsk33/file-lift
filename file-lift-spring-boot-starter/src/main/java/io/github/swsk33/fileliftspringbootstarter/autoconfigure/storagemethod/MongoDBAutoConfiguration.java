package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.ConfigCheckAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.property.CoreProperties;
import io.github.swsk33.fileliftspringbootstarter.property.storagemethod.MongoDBProperties;
import lombok.extern.slf4j.Slf4j;
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
@EnableConfigurationProperties({CoreProperties.class, MongoDBProperties.class})
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.MONGO)
@AutoConfigureBefore(ConfigCheckAutoConfiguration.class)
public class MongoDBAutoConfiguration {

	@Bean
	public MongoConfig initMongoConfig(CoreProperties coreProperties, MongoDBProperties mongoDBProperties) {
		log.info("使用MongoDB GridFS储存方案");
		MongoConfig config = MongoConfig.getInstance();
		coreProperties.setConfigValues(config);
		mongoDBProperties.setConfigValues(config);
		return config;
	}

}