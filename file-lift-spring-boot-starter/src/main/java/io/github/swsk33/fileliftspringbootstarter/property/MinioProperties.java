package io.github.swsk33.fileliftspringbootstarter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Minio储存方案配置读取
 */
@Data
@Component
@ConfigurationProperties(prefix = "io.github.swsk33.file-lift.minio")
public class MinioProperties {

	/**
	 * MinIO服务器地址和端口
	 * 默认为：http://127.0.0.1:9000
	 */
	private String endpoint = "http://127.0.0.1:9000";

	/**
	 * 认证用的Access Key
	 */
	private String accessKey;

	/**
	 * 认证用的Secret Key
	 */
	private String secretKey;

	/**
	 * 存放上传文件的桶名称，桶不存在会被自动创建
	 * 默认为：file-lift-objects
	 */
	private String bucketName = "file-lift-objects";

}