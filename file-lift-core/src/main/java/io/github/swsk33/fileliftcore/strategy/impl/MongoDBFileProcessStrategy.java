package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.github.swsk33.fileliftcore.config.MongoClientConfig;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.FileConfig;
import io.github.swsk33.fileliftcore.model.file.MongoFile;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.util.GridFSUtils;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

/**
 * 基于MongoDB GridFS的文件保存策略
 */
public class MongoDBFileProcessStrategy implements FileProcessStrategy {

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		// 如果关闭了自动重命名且开启了允许覆盖，则先查找数据库是否存在对应文件名，若存在则先删除
		if (!FileConfig.getInstance().isAutoRename() && FileConfig.getInstance().isOverride()) {
			MongoFile getFile = (MongoFile) findFileByMainName(saveName);
			if (getFile != null) {
				MongoClientConfig.getBucket().delete(getFile.getId());
			}
		}
		ObjectId id;
		try {
			id = GridFSUtils.uploadFile(file.getInputStream(), saveName, formatName);
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
		if (!GridFSUtils.fileExists(filename)) {
			return;
		}
		GridFSUtils.deleteFile(filename);
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		GridFSFile getFile = GridFSUtils.findFileByMainName(filename);
		if (getFile == null) {
			return null;
		}
		return MongoFile.createMongoFile(getFile);
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		GridFSFile getFile = GridFSUtils.findFileByFullName(fullName);
		if (getFile == null) {
			return null;
		}
		return MongoFile.createMongoFile(getFile);
	}

	@Override
	public boolean fileExists(String fullName) {
		return GridFSUtils.fileExists(FileNameUtil.mainName(fullName));
	}

	@Override
	public BinaryContent downloadFileByMainName(String filename) {
		MongoFile getFile = (MongoFile) findFileByMainName(filename);
		return getFile == null ? null : getFile.toBinaryContent();
	}

	@Override
	public BinaryContent downloadFileByFullName(String fullName) {
		MongoFile getFile = (MongoFile) findFileByFullName(fullName);
		return getFile == null ? null : getFile.toBinaryContent();
	}

}