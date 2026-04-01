package io.github.swsk33.fileliftcore.model.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * S3对象存储文件类型
 */
@Value
@ToString(callSuper = true, exclude = "s3Client")
@EqualsAndHashCode(callSuper = true)
public class S3File extends UploadFile {

	/**
	 * 所属桶名称
	 */
	String bucketName;

	/**
	 * S3对象Key（完整路径）
	 */
	String objectKey;

	/**
	 * 文件Content-Type
	 */
	String contentType;

	/**
	 * 当前文件所属S3客户端
	 */
	@Getter(AccessLevel.NONE)
	transient S3Client s3Client;

	/**
	 * 私有构造函数
	 *
	 * @param name        文件名
	 * @param format      扩展名
	 * @param length      文件大小
	 * @param bucketName  所属桶
	 * @param objectKey   文件完整路径
	 * @param contentType 文件Content-Type类型
	 * @param s3Client    S3客户端引用
	 */
	private S3File(String name, String format, long length, String bucketName, String objectKey, String contentType, S3Client s3Client) {
		super(name, format, length);
		this.bucketName = bucketName;
		this.objectKey = objectKey;
		this.contentType = contentType;
		this.s3Client = s3Client;
	}

	/**
	 * 创建S3文件对象
	 *
	 * @param name        文件名
	 * @param format      扩展名
	 * @param length      文件大小
	 * @param bucketName  所属桶
	 * @param objectKey   文件完整路径
	 * @param contentType 文件Content-Type类型
	 * @param s3Client    S3客户端引用
	 * @return S3文件对象
	 */
	public static S3File createS3File(String name, String format, long length, String bucketName, String objectKey, String contentType, S3Client s3Client) {
		return new S3File(name, format, length, bucketName, objectKey, contentType, s3Client);
	}

	@Override
	public BinaryContent toBinaryContent() {
		String mimeType = contentType;
		if (StrUtil.isEmpty(mimeType)) {
			mimeType = HttpUtil.getMimeType(objectKey, "application/octet-stream");
		}
		return new BinaryContent(
				mimeType,
				s3Client.getObject(builder -> builder.bucket(bucketName).key(objectKey))
		);
	}

}