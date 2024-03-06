# FileLift - 文件上传管理框架

<p align="center">
	<a target="_blank" href="https://central.sonatype.com/search?smo=true&q=io.github.swsk33.file-lift">
		<img src="https://img.shields.io/maven-central/v/io.github.swsk33/file-lift-parent
" />
	</a>
	<a target="_blank" href="https://www.gnu.org/licenses/old-licenses/gpl-2.0.html">
		<img alt="GitHub" src="https://img.shields.io/github/license/swsk33/code-post">
	</a>
	<a target="_blank" href="https://www.azul.com/downloads/#downloads-table-zulu">
		<img alt="Static Badge" src="https://img.shields.io/badge/8%2B-blue?label=JDK">
	</a>
</p>


## 1，介绍
FileLift是一款简单的Java文件上传管理框架，其中封装了常用的关于文件上传、下载以及上传的文件管理的接口，并且支持多种上传文件的储存方式，开箱即用。

### (1) 项目背景

在之前开发的许多后端项目中，无论是SSM单体架构，还是分布式微服务，都多多少少会涉及到小文件上传的功能，那么对于文件上传，除了处理文件转存、管理已上传文件等功能之外，还可能需要对上传的文件进行格式校验等等。每开发一个项目，就去上一个项目复制文件上传服务的代码，也非常的不优雅。

因此，该项目将文件上传的一些逻辑抽离了出来，并封装了一些常用的处理文件上传和文件管理的接口，作为一个外部库。只需引入这个外部库，并加以简单的配置，你就可以以最简单的方式，完成文件上传的操作了！

### (2) 功能介绍

该框架主要是用于简化传统Spring MVC或者Spring Boot项目的文件上传逻辑，目前仅仅支持的是**小文件的上传和管理**，不建议用于大文件上传。

其主要功能如下：

- 提供了一些关于文件业务的接口，包括：
	- 文件上传
	- 删除已上传文件
	- 已上传文件信息获取
	- 下载文件
- 支持多种储存已上传文件的方式，包括：
	- 本地文件系统
	- MongoDB GridFS（需要额外引入`mongodb-driver-sync`或者`spring-boot-starter-data-mongodb`依赖）
	- MinIO对象储存（需要额外引入`minio`依赖）

## 2，快速开始

### (1) 环境要求

使用该框架需要满足以下要求：

- JDK 8及其以上版本
- Spring 5.x或者Spring Boot 2.x及其以上版本，建议使用Spring Boot 2.7.x及其以上版本

### (2) 在Spring Boot环境集成

首先引入下列依赖：

```xml
<dependency>
	<groupId>io.github.swsk33</groupId>
	<artifactId>file-lift-spring-boot-starter</artifactId>
	<version>1.2.0</version>
</dependency>
```

然后在Spring Boot配置文件`application.yml`中，加入下列配置：

```yaml
# 服务端配置
spring:
  # 上传文件最大大小
  servlet:
    multipart:
      max-file-size: 12MB
      max-request-size: 12MB
  # JSON反序列化设置
  jackson:
    deserialization:
      fail-on-unknown-properties: false
    default-property-inclusion: non_null
server:
  port: 8801
  tomcat:
    max-http-form-post-size: 12MB

####### 以下是FileLift相关配置 #######
io:
  github:
    swsk33:
      file-lift:
        core:
          # 使用本地文件系统储存上传的文件
          storage-method: "filesystem"
          # 上传文件最大限制为10240KB，即10MB
          # 不配置该选项表示不对文件大小做限制
          size-limit: 10240
          # 不允许同名文件上传覆盖
          override: false
          # 开启文件上传后自动命名功能
          auto-rename: true
          # 使用时间字符串对文件自动命名
          auto-rename-format: "time"
          # 允许上传的文件格式，这里表示允许jpg、png和无扩展名的文件上传
          # 不配置该选项表示允许任何格式文件上传
          allowed-formats:
            - "jpg"
            - "png"
            - "?"
        # 关于文件系统储存方案配置
        filesystem:
          # 表示上传的文件保存到程序运行目录下的avatar文件夹中（不存在会自动创建）
          save-folder: "avatar"
```

这里使用的是`YAML`格式的配置，使用`properties`同理。

### (3) 编写API

现在，写一个`RestController`调用文件上传服务即可：

```java
package io.github.swsk33.fileliftspringboot3test.api;

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
		// 调用服务对象的upload方法即可完成上传文件，返回这个文件的相关信息
		return uploadFileService.upload(file);
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
	public ResponseEntity<byte[]> downloadByFullName(@PathVariable("fullName") String fullName) {
		// 调用服务对象的downloadFileByFullName可以下载文件，返回包含文件内容二进制流以及一些其它元数据
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
```

运行项目，访问这些接口，即可成功地完成文件上传、管理或者下载功能！

需要注意的是，上传文件的请求需要以`MultipartFile`的形式，前端可以使用`form-data`表单。

### (4) 访问API

首选，上传一个文件，向上述接口`http://127.0.0.1:8801/api/file/upload`发送POST请求，请求体为`form-data`形式，得到响应结果如下：

```json
{
	"success": true,
	"message": "上传文件完成！",
	"data": {
		"name": "20230629184020066",
		"format": "jpg",
		"relativePath": "avatar/20230629184020066.jpg",
		"absolutePath": "/home/swsk33/app/file-lift-spring-boot-test/avatar/20230629184020066.jpg"
	}
}
```

然后，通过文件名查找文件信息，访问上述的接口地址`http://127.0.0.1:8801/api/file/find/20230629184020066`，你可以获取到这个文件的信息，返回形式和上传文件时返回的一样。

然后下载我们上传的图片，访问`http://127.0.0.1:8801/api/file/download/20230629184020066`，可以得到图片：

![image-20230629190121107](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230629190121107.png)

通过这个简单的例子，就完成了文件的上传、下载操作了！大家可以访问其它接口尝试别的操作。

可见，只需一行代码，就可以完成文件上传的功能了！这背后包括了文件的上传、各种校验、文件保存等等操作。

## 3，文档

在此，我们通过FileLift实现了一个简单的文件上传和下载功能了！更多关于该框架的文档请参考：

- 详细文档：[传送门](./docs/主要文档.md)
- 配置参考：[传送门](./docs/配置选项.md)
- API文档：[传送门](https://apidoc.gitee.com/swsk33/file-lift/)