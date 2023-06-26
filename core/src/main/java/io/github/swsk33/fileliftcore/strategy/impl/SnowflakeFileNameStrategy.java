package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.util.IdUtil;
import io.github.swsk33.fileliftcore.strategy.FileNameStrategy;

/**
 * 雪花ID文件名生成策略
 */
public class SnowflakeFileNameStrategy implements FileNameStrategy {

	@Override
	public String generateFileName() {
		return IdUtil.getSnowflakeNextIdStr();
	}

}