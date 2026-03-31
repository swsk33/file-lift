package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.mongodb.client.gridfs.GridFSBucket;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.bson.types.ObjectId;

/**
 * MongoDB文件类型，存放文件信息
 */
@Value
@ToString(callSuper = true, exclude = "bucket")
@EqualsAndHashCode(callSuper = true)
public class MongoFile extends UploadFile {

	/**
	 * 文件id
	 */
	ObjectId id;

	/**
	 * 当前文件所属的 GridFS 桶
	 */
	transient GridFSBucket bucket;

	/**
	 * 私有构造函数
	 *
	 * @param name   文件名
	 * @param format 扩展名
	 * @param length 文件大小
	 * @param id     文件id
	 * @param bucket 所属桶
	 */
	private MongoFile(String name, String format, long length, ObjectId id, GridFSBucket bucket) {
		super(name, format, length);
		this.id = id;
		this.bucket = bucket;
	}

	/**
	 * 创建 MongoDB 文件对象
	 *
	 * @param name   文件名
	 * @param format 扩展名
	 * @param length 文件大小
	 * @param id     文件id
	 * @param bucket 所属桶
	 * @return 文件对象
	 */
	public static MongoFile createMongoFile(String name, String format, long length, ObjectId id, GridFSBucket bucket) {
		return new MongoFile(name, format, length, id, bucket);
	}

	@Override
	public BinaryContent toBinaryContent() {
		BinaryContent content = new BinaryContent();
		StringBuilder fileFullName = new StringBuilder(this.getName());
		if (!StrUtil.isEmpty(this.getFormat())) {
			fileFullName.append(".").append(this.getFormat());
		}
		content.setContentType(HttpUtil.getMimeType(fileFullName.toString(), "application/octet-stream"));
		content.setFileStream(bucket.openDownloadStream(this.getId()));
		return content;
	}

}