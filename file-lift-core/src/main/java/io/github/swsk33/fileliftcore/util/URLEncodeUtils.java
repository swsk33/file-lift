package io.github.swsk33.fileliftcore.util;

import java.net.URLEncoder;

/**
 * URL编码的实用类
 */
public class URLEncodeUtils {

	/**
	 * 百分号编码
	 *
	 * @param origin 原始字符串
	 * @return 编码后字符串，编码失败返回null
	 */
	public static String percentEncode(String origin) {
		String result = null;
		try {
			result = URLEncoder.encode(origin, "UTF-8").replace("+", "%20");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}