package io.github.swsk33.fileliftcore.strategy.context;

import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.FileSystemProcessStrategy;
import io.github.swsk33.fileliftcore.strategy.impl.MongoDBFileProcessStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于文件增删改查策略的上下文
 */
public class FileProcessStrategyContext {

	/**
	 * 存放所有文件增删改查策略的容器
	 */
	private static final Map<String, FileProcessStrategy> FILE_PROCESS_STRATEGY_MAP = new HashMap<>();

	// 初始化策略
	static {
		FILE_PROCESS_STRATEGY_MAP.put(FileStorageMethods.FILE, new FileSystemProcessStrategy());
		FILE_PROCESS_STRATEGY_MAP.put(FileStorageMethods.MONGO, new MongoDBFileProcessStrategy());
	}

	/**
	 * 获取策略对象
	 *
	 * @param storageMethod 文件储存方式
	 * @return 策略对象，如果文件储存方式不存在，则使用默认
	 */
	private static FileProcessStrategy getStrategy(String storageMethod) {
		if (!FILE_PROCESS_STRATEGY_MAP.containsKey(storageMethod)) {
			return FILE_PROCESS_STRATEGY_MAP.get(FileStorageMethods.FILE);
		}
		return FILE_PROCESS_STRATEGY_MAP.get(storageMethod);
	}

	/**
	 * 保存文件
	 *
	 * @param storageMethod 文件储存方式
	 * @param file          上传的文件
	 * @param saveName      保存的文件名（不带扩展名）
	 * @return 保存的文件对象，其中不包含文件内容信息（文件流为空），若保存失败则返回null
	 */
	public static UploadFile saveFile(String storageMethod, MultipartFile file, String saveName) {
		return getStrategy(storageMethod).saveFile(file, saveName);
	}

	/**
	 * 删除文件
	 *
	 * @param storageMethod 文件储存方式
	 * @param filename      要删除的文件名
	 */
	public static void deleteFile(String storageMethod, String filename) {
		getStrategy(storageMethod).deleteFile(filename);
	}

	/**
	 * 重命名文件
	 *
	 * @param storageMethod 文件储存方式
	 * @param originName    原始文件名（不带扩展名）
	 * @param newName       新的文件名（不带扩展名）
	 */
	public static void renameFile(String storageMethod, String originName, String newName) {
		getStrategy(storageMethod).renameFile(originName, newName);
	}

	/**
	 * 根据文件名查找文件，不使用扩展名
	 *
	 * @param storageMethod 文件储存方式
	 * @param filename      文件名（不带扩展名）
	 * @return 文件查找结果，不存在返回null
	 */
	public static UploadFile findFileByMainName(String storageMethod, String filename) {
		return getStrategy(storageMethod).findFileByMainName(filename);
	}

	/**
	 * 根据完整文件名直接获取文件
	 *
	 * @param storageMethod 文件储存方式
	 * @param fullName      文件名（需要包含扩展名）
	 * @return 文件查找结果，不存在返回null
	 */
	public static UploadFile findFileByFullName(String storageMethod, String fullName) {
		return getStrategy(storageMethod).findFileByFullName(fullName);
	}

}