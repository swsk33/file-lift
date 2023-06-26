package io.github.swsk33.fileliftcore.param;

/**
 * 文件存储方式
 */
public class FileStorage {

	/**
	 * 使用文件系统存储文件
	 */
	public static final String FILE = "filesystem";

	/**
	 * 使用MongoDB GridFS方式存储文件
	 */
	public static final String MONGO = "mongodb";

}