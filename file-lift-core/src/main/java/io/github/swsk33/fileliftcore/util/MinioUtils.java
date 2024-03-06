package io.github.swsk33.fileliftcore.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import io.github.swsk33.fileliftcore.config.MinioClientConfigure;
import io.github.swsk33.fileliftcore.model.config.MinioConfig;
import io.minio.*;
import io.minio.messages.Tags;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装了一些常用的MinIO操作的实用类
 */
public class MinioUtils {

	/**
	 * MinIO客户端对象
	 */
	private static MinioClient client;

	/**
	 * 获取客户端对象
	 *
	 * @return MinIO客户端对象
	 */
	private static MinioClient getClient() {
		if (client == null) {
			client = MinioClientConfigure.getMinioClient();
		}
		return client;
	}

	/**
	 * 获取配置的储存桶名称
	 *
	 * @return 储存桶名称
	 */
	private static String getBucketName() {
		return MinioConfig.getInstance().getBucketName();
	}

	/**
	 * 上传文件至MinIO
	 *
	 * @param fileInputStream 文件输入流对象
	 * @param filename        上传后的文件名（存到MinIO后的文件名，不带扩展名）
	 * @param type            文件原本的扩展名，不带.
	 * @return 上传文件后的响应操作信息
	 */
	public static ObjectWriteResponse uploadFile(InputStream fileInputStream, String filename, String type) throws Exception {
		// 构造条件
		Map<String, String> tagMap = new HashMap<>();
		tagMap.put("type", type);
		PutObjectArgs args = PutObjectArgs.builder()
				.bucket(getBucketName())
				.object(filename)
				.contentType(HttpUtil.getMimeType(filename + "." + type, "application/octet-stream"))
				.tags(tagMap)
				.stream(fileInputStream, fileInputStream.available(), -1).build();
		// 上传文件
		ObjectWriteResponse result = getClient().putObject(args);
		// 上传完成关闭流
		fileInputStream.close();
		return result;
	}

	/**
	 * 删除文件
	 *
	 * @param filename 要删除的文件名（不带扩展名）
	 */
	public static void deleteFile(String filename) {
		RemoveObjectArgs args = RemoveObjectArgs.builder()
				.bucket(getBucketName())
				.object(filename).build();
		try {
			getClient().removeObject(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件名查询文件对象（不带扩展名）
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 文件信息对象，若文件不存在则为null
	 */
	public static StatObjectResponse findFileByMainName(String filename) {
		StatObjectArgs args = StatObjectArgs.builder()
				.bucket(getBucketName())
				.object(filename).build();
		StatObjectResponse result = null;
		try {
			result = getClient().statObject(args);
		} catch (Exception e) {
			// not print
		}
		return result;
	}

	/**
	 * 获取文件的标签信息
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 得到的文件标签信息，查询失败返回null
	 */
	public static Map<String, String> getFileTags(String filename) {
		Map<String, String> tagMap;
		GetObjectTagsArgs args = GetObjectTagsArgs.builder()
				.bucket(getBucketName())
				.object(filename).build();
		try {
			Tags tags = getClient().getObjectTags(args);
			tagMap = tags.get();
		} catch (Exception e) {
			return null;
		}
		return tagMap;
	}

	/**
	 * 检测文件是否存在于MinIO中
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 文件是否存在
	 */
	public static boolean fileExists(String filename) {
		return findFileByMainName(filename) != null;
	}

	/**
	 * 下载文件
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 下载得到的文件流，若文件不存在则为null
	 */
	public static InputStream downloadFileStream(String filename) {
		byte[] content;
		GetObjectArgs args = GetObjectArgs.builder()
				.bucket(getBucketName())
				.object(filename).build();
		// 下载并读取
		try (InputStream stream = getClient().getObject(args)) {
			content = IoUtil.readBytes(stream);
		} catch (Exception e) {
			return null;
		}
		// 从字节创建流
		return new ByteArrayInputStream(content);
	}

}