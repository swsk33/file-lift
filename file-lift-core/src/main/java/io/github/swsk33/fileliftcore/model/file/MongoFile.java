package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.github.swsk33.fileliftcore.config.MongoClientConfigure;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import lombok.Data;
import org.bson.types.ObjectId;

/**
 * MongoDB文件类型，存放文件信息
 */
@Data
public class MongoFile extends UploadFile {

	/**
	 * 文件的id
	 */
	private ObjectId id;

	/**
	 * 根据查询得到的GridFS文件结果，创建一个MongoDB文件信息对象
	 *
	 * @param file GridFS文件结果
	 * @return MongoDB文件信息对象，若传入file为空，则返回null
	 */
	public static MongoFile createMongoFile(GridFSFile file) {
		MongoFile mongoFile = new MongoFile();
		mongoFile.setName(file.getFilename());
		if (file.getMetadata() != null) {
			mongoFile.setFormat((String) file.getMetadata().get("type"));
			mongoFile.setLength(file.getLength());
		}
		mongoFile.setId(file.getObjectId());
		return mongoFile;
	}

	@Override
	public BinaryContent toBinaryContent() {
		BinaryContent content = new BinaryContent();
		StringBuilder fileFullName = new StringBuilder(this.getName());
		if (!StrUtil.isEmpty(this.getFormat())) {
			fileFullName.append(".").append(this.getFormat());
		}
		content.setContentType(HttpUtil.getMimeType(fileFullName.toString(), "application/octet-stream"));
		content.setFileStream(MongoClientConfigure.getBucket().openDownloadStream(this.getId()));
		return content;
	}

}