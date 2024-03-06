package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;

/**
 * 适用于MinIO储存方案的配置（单例）
 */
@Data
public class MinioConfig {

	/**
	 * 唯一单例
	 */
	private static volatile MinioConfig INSTANCE;

	/**
	 * 获取MinIO配置实例
	 *
	 * @return MinIO配置信息
	 */
	public static MinioConfig getInstance() {
		if (INSTANCE == null) {
			synchronized (MinioConfig.class) {
				if (INSTANCE == null) {
					INSTANCE = new MinioConfig();
				}
			}
		}
		return INSTANCE;
	}

	private MinioConfig() {

	}

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