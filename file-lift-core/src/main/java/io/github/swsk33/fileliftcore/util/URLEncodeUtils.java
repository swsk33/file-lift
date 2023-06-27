package io.github.swsk33.fileliftcore.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL编码的实用类
 */
public class URLEncodeUtils {

	/**
	 * 百分号编码
	 *
	 * @param origin 原始字符串
	 * @return 编码后字符串
	 */
	public static String percentEncode(String origin) {
		return URLEncoder.encode(origin, StandardCharsets.UTF_8).replace("+", "%20");
	}

}