package io.github.swsk33.fileliftcore.strategy.context;

import io.github.swsk33.fileliftcore.param.AutoRenameFormats;
import io.github.swsk33.fileliftcore.strategy.FileNameStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.SimpleUUIDFileNameStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.SnowflakeFileNameStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.TimeBasedFileNameStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.UUIDFileNameStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件名策略上下文
 */
public class FileNameStrategyContext {

	/**
	 * 存放所有文件名生成策略的哈希表
	 */
	public static final Map<String, FileNameStrategy> FILE_NAME_STRATEGY_MAP = new HashMap<>();

	// 初始化策略
	static {
		FILE_NAME_STRATEGY_MAP.put(AutoRenameFormats.UUID, new UUIDFileNameStrategy());
		FILE_NAME_STRATEGY_MAP.put(AutoRenameFormats.SIMPLE_UUID, new SimpleUUIDFileNameStrategy());
		FILE_NAME_STRATEGY_MAP.put(AutoRenameFormats.SNOW_FLAKE, new SnowflakeFileNameStrategy());
		FILE_NAME_STRATEGY_MAP.put(AutoRenameFormats.TIME, new TimeBasedFileNameStrategy());
	}

	/**
	 * 传入文件名形式以调用策略随机生成文件名
	 *
	 * @param fileNameFormat 文件名格式
	 * @return 随机生成的文件名
	 */
	public static String generateFileName(String fileNameFormat) {
		if (!FILE_NAME_STRATEGY_MAP.containsKey(fileNameFormat)) {
			return FILE_NAME_STRATEGY_MAP.get(AutoRenameFormats.SIMPLE_UUID).generateFileName();
		}
		return FILE_NAME_STRATEGY_MAP.get(fileNameFormat).generateFileName();
	}

}