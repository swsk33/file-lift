package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.util.IdUtil;
import io.github.swsk33.fileliftcore.strategy.FileNameStrategy;

/**
 * UUID文件名策略
 */
public class UUIDFileNameStrategy implements FileNameStrategy {

	@Override
	public String generateFileName() {
		return IdUtil.randomUUID();
	}

}