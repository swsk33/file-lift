package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.http.HttpUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件系统的文件类型，存放文件信息
 */
@Data
public class LocalFile extends UploadFile {

	/**
	 * 当前路径
	 */
	private volatile static Path currentPath;

	/**
	 * 获取当前路径对象，为绝对路径形式
	 *
	 * @return 路径对象
	 */
	private static Path getCurrentPath() {
		// 双检锁延迟初始化
		if (currentPath == null) {
			synchronized (LocalFile.class) {
				if (currentPath == null) {
					currentPath = Paths.get("").toAbsolutePath();
				}
			}
		}
		return currentPath;
	}

	/**
	 * 文件的相对路径
	 */
	private String relativePath;

	/**
	 * 文件绝对路径
	 */
	private String absolutePath;

	/**
	 * 通过一个文件绝对路径，创建一个本地文件对象
	 *
	 * @param absolutePath 文件绝对路径
	 * @return 本地文件对象，文件不存在返回null
	 */
	public static LocalFile createLocalFile(String absolutePath) {
		if (!FileUtil.exist(absolutePath)) {
			return null;
		}
		LocalFile newFile = new LocalFile();
		newFile.setName(FileNameUtil.mainName(absolutePath));
		newFile.setFormat(FileNameUtil.extName(absolutePath));
		newFile.setLength(FileUtil.file(absolutePath).length());
		newFile.setAbsolutePath(absolutePath);
		newFile.setRelativePath(getCurrentPath().relativize(Paths.get(absolutePath)).toString());
		return newFile;
	}

	@Override
	public BinaryContent toBinaryContent() {
		BinaryContent content = new BinaryContent();
		content.setContentType(HttpUtil.getMimeType(absolutePath, "application/octet-stream"));
		content.setFileStream(FileUtil.getInputStream(absolutePath));
		return content;
	}

}