package io.github.swsk33.fileliftcore.model.config;

/**
 * 抽象文件系统存储配置类，作为统一父类使用
 */
public abstract class StorageConfig {

	/**
	 * 判断配置对象是否是特定配置子类型
	 *
	 * @param config     配置对象
	 * @param configType 配置对象实际类型
	 * @return 是否是实际类型
	 */
	public static <T extends StorageConfig> boolean isConfigType(StorageConfig config, Class<T> configType) {
		return configType.isAssignableFrom(config.getClass());
	}

	/**
	 * 向下转型为具体的文件系统存储配置类型子类
	 *
	 * @param config     配置对象
	 * @param configType 配置对象实际类型
	 * @return 转换后实际子类实例
	 */
	public static <T extends StorageConfig> T castTo(StorageConfig config, Class<T> configType) {
		return configType.cast(config);
	}

}