package io.github.swsk33.fileliftcore.strategy.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import io.github.swsk33.fileliftcore.model.config.FileSystemConfig;
import io.github.swsk33.fileliftcore.model.file.LocalFile;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.strategy.FileProcessStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 适用于文件系统的文件处理方案
 */
public class FileSystemProcessStrategy implements FileProcessStrategy {

	/**
	 * 存放文件的文件夹对象
	 */
	private volatile File saveFolder;

	/**
	 * 文件搜索缓存，存放每次查找结果，以加快查找速度
	 * key：文件名，不带扩展名
	 * value：文件对象
	 */
	private volatile Map<String, UploadFile> fileSearchCache;

	/**
	 * 获取保存文件夹的绝对路径
	 *
	 * @return 保存文件夹的绝对路径
	 */
	private String getSaveFolderPath() {
		// 使用双检锁延迟初始化
		if (saveFolder == null) {
			synchronized (FileSystemProcessStrategy.class) {
				if (saveFolder == null) {
					saveFolder = new File(FileSystemConfig.getInstance().getSaveFolder());
					// 文件夹不存在则创建
					if (!saveFolder.exists()) {
						saveFolder.mkdirs();
					}
				}
			}
		}
		return saveFolder.getAbsolutePath();
	}

	/**
	 * 获取存放文件缓存结果的哈希表对象
	 *
	 * @return 存放文件缓存结果的哈希表
	 */
	private Map<String, UploadFile> getCacheMap() {
		// 使用双检锁延迟初始化
		if (fileSearchCache == null) {
			synchronized (FileSystemProcessStrategy.class) {
				if (fileSearchCache == null) {
					fileSearchCache = new ConcurrentHashMap<>();
				}
			}
		}
		return fileSearchCache;
	}

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		// 保存文件
		StringBuilder fileSaveFullName = new StringBuilder(saveName);
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		if (!StrUtil.isEmpty(formatName)) {
			fileSaveFullName.append(".").append(formatName);
		}
		String absolutePath = getSaveFolderPath() + File.separator + fileSaveFullName;
		try {
			file.transferTo(FileUtil.file(absolutePath));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return LocalFile.createLocalFile(absolutePath);
	}

	@Override
	public void deleteFile(String filename) {
		UploadFile getFile = findFileByMainName(filename);
		if (getFile == null) {
			return;
		}
		FileUtil.del(((LocalFile) getFile).getAbsolutePath());
		// 若缓存中存在，也进行移除
		getCacheMap().remove(filename);
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		// 查找对应文件
		// 先在缓存查找
		UploadFile getFile = getCacheMap().get(filename);
		// 若查找得到结果为空，则在文件系统查找
		if (getFile == null) {
			// 找到的文件路径
			StringBuilder filePath = new StringBuilder();
			try (Stream<Path> fileStream = Files.walk(Paths.get(getSaveFolderPath()), 1)) {
				fileStream.filter(path -> path.toFile().isFile()).anyMatch(path -> {
					String name = path.getFileName().toString();
					// 匹配到对应文件则赋值并停止遍历
					if (name.startsWith(filename)) {
						filePath.append(path.toFile().getAbsolutePath());
						return true;
					}
					return false;
				});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			// 处理查找到的文件
			// 若文件路径为空说明未找到
			if (StrUtil.isEmpty(filePath)) {
				return null;
			}
			// 生成文件对象
			getFile = LocalFile.createLocalFile(filePath.toString());
			// 存入缓存
			getCacheMap().put(filename, getFile);
		}
		return getFile;
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		return LocalFile.createLocalFile(getSaveFolderPath() + File.separator + fullName);
	}

	@Override
	public boolean fileExists(String fullName) {
		return FileUtil.file(getSaveFolderPath() + File.separator + fullName).exists();
	}

	@Override
	public InputStream downloadFileByMainName(String filename) {
		// 先查找文件
		UploadFile getFile = findFileByMainName(filename);
		return getFile == null ? null : FileUtil.getInputStream(((LocalFile) getFile).getAbsolutePath());
	}

	@Override
	public InputStream downloadFileByFullName(String fullName) {
		// 先获取文件
		UploadFile getFile = findFileByFullName(fullName);
		return getFile == null ? null : FileUtil.getInputStream(((LocalFile) getFile).getAbsolutePath());
	}

}