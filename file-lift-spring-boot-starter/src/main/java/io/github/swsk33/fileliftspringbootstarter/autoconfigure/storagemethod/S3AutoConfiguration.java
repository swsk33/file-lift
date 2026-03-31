package io.github.swsk33.fileliftspringbootstarter.autoconfigure.storagemethod;

import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.S3Config;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftspringbootstarter.autoconfigure.CoreConfigAutoConfiguration;
import io.github.swsk33.fileliftspringbootstarter.property.S3Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;

/**
 * 使用S3方案时，对该方案进行自动配置
 */
@Slf4j
@AutoConfiguration(after = CoreConfigAutoConfiguration.class)
@EnableConfigurationProperties(S3Properties.class)
@ConditionalOnProperty(prefix = "io.github.swsk33.file-lift.core", name = "storage-method", havingValue = FileStorageMethods.S3)
public class S3AutoConfiguration {

	@Bean
	public S3Config createS3Config(S3Properties s3Properties) {
		S3Config.S3ConfigBuilder builder = S3Config.builder();
		builder.endpoint(s3Properties.getEndpoint())
				.accessKey(s3Properties.getAccessKey())
				.secretKey(s3Properties.getSecretKey())
				.pathStyle(s3Properties.isPathStyle())
				.bucketName(s3Properties.getBucketName());
		if (!StrUtil.isEmpty(s3Properties.getRegion())) {
			builder.region(Region.of(s3Properties.getRegion()));
		}
		log.info("使用S3对象存储方案");
		return builder.build();
	}

}