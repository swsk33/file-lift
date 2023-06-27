package io.github.swsk33.fileliftcore.model.config;

import lombok.Data;

/**
 * 适用于MongoDB GridFS储存的配置（单例）
 */
@Data
public class MongoConfig extends FileConfig {

	/**
	 * 私有化构造器
	 */
	private MongoConfig() {

	}

	/**
	 * 获取唯一单例
	 *
	 * @return 唯一单例
	 */
	public static MongoConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MongoConfig();
		}
		return (MongoConfig) INSTANCE;
	}

	/**
	 * 数据库地址
	 */
	private String host;

	/**
	 * 数据库端口
	 * 默认为27017
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
	 * 默认为admin
	 */
	private String authDatabase = "admin";

}