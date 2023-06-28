package io.github.swsk33.fileliftcore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 存放文件的二进制内容相关信息的对象
 * 其中包含了文件的Content-Type以及文件流（文件内容）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent {

	/**
	 * 文件的Content-Type
	 */
	private String contentType;

	/**
	 * 文件流对象（存放文件内容）
	 */
	private InputStream fileStream;

	/**
	 * 以字节码形式获取该文件的内容，可以返回给前端以下载文件
	 *
	 * @return 文件内容二进制字节内容，获取失败返回null
	 */
	public byte[] getContentByte() {
		byte[] content = null;
		try {
			content = fileStream.readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 以文本形式，返回文件内容
	 *
	 * @param charset 文本的编码
	 * @return 文件文本内容
	 */
	public String getContentText(Charset charset) {
		return new String(getContentByte(), charset);
	}

	/**
	 * 以文本形式，返回文件内容，为UTF-8编码
	 *
	 * @return 文件文本内容
	 */
	public String getContentText() {
		return getContentText(StandardCharsets.UTF_8);
	}

}