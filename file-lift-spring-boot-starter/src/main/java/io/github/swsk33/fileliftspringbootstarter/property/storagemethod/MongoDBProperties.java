package io.github.swsk33.fileliftspringbootstarter.property.storagemethod;

import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 获取MongoDB连接配置
 */
@Data
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoDBProperties {

	/**
	 * MongoDB数据库地址
	 */
	private String host;

	/**
	 * MongoDB数据库端口
	 */
	private int port;

	/**
	 * MongoDB数据库的默认库名
	 */
	private String database;

	/**
	 * MongoDB数据库用户名
	 */
	private String username;

	/**
	 * MongoDB数据库密码
	 */
	private String password;

	/**
	 * MongoDB用户认证数据库
	 */
	private String authenticationDatabase;

	/**
	 * 传入配置对象，将这个类中的获取到的配置值设定到配置对象中去
	 *
	 * @param config 配置对象
	 */
	public void setConfigValues(MongoConfig config) {
		config.setHost(host);
		config.setPort(port);
		config.setDatabase(database);
		config.setUsername(username);
		config.setPassword(password);
		config.setAuthDatabase(authenticationDatabase);
	}

}