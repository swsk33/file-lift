package io.github.swsk33.fileliftcore.strategy.context;

import io.github.swsk33.fileliftcore.model.BinaryContent;
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
 * 该上下文采用延迟初始化策略的方式
 */
public class FileProcessStrategyContext {

	/**
	 * 存放所有文件增删改查策略的容器
	 */
	private static final Map<String, FileProcessStrategy> FILE_PROCESS_STRATEGY_MAP = new HashMap<>();

	/**
	 * 存放文件储存方式名（常量）对应的文件储存策略类的容器
	 * 用于在延迟初始化的时候，通过文件储存方式取出对应的策略类，并利用反射实例化然后放入策略容器
	 */
	private static final Map<String, Class<?>> FILE_STORAGE_METHOD_CLASS_MAP = new HashMap<>();

	// 初始化储存方式常量对应的策略类容器
	static {
		FILE_STORAGE_METHOD_CLASS_MAP.put(FileStorageMethods.FILE, FileSystemProcessStrategy.class);
		FILE_STORAGE_METHOD_CLASS_MAP.put(FileStorageMethods.MONGO, MongoDBFileProcessStrategy.class);
	}

	/**
	 * 获取策略对象
	 *
	 * @param storageMethod 文件储存方式
	 * @return 策略对象，如果文件储存方式不存在，则使用默认
	 */
	private static FileProcessStrategy getStrategy(String storageMethod) {
		// 如果该文件储存方式位于储存方式常量列表中，但是不在策略容器中，说明还未进行初始化
		if (FileStorageMethods.contains(storageMethod) && !FILE_PROCESS_STRATEGY_MAP.containsKey(storageMethod)) {
			try {
				FILE_PROCESS_STRATEGY_MAP.put(storageMethod, (FileProcessStrategy) FILE_STORAGE_METHOD_CLASS_MAP.get(storageMethod).getConstructor().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 否则，就是传入了不存在的文件储存方式，按照默认值执行
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

	/**
	 * 根据完整文件名判断文件是否存在
	 *
	 * @param storageMethod 文件储存方式
	 * @param fullName      文件名（需要包含扩展名）
	 * @return 该文件是否存在
	 */
	public static boolean fileExists(String storageMethod, String fullName) {
		return getStrategy(storageMethod).fileExists(fullName);
	}

	/**
	 * 根据文件名下载文件，不使用扩展名
	 *
	 * @param storageMethod 文件储存方式
	 * @param filename      文件名（不带扩展名）
	 * @return 文件的二进制内容信息对象，文件不存在返回null
	 */
	public static BinaryContent downloadFileByMainName(String storageMethod, String filename) {
		return getStrategy(storageMethod).downloadFileByMainName(filename);
	}

	/**
	 * 根据完整文件名直接下载文件
	 *
	 * @param storageMethod 文件储存方式
	 * @param fullName      文件名（需要包含扩展名）
	 * @return 文件的二进制内容信息对象，文件不存在返回null
	 */
	public static BinaryContent downloadFileByFullName(String storageMethod, String fullName) {
		return getStrategy(storageMethod).downloadFileByFullName(fullName);
	}

}