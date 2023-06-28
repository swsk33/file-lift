package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.mongodb.client.gridfs.GridFSBucket;
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

import static io.github.swsk33.fileliftcore.util.GridFSUtils.findFileByName;
import static io.github.swsk33.fileliftcore.util.GridFSUtils.uploadFile;

/**
 * 基于MongoDB GridFS的文件保存策略
 */
public class MongoDBFileProcessStrategy implements FileProcessStrategy {

	/**
	 * GridFS桶对象
	 */
	private volatile GridFSBucket bucket;

	/**
	 * 获取GridFS桶对象
	 *
	 * @return GridFS桶对象
	 */
	private GridFSBucket getBucket() {
		// 使用双检锁延迟初始化对象
		if (bucket == null) {
			synchronized (MongoDBFileProcessStrategy.class) {
				if (bucket == null) {
					bucket = MongoClientConfig.getBucket();
				}
			}
		}
		return bucket;
	}

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		// 如果关闭了自动重命名且开启了允许覆盖，则先查找数据库是否存在对应文件名，若存在则先删除
		if (!FileConfig.getInstance().isAutoRename() && FileConfig.getInstance().isOverride()) {
			MongoFile getFile = (MongoFile) findFileByMainName(saveName);
			if (getFile != null) {
				bucket.delete(getFile.getId());
			}
		}
		ObjectId id;
		try {
			id = uploadFile(getBucket(), file.getInputStream(), saveName, formatName);
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
		if (!GridFSUtils.fileExists(getBucket(), filename)) {
			return;
		}
		GridFSUtils.deleteFile(getBucket(), filename);
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		GridFSFile getFile = findFileByName(getBucket(), filename);
		if (getFile == null) {
			return null;
		}
		MongoFile result = new MongoFile();
		result.setName(filename);
		if (getFile.getMetadata() != null) {
			result.setFormat(getFile.getMetadata().get("type").toString());
		}
		result.setId(getFile.getObjectId());
		return result;
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		return findFileByMainName(FileNameUtil.mainName(fullName));
	}

	@Override
	public boolean fileExists(String fullName) {
		return GridFSUtils.fileExists(getBucket(), FileNameUtil.mainName(fullName));
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