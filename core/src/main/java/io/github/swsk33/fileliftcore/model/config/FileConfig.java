package io.github.swsk33.fileliftcore.model.config;

import io.github.swsk33.fileliftcore.param.AutoRenameFormats;
import io.github.swsk33.fileliftcore.param.FileStorageMethods;
import lombok.Data;

/**
 * 通用配置类
 */
@Data
public abstract class FileConfig {

	/**
	 * 设定文件的储存方式，可以配置下列值
	 * 配置值参考：io.github.swsk33.fileliftcore.param.FileStorage中常量值
	 * 若未配置或者配置为其它错误的值，则按照默认的filesystem方式
	 */
	private String storage = FileStorageMethods.FILE;

	/**
	 * 允许的文件格式（扩展名，不带.），若不配置则允许任何格式文件上传
	 */
	private String[] allowedFormats;

	/**
	 * 文件大小限制，单位为KB，不配置或者配置为0表示不限制大小
	 */
	private int sizeLimit;

	/**
	 * <strong>仅在autoRename为false时生效！</strong>
	 * <br>
	 * 是否开启同名覆盖
	 */
	private boolean override = false;

	/**
	 * 是否开启文件上传后自动重命名
	 */
	private boolean autoRename = true;

	/**
	 * <strong>仅在autoRename为true时生效！</strong>
	 * <br>
	 * 自动命名的命名格式
	 * 配置值参考io.github.swsk33.fileliftcore.param.AutoRenameFormat中的常量值
	 * 若未配置或者配置为其它错误值，则按照默认的simple_uuid方式
	 */
	private String autoRenameFormat = AutoRenameFormats.SIMPLE_UUID;

}