package io.github.swsk33.fileliftspringboottest.api;

import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 测试各种文件操作的API
 */
@RestController
@RequestMapping("/api/file")
public class UploadTestAPI {

	/**
	 * 自动装配文件上传服务
	 */
	@Autowired
	private UploadFileService uploadFileService;

	/**
	 * 上传文件接口
	 *
	 * @param file 接收的文件对象（MultipartFile表单形式）
	 * @return 上传后的文件信息结果
	 */
	@PostMapping("/upload")
	public FileResult<UploadFile> upload(@RequestParam("file") MultipartFile file) {
		return uploadFileService.upload(file);
	}

	/**
	 * 删除一个已上传的文件的接口
	 *
	 * @param filename 要删除的文件名
	 * @return 执行结果
	 */
	@DeleteMapping("/delete/{filename}")
	public FileResult<Void> delete(@PathVariable String filename) {
		return uploadFileService.delete(filename);
	}

	/**
	 * 根据文件名，查找对应文件并获取文件信息
	 *
	 * @param filename 文件名，不带扩展名
	 * @return 对应文件信息结果
	 */
	@GetMapping("/find/{filename}")
	public FileResult<UploadFile> findByName(@PathVariable String filename) {
		return uploadFileService.findByMainName(filename);
	}

	/**
	 * 通过完整文件名直接获取文件
	 *
	 * @param fullName 完整文件名，需要包含扩展名
	 * @return 对应文件信息结果
	 */
	@GetMapping("/get/{fullName}")
	public FileResult<UploadFile> getByFullName(@PathVariable String fullName) {
		return uploadFileService.findByFullName(fullName);
	}

	/**
	 * 根据文件名，下载对应文件
	 *
	 * @param filename 文件名，不带扩展名
	 * @return 下载的文件
	 */
	@GetMapping("/download/{filename}")
	public ResponseEntity<byte[]> downloadByName(@PathVariable String filename) {
		// 包含文件二进制信息的结果
		FileResult<BinaryContent> result = uploadFileService.downloadFileByMainName(filename);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		// 返回响应
		return ResponseEntity.ok()
				// 获取结果中文件的content-type信息并设定
				.contentType(MediaType.parseMediaType(result.getData().getContentType()))
				// 放入文件内容至响应体以下载
				.body(result.getData().getByteAndClose());
	}

	/**
	 * 通过完整文件名直接获取文件并下载
	 *
	 * @param fullName 完整文件名，需要包含扩展名
	 * @return 下载的文件
	 */
	@GetMapping("/download-full/{fullName}")
	public ResponseEntity<byte[]> downloadByFullName(@PathVariable String fullName) {
		// 包含文件二进制信息的结果
		FileResult<BinaryContent> result = uploadFileService.downloadFileByFullName(fullName);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		// 返回响应
		return ResponseEntity.ok()
				// 获取结果中文件的content-type信息并设定
				.contentType(MediaType.parseMediaType(result.getData().getContentType()))
				// 放入文件内容至响应体以下载
				.body(result.getData().getByteAndClose());
	}

}