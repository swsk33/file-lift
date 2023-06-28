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
public class FileSystemTestAPI {

	@Autowired
	private UploadFileService uploadFileService;

	@PostMapping("/upload")
	public FileResult<UploadFile> upload(@RequestParam("file") MultipartFile file) {
		return uploadFileService.upload(file);
	}

	@DeleteMapping("/delete/{filename}")
	public FileResult<Void> delete(@PathVariable String filename) {
		return uploadFileService.delete(filename);
	}

	@GetMapping("/find/{filename}")
	public FileResult<UploadFile> findByName(@PathVariable String filename) {
		return uploadFileService.findByMainName(filename);
	}

	@GetMapping("/get/{fullName}")
	public FileResult<UploadFile> getByFullName(@PathVariable String fullName) {
		return uploadFileService.findByFullName(fullName);
	}

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
				.body(result.getData().getContentByte());
	}

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
				.body(result.getData().getContentByte());
	}

}