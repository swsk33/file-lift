package io.github.swsk33.fileliftcore.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 文件抽象类
 */
@Data
@JsonIgnoreProperties("fileStream")
public abstract class UploadFile {

	/**
	 * 文件名
	 */
	private String name;

	/**
	 * 文件扩展名（不带.）
	 */
	private String format;

	/**
	 * 文件流对象，用于获取该文件的内容（不会序列化）
	 */
	private InputStream fileStream;

	/**
	 * 获取文件的二进制字节内容
	 *
	 * @return 文件的内容，为二进制字节形式
	 */
	public byte[] getContentByte() throws IOException {
		return fileStream.readAllBytes();
	}

	/**
	 * 获取文件的内容，为字符串形式，编码为UTF-8
	 *
	 * @return 字符串形式的文件内容
	 */
	public String getContentText() throws IOException {
		return getContentText(StandardCharsets.UTF_8);
	}

	/**
	 * 获取文件的内容，为字符串形式
	 *
	 * @param charset 指定字符串的编码
	 * @return 字符串形式的文件内容
	 */
	public String getContentText(Charset charset) throws IOException {
		return new String(getContentByte(), charset);
	}

}