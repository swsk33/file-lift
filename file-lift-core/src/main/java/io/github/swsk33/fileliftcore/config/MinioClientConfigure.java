package io.github.swsk33.fileliftcore.config;

import io.github.swsk33.fileliftcore.model.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

/**
 * MinIO连接客户端配置（单例）
 */
public class MinioClientConfigure {

	/**
	 * 客户端对象
	 */
	private static volatile MinioClient minioClient;

	/**
	 * 私有化构造器
	 */
	private MinioClientConfigure() {

	}

	/**
	 * 获取MinIO客户端对象
	 *
	 * @return MinIO客户端对象
	 */
	public static MinioClient getMinioClient() {
		if (minioClient == null) {
			synchronized (MinioClientConfigure.class) {
				if (minioClient == null) {
					// 获取配置
					MinioConfig config = MinioConfig.getInstance();
					// 创建客户端
					minioClient = MinioClient.builder()
							.endpoint(config.getEndpoint())
							.credentials(config.getAccessKey(), config.getSecretKey()).build();
					try {
						// 检查桶是否存在，不存在创建
						String bucketName = config.getBucketName();
						boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
						if (!bucketExists) {
							minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return minioClient;
	}

}