package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.config.CoreConfig;
import io.github.swsk33.fileliftcore.model.config.S3Config;
import io.github.swsk33.fileliftcore.model.file.S3File;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import io.github.swsk33.fileliftcore.util.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;

/**
 * 基于S3协议兼容对象存储的文件保存策略
 */
@Slf4j
public class S3FileProcessStrategy implements FileProcessStrategy {

	/**
	 * 框架核心配置
	 */
	private final CoreConfig coreConfig;

	/**
	 * S3 客户端对象
	 */
	private final S3Client s3Client;

	/**
	 * 桶名
	 */
	private final String bucketName;

	/**
	 * S3 协议文件系统策略构造函数
	 *
	 * @param coreConfig 核心配置对象
	 * @param s3Config   S3 配置对象
	 */
	public S3FileProcessStrategy(CoreConfig coreConfig, S3Config s3Config) {
		this.coreConfig = coreConfig;
		this.bucketName = s3Config.getBucketName();
		if (StrUtil.isEmpty(bucketName)) {
			throw new IllegalArgumentException("S3策略初始化失败：未配置bucketName");
		}
		// 地区配置
		Region region = s3Config.getRegion();
		// 创建S3客户端构建器
		S3ClientBuilder clientBuilder = S3Client.builder()
				.region(region)
				.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(s3Config.isPathStyle()).build());
		// 配置 Endpoint
		if (!StrUtil.isEmpty(s3Config.getEndpoint())) {
			clientBuilder.endpointOverride(URI.create(s3Config.getEndpoint()));
		}
		// 配置密码
		if (!StrUtil.isEmpty(s3Config.getAccessKey()) && !StrUtil.isEmpty(s3Config.getSecretKey())) {
			clientBuilder.credentialsProvider(StaticCredentialsProvider.create(
					AwsBasicCredentials.create(s3Config.getAccessKey(), s3Config.getSecretKey())
			));
		} else {
			clientBuilder.credentialsProvider(AnonymousCredentialsProvider.create());
		}
		s3Client = clientBuilder.build();
		ensureBucketExists(region);
	}

	/**
	 * 创建一个S3文件对象
	 *
	 * @param objectKey   完整路径
	 * @param length      文件大小
	 * @param contentType Content-Type类型
	 * @return S3文件对象
	 */
	private S3File createS3File(String objectKey, long length, String contentType) {
		return S3File.createS3File(
				FileNameUtil.mainName(objectKey),
				FileNameUtil.extName(objectKey),
				length,
				bucketName,
				objectKey,
				contentType,
				s3Client
		);
	}

	/**
	 * 确保文件桶存在，不存在会自动创建
	 *
	 * @param region 区域
	 */
	private void ensureBucketExists(Region region) {
		try {
			s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
		} catch (S3Exception e) {
			if (e.statusCode() != 404) {
				throw e;
			}
			CreateBucketRequest.Builder requestBuilder = CreateBucketRequest.builder().bucket(bucketName);
			if (!Region.US_EAST_1.equals(region) && !Region.AWS_GLOBAL.equals(region)) {
				requestBuilder.createBucketConfiguration(
						CreateBucketConfiguration.builder().locationConstraint(region.id()).build()
				);
			}
			s3Client.createBucket(requestBuilder.build());
			log.info("S3存储桶不存在，已自动创建：{}", bucketName);
		}
	}

	/**
	 * 删除对象
	 *
	 * @param objectKey 对象键名（S3完整路径）
	 */
	private void deleteObject(String objectKey) {
		s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build());
	}

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		// 处理文件名
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		String objectKey = FileNameUtils.buildFullName(saveName, formatName);
		if (coreConfig.isOverride() && fileExists(objectKey)) {
			deleteObject(objectKey);
		}
		try {
			String contentType = file.getContentType();
			if (StrUtil.isEmpty(contentType)) {
				contentType = HttpUtil.getMimeType(objectKey, "application/octet-stream");
			}
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(objectKey)
					.contentType(contentType)
					.build();
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
			return createS3File(objectKey, file.getSize(), contentType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteFile(String filename) {
		S3File file = (S3File) findFileByMainName(filename);
		if (file == null) {
			return;
		}
		deleteObject(file.getObjectKey());
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		ListObjectsV2Request request = ListObjectsV2Request.builder()
				.bucket(bucketName)
				.prefix(filename)
				.maxKeys(1)
				.build();
		return s3Client.listObjectsV2(request).contents().stream()
				.findFirst()
				.map(s3Object -> findFileByFullName(s3Object.key()))
				.orElse(null);
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		try {
			HeadObjectResponse response = s3Client.headObject(
					HeadObjectRequest.builder().bucket(bucketName).key(fullName).build()
			);
			return createS3File(fullName, response.contentLength(), response.contentType());
		} catch (NoSuchKeyException e) {
			return null;
		} catch (S3Exception e) {
			if (e.statusCode() == 404) {
				return null;
			}
			throw e;
		}
	}

	@Override
	public boolean fileExists(String fullName) {
		return findFileByFullName(fullName) != null;
	}

	@Override
	public BinaryContent downloadFileByMainName(String filename) {
		S3File file = (S3File) findFileByMainName(filename);
		return file == null ? null : file.toBinaryContent();
	}

	@Override
	public BinaryContent downloadFileByFullName(String fullName) {
		S3File file = (S3File) findFileByFullName(fullName);
		return file == null ? null : file.toBinaryContent();
	}

}