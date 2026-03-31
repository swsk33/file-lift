package io.github.swsk33.fileliftcore.util;

import cn.hutool.core.util.StrUtil;

/**
 * 文件名实用类
 */
public class FileNameUtils {

	/**
	 * 构建完整文件名
	 *
	 * @param mainName   文件名
	 * @param formatName 文件名扩展
	 * @return 完整文件名
	 */
	public static String buildFullName(String mainName, String formatName) {
		if (StrUtil.isEmpty(formatName)) {
			return mainName;
		}
		return mainName + "." + formatName;
	}

}