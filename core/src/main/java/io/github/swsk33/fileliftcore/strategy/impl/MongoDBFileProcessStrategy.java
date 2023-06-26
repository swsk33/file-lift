package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.github.swsk33.fileliftcore.config.MongoClientConfig;
import io.github.swsk33.fileliftcore.model.file.MongoFile;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.util.GridFSUtils;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import static io.github.swsk33.fileliftcore.util.GridFSUtils.*;

/**
 * 基于MongoDB GridFS的文件保存策略
 */
public class MongoDBFileProcessStrategy implements FileProcessStrategy {

	/**
	 * GridFS桶对象
	 */
	private GridFSBucket bucket;

	/**
	 * 第一次存放文件时，执行初始化操作（懒加载）
	 */
	private void init() {
		bucket = MongoClientConfig.getBucket();
	}

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		// 第一次调用时初始化
		if (bucket == null) {
			init();
		}
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		ObjectId id;
		try {
			id = uploadFile(bucket, file.getInputStream(), saveName, formatName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 返回文件信息
		MongoFile uploadedFile = new MongoFile();
		uploadedFile.setName(saveName);
		uploadedFile.setFormat(formatName);
		uploadedFile.setId(id);
		return uploadedFile;
	}

	@Override
	public void deleteFile(String filename) {
		// 第一次调用时初始化
		if (bucket == null) {
			init();
		}
		if (!fileExists(bucket, filename)) {
			return;
		}
		GridFSUtils.deleteFile(bucket, filename);
	}

	@Override
	public void renameFile(String originName, String newName) {
		// 第一次调用时初始化
		if (bucket == null) {
			init();
		}
		if (!fileExists(bucket, originName)) {
			return;
		}
		GridFSUtils.renameFile(bucket, originName, newName);
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		// 第一次调用时初始化
		if (bucket == null) {
			init();
		}
		GridFSFile getFile = findFileByName(bucket, filename);
		if (getFile == null) {
			return null;
		}
		MongoFile result = new MongoFile();
		result.setName(filename);
		if (getFile.getMetadata() != null) {
			result.setFormat(getFile.getMetadata().get("type").toString());
		}
		result.setFileStream(bucket.openDownloadStream(getFile.getObjectId()));
		result.setId(getFile.getObjectId());
		return result;
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		// 第一次调用时初始化
		if (bucket == null) {
			init();
		}
		return findFileByMainName(FileNameUtil.mainName(fullName));
	}

}