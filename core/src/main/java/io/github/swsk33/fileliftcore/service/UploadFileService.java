package io.github.swsk33.fileliftcore.service;

import io.github.swsk33.fileliftcore.model.FileResult;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于对上传的文件进行操作的类，不限于接收上传的文件、获取文件等等
 * 下列所有文件名参数，不需要带扩展名
 */
public interface UploadFileService {

	/**
	 * 上传文件并保存
	 *
	 * @param uploadFile 上传的文件对象
	 * @return 文件结果
	 */
	FileResult<Void> upload(MultipartFile uploadFile);

	/**
	 * 删除已上传文件
	 *
	 * @param filename 要删除的文件名
	 * @return 文件结果
	 */
	FileResult<Void> delete(String filename);

	/**
	 * 对一个已上传的文件重命名
	 *
	 * @param originName 待重命名的文件名
	 * @param newName    新的文件名
	 * @return 文件结果
	 */
	FileResult<Void> rename(String originName, String newName);

	/**
	 * 查找文件并获取
	 *
	 * @param filename 文件名
	 * @return 文件结果
	 */
	FileResult<UploadFile> find(String filename);

}