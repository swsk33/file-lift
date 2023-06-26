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
	private File saveFolder;

	/**
	 * 文件搜索缓存，存放每次查找结果，以加快查找速度
	 * key：文件名，不带扩展名
	 * value：文件对象
	 */
	private Map<String, UploadFile> fileSearchCache;

	/**
	 * 第一次存放文件时，执行初始化操作（懒加载）
	 */
	private void init() {
		saveFolder = new File(FileSystemConfig.getInstance().getSaveFolder());
		fileSearchCache = new ConcurrentHashMap<>();
		// 文件夹不存在则创建
		if (!saveFolder.exists()) {
			saveFolder.mkdirs();
		}
	}

	@Override
	public UploadFile saveFile(MultipartFile file, String saveName) {
		// 判断是否执行初始化操作
		if (saveFolder == null) {
			init();
		}
		// 保存文件
		StringBuilder fileSaveFullName = new StringBuilder(saveName);
		String formatName = FileNameUtil.extName(file.getOriginalFilename());
		if (!StrUtil.isEmpty(formatName)) {
			fileSaveFullName.append(".").append(formatName);
		}
		String absolutePath = saveFolder.getAbsolutePath() + File.separator + fileSaveFullName;
		try {
			file.transferTo(FileUtil.file(absolutePath));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return LocalFile.createLocalFile(absolutePath, false);
	}

	@Override
	public void deleteFile(String filename) {
		// 判断是否执行初始化操作
		if (saveFolder == null) {
			init();
		}
		UploadFile getFile = findFileByMainName(filename);
		if (getFile == null) {
			return;
		}
		FileUtil.del(((LocalFile) getFile).getAbsolutePath());
		// 若缓存中存在，也进行移除
		fileSearchCache.remove(filename);
	}

	@Override
	public void renameFile(String originName, String newName) {
		// 判断是否执行初始化操作
		if (saveFolder == null) {
			init();
		}
		UploadFile getFile = findFileByMainName(originName);
		if (getFile == null) {
			return;
		}
		FileUtil.rename(FileUtil.file(((LocalFile) getFile).getAbsolutePath()), newName, true, true);
		// 修改缓存
		fileSearchCache.remove(originName);
	}

	@Override
	public UploadFile findFileByMainName(String filename) {
		// 判断是否执行初始化操作
		if (saveFolder == null) {
			init();
		}
		// 查找对应文件
		// 先在缓存查找
		UploadFile getFile = fileSearchCache.get(filename);
		// 若查找得到结果为空，则在文件系统查找
		if (getFile == null) {
			// 找到的文件路径
			StringBuilder filePath = new StringBuilder();
			try (Stream<Path> fileStream = Files.walk(Paths.get(saveFolder.getAbsolutePath()), 1)) {
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
			getFile = LocalFile.createLocalFile(filePath.toString(), true);
			// 存入缓存
			fileSearchCache.put(filename, getFile);
		}
		return getFile;
	}

	@Override
	public UploadFile findFileByFullName(String fullName) {
		// 判断是否执行初始化操作
		if (saveFolder == null) {
			init();
		}
		return LocalFile.createLocalFile(saveFolder.getAbsolutePath() + File.separator + fullName, true);
	}

}