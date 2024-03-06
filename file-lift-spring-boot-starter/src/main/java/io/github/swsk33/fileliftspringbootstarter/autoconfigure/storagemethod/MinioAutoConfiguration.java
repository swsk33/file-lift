package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import io.github.swsk33.fileliftcore.model.config.MinioConfig;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.property.MinioProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用Minio方案时，对该方案进行自动配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@AutoConfigureAfter(CoreConfigAutoConfiguration.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.MINIO)
public class MinioAutoConfiguration {

	@Bean
	public MinioConfig createMinioConfig(MinioProperties minioProperties) {
		MinioConfig config = MinioConfig.getInstance();
		config.setEndpoint(minioProperties.getEndpoint());
		config.setAccessKey(minioProperties.getAccessKey());
		config.setSecretKey(minioProperties.getSecretKey());
		config.setBucketName(minioProperties.getBucketName());
		log.info("使用MinIO储存方案");
		return config;
	}

}