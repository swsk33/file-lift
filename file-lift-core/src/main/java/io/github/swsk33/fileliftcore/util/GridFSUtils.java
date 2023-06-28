package io.github.swsk33.fileliftcore.util;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装了一些常用的MongoDB GridFS操作的实用类
 */
public class GridFSUtils {

	/**
	 * 上传文件至MongoDB
	 *
	 * @param bucket          GridFS Bucket对象
	 * @param fileInputStream 文件输入流对象
	 * @param filename        上传后的文件名（存到MongoDB后的文件名，不带扩展名）
	 * @param type            文件原本的扩展名，不带.
	 * @return 上传文件后的文件id
	 */
	public static ObjectId uploadFile(GridFSBucket bucket, InputStream fileInputStream, String filename, String type) throws IOException {
		GridFSUploadOptions options = new GridFSUploadOptions();
		options.chunkSizeBytes(fileInputStream.available());
		options.metadata(new Document("type", type));
		return bucket.uploadFromStream(filename, fileInputStream, options);
	}

	/**
	 * 删除文件
	 *
	 * @param bucket   GridFS Bucket对象
	 * @param filename 要删除的文件名（不带扩展名）
	 */
	public static void deleteFile(GridFSBucket bucket, String filename) {
		GridFSFile file = findFileByName(bucket, filename);
		if (file == null) {
			return;
		}
		bucket.delete(file.getObjectId());
	}

	/**
	 * 根据文件名查询文件对象（不带扩展名）
	 *
	 * @param bucket   GridFS Bucket对象
	 * @param filename 文件名（不带扩展名）
	 * @return 文件对象，若文件不存在则为null
	 */
	public static GridFSFile findFileByName(GridFSBucket bucket, String filename) {
		GridFSFile result;
		Bson query = Filters.eq("filename", filename);
		try (MongoCursor<GridFSFile> cursor = bucket.find(query).cursor()) {
			if (!cursor.hasNext()) {
				return null;
			}
			result = cursor.next();
		}
		return result;
	}

	/**
	 * 检测文件是否存在于MongoDB中
	 *
	 * @param bucket   GridFS Bucket对象
	 * @param filename 文件名（不带扩展名）
	 * @return 文件是否存在
	 */
	public static boolean fileExists(GridFSBucket bucket, String filename) {
		return findFileByName(bucket, filename) != null;
	}

}