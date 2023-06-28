package io.github.swsk33.fileliftcore.config;

import cn.hutool.core.util.StrUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;

import static io.github.swsk33.fileliftcore.util.URLEncodeUtils.percentEncode;

/**
 * MongoDB连接客户端配置（单例）
 */
public class MongoClientConfig {

	/**
	 * 数据库客户端对象
	 */
	private static volatile MongoClient mongoClient;

	/**
	 * GridFS存储桶对象
	 */
	private static GridFSBucket bucket;

	/**
	 * 私有化构造器
	 */
	private MongoClientConfig() {

	}

	/**
	 * 获取MongoDB连接客户端
	 *
	 * @return MongoDB连接客户端
	 */
	private static MongoClient getMongoClient() {
		// 使用双检锁延迟初始化
		if (mongoClient == null) {
			synchronized (MongoClientConfig.class) {
				if (mongoClient == null) {
					// 读取配置完成连接
					MongoConfig config = MongoConfig.getInstance();
					// 拼接连接字符串
					StringBuilder url = new StringBuilder("mongodb://");
					if (!StrUtil.isEmpty(config.getUsername()) && !StrUtil.isEmpty(config.getPassword())) {
						url.append(percentEncode(config.getUsername())).append(":").append(percentEncode(config.getPassword())).append("@");
					}
					url.append(config.getHost()).append(":").append(config.getPort()).append("/").append(config.getDatabase());
					url.append("?").append("authSource=").append(config.getAuthDatabase());
					// 发起连接以创建MongoDB客户端
					mongoClient = MongoClients.create(url.toString());
				}
			}
		}
		return mongoClient;
	}

	/**
	 * 获取GridFS桶对象
	 *
	 * @return 桶对象
	 */
	public static GridFSBucket getBucket() {
		// 调用时检查是否初始化
		if (bucket == null) {
			// 初始化桶对象
			bucket = GridFSBuckets.create(getMongoClient().getDatabase(MongoConfig.getInstance().getDatabase()));
		}
		return bucket;
	}

}