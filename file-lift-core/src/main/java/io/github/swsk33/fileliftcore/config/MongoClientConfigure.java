package io.github.swsk33.fileliftcore.config;

import cn.hutool.core.util.StrUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;

import java.net.URI;

import static io.github.swsk33.fileliftcore.util.URLEncodeUtils.percentEncode;

/**
 * MongoDB连接客户端配置（单例）
 */
public class MongoClientConfigure {

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
	private MongoClientConfigure() {

	}

	/**
	 * 获取MongoDB连接客户端
	 *
	 * @return MongoDB连接客户端
	 */
	private static MongoClient getMongoClient() {
		// 使用双检锁延迟初始化
		if (mongoClient == null) {
			synchronized (MongoClientConfigure.class) {
				if (mongoClient == null) {
					// 获取配置完成连接
					MongoConfig config = MongoConfig.getInstance();
					// 如果uri属性已被配置，则直接使用uri属性
					if (!StrUtil.isEmpty(config.getUri())) {
						mongoClient = MongoClients.create(config.getUri());
						// 解析并且设定数据库
						try {
							config.setDatabase(new URI(config.getUri()).getPath().substring(1));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// 拼接连接字符串
						// 协议头
						StringBuilder uri = new StringBuilder("mongodb://");
						// 用户名密码部分
						if (!StrUtil.isEmpty(config.getUsername()) && !StrUtil.isEmpty(config.getPassword())) {
							uri.append(percentEncode(config.getUsername())).append(":").append(percentEncode(config.getPassword())).append("@");
						}
						// 地址部分
						uri.append(config.getHost()).append(":").append(config.getPort());
						// 数据库部分
						uri.append("/").append(config.getDatabase());
						// 参数部分
						uri.append("?").append("authSource=").append(config.getAuthDatabase());
						// 发起连接以创建MongoDB客户端
						mongoClient = MongoClients.create(uri.toString());
					}
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
			MongoClient client = getMongoClient();
			MongoConfig config = MongoConfig.getInstance();
			bucket = GridFSBuckets.create(client.getDatabase(config.getDatabase()), config.getBucketName());
		}
		return bucket;
	}

}