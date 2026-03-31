package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;
import software.amazon.awssdk.regions.Region;

/**
 * S3文件系统访问配置
 */
@Data
public class S3Config {

	/**
	 * 访问地址
	 */
	private String endpoint;

	/**
	 * 访问密钥 AK
	 */
	private String accessKey;

	/**
	 * 加密密钥 SK
	 */
	private String secretKey;

	/**
	 * 地域，参考{@link Region}，自部署S3服务可忽略
	 * 默认：AWS_GLOBAL
	 */
	private Region region = Region.AWS_GLOBAL;

	/**
	 * 是否开启S3路径访问风格
	 * 设为false则采用虚拟主机 (Virtual Hosted) 风格
	 * 默认：true
	 */
	private boolean pathStyle = true;

	/**
	 * 桶名
	 */
	private String bucketName;

}