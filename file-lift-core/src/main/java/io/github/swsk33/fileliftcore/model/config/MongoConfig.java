package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;

/**
 * 适用于MongoDB GridFS储存的配置（单例）
 */
@Data
public class MongoConfig {

	/**
	 * 唯一单例
	 */
	private static volatile MongoConfig INSTANCE;

	/**
	 * 获取文件系统方案配置单例
	 */
	public static MongoConfig getInstance() {
		if (INSTANCE == null) {
			synchronized (MongoConfig.class) {
				if (INSTANCE == null) {
					INSTANCE = new MongoConfig();
				}
			}
		}
		return INSTANCE;
	}

	private MongoConfig() {

	}

	/**
	 * MongoDB连接字符串<br>
	 * 连接字符串格式：
	 * <ul>
	 *     <li>单节点：mongodb://user:password@host:port/database?authSource=authDatabase</li>
	 *     <li>集群：mongodb://user:password@host1:port1,host2:port2,host3:port3/database?authSource=authDatabase</li>
	 * </ul>
	 * 若配置了该连接字符串，则下列其余地址配置将无效：
	 * <ul>
	 *     <li>host</li>
	 *     <li>port</li>
	 *     <li>username</li>
	 *     <li>password</li>
	 *     <li>database</li>
	 *     <li>authDatabase</li>
	 * </ul>
	 */
	private String uri;

	/**
	 * 数据库地址
	 * 默认为：127.0.0.1
	 */
	private String host = "127.0.0.1";

	/**
	 * 数据库端口
	 * 默认为：27017
	 */
	private int port = 27017;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 连接的数据库
	 * 最好是和项目数据库一致
	 */
	private String database;

	/**
	 * 认证数据库
	 * 默认为：admin
	 */
	private String authDatabase = "admin";

	/**
	 * 文件存放桶名称
	 * 默认为：fs
	 */
	private String bucketName = "fs";

}