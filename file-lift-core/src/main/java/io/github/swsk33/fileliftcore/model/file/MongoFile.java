package io.github.swsk33.fileliftcore.model.file;

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

}