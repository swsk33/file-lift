package io.github.swsk33.fileliftcore.strategy;

import io.github.swsk33.fileliftcore.model.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件的增删改查策略
 */
public interface FileProcessStrategy {

	/**
	 * 保存文件
	 *
	 * @param file     文件对象
	 * @param saveName 保存的文件名（不带扩展名）
	 * @return 保存的文件对象，若保存失败则返回null
	 */
	UploadFile saveFile(MultipartFile file, String saveName);

	/**
	 * 删除文件
	 *
	 * @param filename 要删除的文件名（不带扩展名）
	 */
	void deleteFile(String filename);

	/**
	 * 根据文件名查找文件，不使用扩展名
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 文件查找结果，不存在返回null
	 */
	UploadFile findFileByMainName(String filename);

	/**
	 * 根据完整文件名直接获取文件
	 * 该方法没有查找过程，效率较高，但是要使用文件完整名称
	 *
	 * @param fullName 完整文件名（需要包含扩展名）
	 * @return 文件查找结果，包含文件内容信息，不存在返回null
	 */
	UploadFile findFileByFullName(String fullName);

	/**
	 * 传入一个完整文件名，判断文件是否存在
	 *
	 * @param fullName 完整文件名（需要包含扩展名）
	 * @return 该文件是否存在
	 */
	boolean fileExists(String fullName);

	/**
	 * 根据文件名下载文件
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 文件的输入流，文件不存在返回null
	 */
	InputStream downloadFileByMainName(String filename);

	/**
	 * 根据完整的文件名下载文件
	 *
	 * @param fullName 完整文件名（需要包含扩展名）
	 * @return 文件的输入流，文件不存在返回null
	 */
	InputStream downloadFileByFullName(String fullName);

}