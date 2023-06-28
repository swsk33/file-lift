package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.github.swsk33.fileliftcore.config.MongoClientConfig;
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

	@Override
	public BinaryContent toBinaryContent() {
		BinaryContent content = new BinaryContent();
		StringBuilder fileFullName = new StringBuilder(this.getName());
		if (!StrUtil.isEmpty(this.getFormat())) {
			fileFullName.append(".").append(this.getFormat());
		}
		content.setContentType(HttpUtil.getMimeType(fileFullName.toString(), "application/octet-stream"));
		content.setFileStream(MongoClientConfig.getBucket().openDownloadStream(this.getId()));
		return content;
	}

}