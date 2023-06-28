package io.github.swsk33.fileliftcore.service;

import io.github.swsk33.fileliftcore.model.FileResult;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 用于对上传的文件进行操作的类，不限于接收上传的文件、获取文件等等
 */
public interface UploadFileService {

	/**
	 * 上传文件并保存
	 *
	 * @param uploadFile 上传的文件对象
	 * @return 文件结果，包含上传的文件信息
	 */
	FileResult<UploadFile> upload(MultipartFile uploadFile);

	/**
	 * 删除已上传文件
	 *
	 * @param filename 要删除的文件名
	 * @return 文件结果
	 */
	FileResult<Void> delete(String filename);

	/**
	 * 查找文件并获取，不使用扩展名
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 文件结果，包含对应的文件信息
	 */
	FileResult<UploadFile> findByMainName(String filename);

	/**
	 * 根据文件的完整名称直接获取文件
	 * 在已知文件名及其扩展名的情况下推荐使用本方法直接获取到文件，在文件系统储存时使用该方法可以避免查找文件的性能消耗
	 *
	 * @param fullName 文件全名（需要包含扩展名）
	 * @return 文件结果，包含对应的文件信息
	 */
	FileResult<UploadFile> findByFullName(String fullName);

	/**
	 * 根据文件名查找并下载对应文件，得到文件流
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 文件结果
	 */
	FileResult<InputStream> downloadFileByMainName(String filename);

	/**
	 * 根据文件的完整名称直接获取对应文件并下载，得到文件流
	 * 在已知文件名及其扩展名的情况下推荐使用本方法直接获取到文件，在文件系统储存时使用该方法可以避免查找文件的性能消耗
	 *
	 * @param fullName 文件全名（需要包含扩展名）
	 * @return 文件结果
	 */
	FileResult<InputStream> downloadFileByFullName(String fullName);

}