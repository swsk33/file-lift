package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.util.MinioUtils;
import lombok.Data;

/**
 * Minio文件类型，存放文件信息
 */
@Data
public class MinioFile extends UploadFile {

	/**
	 * 文件对象名称（或者桶内路径）
	 */
	private String objectName;

	/**
	 * 文件对象的etag（唯一标识）
	 */
	private String etag;

	@Override
	public BinaryContent toBinaryContent() {
		BinaryContent content = new BinaryContent();
		StringBuilder fileFullName = new StringBuilder(this.getName());
		if (!StrUtil.isEmpty(this.getFormat())) {
			fileFullName.append(".").append(this.getFormat());
		}
		content.setContentType(HttpUtil.getMimeType(fileFullName.toString(), "application/octet-stream"));
		content.setFileStream(MinioUtils.downloadFileStream(objectName));
		return content;
	}

}