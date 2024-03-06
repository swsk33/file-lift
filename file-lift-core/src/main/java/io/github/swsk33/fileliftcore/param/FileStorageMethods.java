package io.github.swsk33.fileliftcore.param;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 文件存储方式的常量
 */
public class FileStorageMethods {

	/**
	 * 使用文件系统存储文件
	 */
	public static final String FILE = "filesystem";

	/**
	 * 使用MongoDB GridFS方式存储文件
	 */
	public static final String MONGO = "mongodb";

	/**
	 * 使用MinIO存储文件
	 */
	public static final String MINIO = "minio";

	/**
	 * 传入一个文件储存方式名，利用反射的方式检查这个储存方式是否属于上述所有的常量中的某一个
	 *
	 * @param storageMethod 文件储存方式名
	 * @return 是否为上述常量中的某一个
	 */
	public static boolean contains(String storageMethod) {
		Field[] fields = FileStorageMethods.class.getDeclaredFields();
		// 遍历所有的字段
		for (Field field : fields) {
			// 如果说不是public static final的字段，直接跳过
			int modifiers = field.getModifiers();
			if (!Modifier.isPublic(modifiers) || !Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
				continue;
			}
			// 比对字段值
			try {
				if (storageMethod.equals(field.get(null))) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}