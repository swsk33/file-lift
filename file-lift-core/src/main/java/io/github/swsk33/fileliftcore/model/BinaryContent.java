package io.github.swsk33.fileliftcore.model;

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
public class BinaryContent implements AutoCloseable {

	/**
	 * 文件的Content-Type
	 */
	private String contentType;

	/**
	 * 文件流对象（存放文件内容）
	 */
	private InputStream fileStream;

	/**
	 * 关闭该二进制内容对象中的文件流以释放资源
	 */
	@Override
	public void close() throws Exception {
		if (fileStream != null) {
			fileStream.close();
		}
	}

	/**
	 * 以字节码形式获取该文件的内容，可以返回给前端以下载文件
	 * 注意，该方法读取完成内容后不会关闭，可以再次读取
	 *
	 * @return 文件内容二进制字节内容，获取失败返回null
	 */
	public byte[] getByte() {
		byte[] content = null;
		try {
			content = fileStream.readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 以字节码形式获取该文件的内容，并且在读取完成后关闭该资源
	 *
	 * @return 文件内容二进制字节内容，获取失败返回null
	 */
	public byte[] getByteAndClose() {
		byte[] content = null;
		try (this) {
			content = getByte();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

}