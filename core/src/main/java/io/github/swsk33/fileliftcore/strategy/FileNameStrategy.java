package io.github.swsk33.fileliftcore.strategy;

/**
 * 用于生成文件名的策略
 */
public interface FileNameStrategy {

	/**
	 * 生成一个随机文件名用于自动重命名
	 *
	 * @return 随机文件名，不带扩展名
	 */
	String generateFileName();

}