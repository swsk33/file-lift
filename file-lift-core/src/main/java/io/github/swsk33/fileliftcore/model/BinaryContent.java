package io.github.swsk33.fileliftcore.model;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * 文件的二进制内容信息对象
 * 这个类的对象中，包含了文件的Content-Type以及文件流（文件内容）
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
	 * 以字节码形式获取该文件的内容，并且在读取完成后关闭该资源
	 *
	 * @return 文件内容二进制字节内容，获取失败返回null
	 */
	public byte[] getByteAndClose() {
		return IoUtil.readBytes(fileStream);
	}

}