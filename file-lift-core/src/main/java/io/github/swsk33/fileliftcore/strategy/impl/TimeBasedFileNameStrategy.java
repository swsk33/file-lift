package io.github.swsk33.fileliftcore.strategy.impl;

import io.github.swsk33.fileliftcore.strategy.FileNameStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 根据当前时间生成一个基于时间的文件名，精确到微秒
 * 例如当前时间是：2023年6月28日 13:55:23.123，则文件名为：2023628135523123
 */
public class TimeBasedFileNameStrategy implements FileNameStrategy {

	@Override
	public String generateFileName() {
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		return formatter.format(time);
	}

}