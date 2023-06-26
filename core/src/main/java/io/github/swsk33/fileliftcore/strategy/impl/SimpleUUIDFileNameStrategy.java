package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.util.IdUtil;
import io.github.swsk33.fileliftcore.strategy.FileNameStrategy;

/**
 * SimpleUUID方案文件名生成策略
 */
public class SimpleUUIDFileNameStrategy implements FileNameStrategy {

	@Override
	public String generateFileName() {
		return IdUtil.simpleUUID();
	}

}