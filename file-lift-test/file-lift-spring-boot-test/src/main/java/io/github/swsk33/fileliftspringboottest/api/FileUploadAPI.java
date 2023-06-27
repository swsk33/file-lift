package io.github.swsk33.fileliftspringboottest.api;

import io.github.swsk33.fileliftcore.model.FileResult;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 测试各种文件操作的API
 */
@RestController
@RequestMapping("/api/file")
public class FileUploadAPI {

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

	@PutMapping("/rename/origin/{origin}/new/{newName}")
	public FileResult<Void> rename(@PathVariable String origin, @PathVariable String newName) {
		return uploadFileService.rename(origin, newName);
	}

	@GetMapping("/get/{filename}")
	public FileResult<UploadFile> findByName(@PathVariable String filename) {
		return uploadFileService.findByMainName(filename);
	}

	@GetMapping("/get/{fullName}")
	public FileResult<UploadFile> getByFullName(@PathVariable String fullName) {
		return uploadFileService.findByFullName(fullName);
	}

}