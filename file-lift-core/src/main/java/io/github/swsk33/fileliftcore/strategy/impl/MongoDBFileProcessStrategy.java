package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.config.MongoConfig;
import io.github.swsk33.fileliftcore.model.file.MongoFile;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.util.FileNameUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static io.github.swsk33.fileliftcore.util.URLEncodeUtils.percentEncode;

/**
 * 基于MongoDB GridFS的文件保存策略
 */
public class MongoDBFileProcessStrategy implements FileProcessStrategy {

	/**
	 * 核心配置
	 */
	private final CoreConfig coreConfig;

	/**
	 * MongoDB GridFS 桶配置
	 */
	private final GridFSBucket bucket;

	/**
	 * 初始化 MongoDB 策略
	 *
	 * @param coreConfig  框架核心配置
	 * @param mongoConfig MongoDB 连接配置
	 */
	public MongoDBFileProcessStrategy(CoreConfig coreConfig, MongoConfig mongoConfig) {
		this.coreConfig = coreConfig;
		MongoClient mongoClient;
		String databaseName = mongoConfig.getDatabase();
		// 如果 URI 配置则解析
		if (!StrUtil.isEmpty(mongoConfig.getUri())) {
			mongoClient = MongoClients.create(mongoConfig.getUri());
			try {
				String uriDatabase = new URI(mongoConfig.getUri()).getPath();
				if (!StrUtil.isEmpty(uriDatabase) && uriDatabase.length() > 1) {
					databaseName = uriDatabase.substring(1);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("解析MongoDB URI失败！", e);
			}
		} else {
			// 否则，手动构建 URI
			StringBuilder uri = new StringBuilder("mongodb://");
			if (!StrUtil.isEmpty(mongoConfig.getUsername()) && !StrUtil.isEmpty(mongoConfig.getPassword())) {
				uri.append(percentEncode(mongoConfig.getUsername())).append(":").append(percentEncode(mongoConfig.getPassword())).append("@");
			}
			uri.append(mongoConfig.getHost()).append(":").append(mongoConfig.getPort());
			uri.append("/").append(mongoConfig.getDatabase());
			uri.append("?authSource=").append(mongoConfig.getAuthDatabase());
			mongoClient = MongoClients.create(uri.toString());
		}
		if (StrUtil.isEmpty(databaseName)) {
			throw new IllegalArgumentException("MongoDB GridFS策略初始化失败：未配置database");
		}
		bucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), mongoConfig.getBucketName());
	}

	/**
	 * 根据查询得到的GridFS文件结果，创建一个MongoDB文件信息对象
	 *
	 * @param file GridFS 文件结果
	 * @return MongoDB文件信息对象，若传入file为空，则返回null
	 */
	private MongoFile createMongoFile(GridFSFile file, GridFSBucket bucket) {
		String format = null;
		long length = 0L;
		if (file.getMetadata() != null) {
			format = (String) file.getMetadata().get("type");
			length = file.getLength();
		}
		return MongoFile.createMongoFile(file.getFilename(), format, length, file.getObjectId(), bucket);
	}

	/**
	 * 根据主文件名查找 GridFS 中文件
	 *
	 * @param filename 文件名不带扩展名
	 * @return 文件对象
	 */
	private GridFSFile findGridFileByMainName(String filename) {
		Bson query = eq("filename", filename);
		try (MongoCursor<GridFSFile> cursor = bucket.find(query).cursor()) {
			if (!cursor.hasNext()) {
				return null;
			}
			return cursor.next();
		}
	}

	/**
	 * 根据完整文件名查找 GridFS 文件
	 *
	 * @param fullName 完整文件名
	 * @return 文件对象
	 */
	private GridFSFile findGridFileByFullName(String fullName) {
		String mainName = FileNameUtil.mainName(fullName);
		String formatName = FileNameUtil.extName(fullName);
		if (StrUtil.isEmpty(formatName)) {
			return findGridFileByMainName(mainName);
		}
		Bson query = and(eq("filename", mainName), eq("metadata.type", formatName));
		try (MongoCursor<GridFSFile> cursor = bucket.find(query).cursor()) {
			if (!cursor.hasNext()) {
				return null;
			}
			return cursor.next();
		}
	}

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		// 文件名处理
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		String fullName = FileNameUtils.buildFullName(saveName, formatName);
		// GridFS不会直接覆盖同名文件，这里在允许覆盖时先精确删除原文件
		if (coreConfig.isOverride() && fileExists(fullName)) {
			GridFSFile deleteFile = findGridFileByFullName(fullName);
			if (deleteFile != null) {
				bucket.delete(deleteFile.getObjectId());
			}
		}
		// 执行上传
		try {
			GridFSUploadOptions options = new GridFSUploadOptions();
			options.metadata(new Document("type", formatName));
			ObjectId id = bucket.uploadFromStream(saveName, file.getInputStream(), options);
			return MongoFile.createMongoFile(saveName, formatName, file.getSize(), id, bucket);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteFile(String filename) {
		GridFSFile file = findGridFileByMainName(filename);
		if (file == null) {
			return;
		}
		bucket.delete(file.getObjectId());
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		GridFSFile getFile = findGridFileByMainName(filename);
		return getFile == null ? null : createMongoFile(getFile, bucket);
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		GridFSFile getFile = findGridFileByFullName(fullName);
		return getFile == null ? null : createMongoFile(getFile, bucket);
	}

	@Override
	public boolean fileExists(String fullName) {
		return findGridFileByFullName(fullName) != null;
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