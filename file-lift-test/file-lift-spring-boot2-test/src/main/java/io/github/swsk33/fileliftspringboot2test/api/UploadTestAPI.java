package io.github.swsk33.fileliftspringboot2test.api;

import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
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
		// 调用服务对象的upload方法即可完成上传文件，返回这个文件的相关信息
		return uploadFileService.upload(file);
	}

	/**
	 * 上传文件，并指定上传后的文件名称<br>
	 * 该方法无视配置autoRename，但是仍然会受到override配置影响
	 *
	 * @param file 接收的文件对象（MultipartFile表单形式）
	 * @param name 上传后的文件名，不包括扩展名
	 * @return 上传后文件信息结果
	 */
	@PostMapping("/upload-force-name/{name}")
	public FileResult<UploadFile> uploadForceName(@RequestParam("file") MultipartFile file, @PathVariable("name") String name) {
		return uploadFileService.uploadForceName(file, name);
	}

	/**
	 * 删除一个已上传的文件的接口
	 *
	 * @param filename 要删除的文件名
	 * @return 执行结果
	 */
	@DeleteMapping("/delete/{filename}")
	public FileResult<Void> delete(@PathVariable("filename") String filename) {
		// 调用服务对象的delete方法即可删除文件，返回删除结果
		return uploadFileService.delete(filename);
	}

	/**
	 * 根据文件名，查找对应文件并获取文件信息
	 *
	 * @param filename 文件名，不带扩展名
	 * @return 对应文件信息结果
	 */
	@GetMapping("/find/{filename}")
	public FileResult<UploadFile> findByMainName(@PathVariable("filename") String filename) {
		// 调用服务对象的findByMainName方法即可查找一个已上传的文件信息，返回这个文件的相关信息
		return uploadFileService.findByMainName(filename);
	}

	/**
	 * 通过完整文件名直接获取文件
	 *
	 * @param fullName 完整文件名，需要包含扩展名
	 * @return 对应文件信息结果
	 */
	@GetMapping("/get/{fullName}")
	public FileResult<UploadFile> getByFullName(@PathVariable("fullName") String fullName) {
		// 调用服务对象的findByFullName即可查找一个已上传的文件信息，返回这个文件的相关信息
		// 与findByMainName不同的是，findByMainName方法不需要文件扩展名即可查找文件，而该方法需要
		// 其它方法类似
		return uploadFileService.findByFullName(fullName);
	}

	/**
	 * 根据文件名，下载对应文件
	 *
	 * @param filename 文件名，不带扩展名
	 * @return 下载的文件
	 */
	@GetMapping("/download/{filename}")
	public ResponseEntity<byte[]> downloadByName(@PathVariable("filename") String filename) {
		// 调用服务对象的downloadFileByMainName可以下载文件，返回包含文件内容二进制流以及一些其它元数据
		FileResult<BinaryContent> result = uploadFileService.downloadFileByMainName(filename);
		// 若下载文件失败则返回404
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		// 获取二进制结果中的文件流信息
		BinaryContent content = result.getData();
		// 准备响应头
		HttpHeaders headers = new HttpHeaders();
		// 设定MediaType类型（从文件结果中获取并设定）至响应头
		headers.setContentType(MediaType.parseMediaType(content.getContentType()));
		// 设定Content-Disposition头，告诉浏览器下载的文件名称
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
		// 最终返回响应对象
		return ResponseEntity.ok().headers(headers).body(content.getByteAndClose());
	}

	/**
	 * 通过完整文件名直接获取文件并下载
	 *
	 * @param fullName 完整文件名，需要包含扩展名
	 * @return 下载的文件
	 */
	@GetMapping("/download-full/{fullName}")
	public ResponseEntity<byte[]> downloadByFullName(@PathVariable("fullName") String fullName) {
		// 调用服务对象的downloadFileByFullName可以下载文件，返回包含文件内容二进制流以及一些其它元数据
		FileResult<BinaryContent> result = uploadFileService.downloadFileByFullName(fullName);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		// 获取二进制结果中的文件流信息
		BinaryContent content = result.getData();
		// 准备响应头
		HttpHeaders headers = new HttpHeaders();
		// 设定MediaType类型（从文件结果中获取并设定）至响应头
		headers.setContentType(MediaType.parseMediaType(content.getContentType()));
		// 设定Content-Disposition头，告诉浏览器下载的文件名称
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fullName).build());
		// 最终返回响应对象
		return ResponseEntity.ok().headers(headers).body(content.getByteAndClose());
	}

}