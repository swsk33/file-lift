package io.github.swsk33.fileliftspringbootstarter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * S3储存方案配置读取
 */
@Data
@Component
@ConfigurationProperties(prefix = "io.github.swsk33.file-lift.s3")
public class S3Properties {

	/**
	 * S3服务访问地址
	 * 默认为：<code>http://127.0.0.1:9000</code>
	 */
	private String endpoint = "http://127.0.0.1:9000";

	/**
	 * 认证用的 Access Key
	 */
	private String accessKey;

	/**
	 * 认证用的 Secret Key
	 */
	private String secretKey;

	/**
	 * 地域，默认：us-east-1
	 */
	private String region = "us-east-1";

	/**
	 * 是否开启路径访问风格
	 */
	private boolean pathStyle = true;

	/**
	 * 存放上传文件的桶名称
	 */
	private String bucketName;

}