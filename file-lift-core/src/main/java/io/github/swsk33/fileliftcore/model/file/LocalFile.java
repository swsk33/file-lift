package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件系统的文件类型
 */
@Data
public class LocalFile extends UploadFile {

	/**
	 * 当前路径
	 */
	private static Path currentPath;

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
	 * @param readFile     是否读取文件内容为文件流
	 * @return 本地文件对象，文件不存在返回null
	 */
	public static LocalFile createLocalFile(String absolutePath, boolean readFile) {
		// 第一次调用时初始化当前路径值
		if (currentPath == null) {
			currentPath = Paths.get("");
		}
		if (!FileUtil.exist(absolutePath)) {
			return null;
		}
		LocalFile newFile = new LocalFile();
		newFile.setName(FileNameUtil.mainName(absolutePath));
		newFile.setFormat(FileNameUtil.extName(absolutePath));
		newFile.setAbsolutePath(absolutePath);
		newFile.setRelativePath(currentPath.relativize(Paths.get(absolutePath)).toString());
		if (readFile) {
			newFile.setFileStream(FileUtil.getInputStream(absolutePath));
		}
		return newFile;
	}

}