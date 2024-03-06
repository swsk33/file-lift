package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.file.FileNameUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.FileLiftCoreConfig;
import io.github.swsk33.fileliftcore.model.file.MinioFile;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.util.MinioUtils;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 基于MinIO的文件保存策略
 */
public class MinioFileProcessStrategy implements FileProcessStrategy {

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		// 如果关闭了自动重命名且开启了允许覆盖，则先查找数据库是否存在对应文件名，若存在则先删除
		if (!FileLiftCoreConfig.getInstance().isAutoRename() && FileLiftCoreConfig.getInstance().isOverride()) {
			if (MinioUtils.fileExists(saveName)) {
				MinioUtils.deleteFile(saveName);
			}
		}
		// 执行上传操作
		ObjectWriteResponse uploadResult;
		try {
			uploadResult = MinioUtils.uploadFile(file.getInputStream(), saveName, formatName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 返回文件信息
		MinioFile fileInfo = new MinioFile();
		fileInfo.setName(saveName);
		fileInfo.setFormat(formatName);
		fileInfo.setLength(file.getSize());
		fileInfo.setObjectName(uploadResult.object());
		fileInfo.setEtag(uploadResult.etag());
		return fileInfo;
	}

	@Override
	public void deleteFile(String filename) {
		if (!MinioUtils.fileExists(filename)) {
			return;
		}
		MinioUtils.deleteFile(filename);
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		// 查询文件信息和标签
		StatObjectResponse findResult = MinioUtils.findFileByMainName(filename);
		Map<String, String> tagMap = MinioUtils.getFileTags(filename);
		if (findResult == null || tagMap == null) {
			return null;
		}
		// 组装文件信息
		MinioFile file = new MinioFile();
		file.setName(filename);
		file.setFormat(tagMap.get("type"));
		file.setLength(findResult.size());
		file.setObjectName(findResult.object());
		file.setEtag(findResult.etag());
		return file;
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		String mainName = FileNameUtil.mainName(fullName);
		String extName = FileNameUtil.extName(fullName);
		UploadFile file = findFileByMainName(mainName);
		if (file == null || !file.getFormat().equals(extName)) {
			return null;
		}
		return file;
	}

	@Override
	public boolean fileExists(String fullName) {
		return MinioUtils.fileExists(FileNameUtil.mainName(fullName));
	}

	@Override
	public BinaryContent downloadFileByMainName(String filename) {
		MinioFile file = (MinioFile) findFileByMainName(filename);
		return file != null ? file.toBinaryContent() : null;
	}

	@Override
	public BinaryContent downloadFileByFullName(String fullName) {
		MinioFile file = (MinioFile) findFileByFullName(fullName);
		return file != null ? file.toBinaryContent() : null;
	}

}