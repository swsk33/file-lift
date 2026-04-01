package io.github.swsk33.fileliftcore.model.config;

import lombok.*;
import software.amazon.awssdk.regions.Region;

/**
 * S3文件系统访问配置
 */
@Value
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class S3Config extends StorageConfig {

	/**
	 * 访问地址，需要是：<code>http(s)://host:port</code>形式
	 * 默认：http://127.0.0.1:9000
	 */
	@Builder.Default
	String endpoint = "http://127.0.0.1:9000";

	/**
	 * 访问密钥 AK
	 */
	String accessKey;

	/**
	 * 加密密钥 SK
	 */
	String secretKey;

	/**
	 * 地域，参考{@link Region}，自部署S3服务可忽略
	 * 默认：US_EAST_1
	 */
	@Builder.Default
	Region region = Region.US_EAST_1;

	/**
	 * 是否开启S3路径访问风格
	 * 设为false则采用虚拟主机 (Virtual Hosted) 风格
	 * 默认：true
	 */
	@Builder.Default
	boolean pathStyle = true;

	/**
	 * 桶名
	 */
	String bucketName;

}