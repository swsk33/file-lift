package io.github.swsk33.fileliftcore.strategy.context;

import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.model.config.S3Config;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.FileSystemProcessStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.MongoDBFileProcessStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.S3FileProcessStrategy;

/**
 * 用于创建文件处理策略的上下文
 */
public class FileProcessStrategyContext {

	/**
	 * 私有化构造器
	 */
	private FileProcessStrategyContext() {

	}

	/**
	 * 根据配置创建策略对象
	 *
	 * @param coreConfig    核心配置
	 * @param storageConfig 对应储存方式的配置对象
	 * @return 文件处理策略对象
	 */
	public static FileProcessStrategy createStrategy(CoreConfig coreConfig, Object storageConfig) {
		// 获取配置的文件系统类型
		String storageMethod = coreConfig.getStorageMethod();
		// 文件系统存储
		if (!FileStorageMethods.contains(storageMethod) || FileStorageMethods.FILE.equals(storageMethod)) {
			if (!(storageConfig instanceof FileSystemConfig)) {
				throw new IllegalArgumentException("本地文件系统储存方式需要传入 FileSystemConfig 类型配置");
			}
			return new FileSystemProcessStrategy((FileSystemConfig) storageConfig);
		}
		// MongoDB GridFS 存储
		if (FileStorageMethods.MONGO.equals(storageMethod)) {
			if (!(storageConfig instanceof MongoConfig)) {
				throw new IllegalArgumentException("MongoDB GridFS 储存方式需要传入 MongoConfig 类型配置");
			}
			return new MongoDBFileProcessStrategy(coreConfig, (MongoConfig) storageConfig);
		}
		// S3 协议文件系统
		if (FileStorageMethods.S3.equals(storageMethod)) {
			if (!(storageConfig instanceof S3Config)) {
				throw new IllegalArgumentException("S3储存方式需要传入 S3Config 类型配置");
			}
			return new S3FileProcessStrategy(coreConfig, (S3Config) storageConfig);
		}
		throw new IllegalArgumentException("不支持的文件储存方式：" + storageMethod);
	}

}