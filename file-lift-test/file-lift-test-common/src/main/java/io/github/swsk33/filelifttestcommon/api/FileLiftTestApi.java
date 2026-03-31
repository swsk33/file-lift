package io.github.swsk33.filelifttestcommon.api;

import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 测试各种文件操作的 API
 */
@RestController
@RequestMapping("/api/file")
public class FileLiftTestApi {

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
	 * 上传文件，并指定上传后的文件名称
	 *
	 * @param file 接收的文件对象（MultipartFile表单形式）
	 * @param name 上传后的文件名，不包括扩展名
	 * @return 上传后文件信息结果
	 */
	@PostMapping("/upload-force-name/{name}")
	public FileResult<UploadFile> uploadForceName(@RequestParam("file") MultipartFile file, @PathVariable String name) {
		return uploadFileService.uploadForceName(file, name);
	}

	/**
	 * 删除一个已上传的文件
	 *
	 * @param filename 要删除的文件名
	 * @return 执行结果
	 */
	@DeleteMapping("/delete/{filename}")
	public FileResult<Void> delete(@PathVariable String filename) {
		return uploadFileService.delete(filename);
	}

	/**
	 * 根据文件名查找对应文件
	 *
	 * @param filename 文件名，不带扩展名
	 * @return 对应文件信息结果
	 */
	@GetMapping("/find/{filename}")
	public FileResult<UploadFile> findByMainName(@PathVariable String filename) {
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
	 * 根据文件名下载对应文件
	 *
	 * @param filename 文件名，不带扩展名
	 * @return 下载的文件
	 */
	@GetMapping("/download/{filename}")
	public ResponseEntity<byte[]> downloadByName(@PathVariable String filename) {
		FileResult<BinaryContent> result = uploadFileService.downloadFileByMainName(filename);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		BinaryContent content = result.getData();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(content.getContentType()));
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
		return ResponseEntity.ok().headers(headers).body(content.getByteAndClose());
	}

	/**
	 * 通过完整文件名直接获取文件并下载
	 *
	 * @param fullName 完整文件名，需要包含扩展名
	 * @return 下载的文件
	 */
	@GetMapping("/download-full/{fullName}")
	public ResponseEntity<byte[]> downloadByFullName(@PathVariable String fullName) {
		FileResult<BinaryContent> result = uploadFileService.downloadFileByFullName(fullName);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		BinaryContent content = result.getData();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(content.getContentType()));
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fullName).build());
		return ResponseEntity.ok().headers(headers).body(content.getByteAndClose());
	}

}