package org.think2framework.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

	private static class FileFilter implements FilenameFilter {

		private Pattern pattern; // 正则表达式过滤

		private FileFilter(String filename) {
			pattern = Pattern.compile(filename);
		}

		@Override
		public boolean accept(File dir, String name) {
			Matcher matcher = pattern.matcher(name);
			if (matcher.find()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 根据文件路径和文件名，获取文件列表，如果文件名带*则加载多个
	 *
	 * @param file
	 *            文件路径和文件名
	 * @return 文件列表
	 */
	public static File[] getFiles(String file) {
		String filePath = "";
		String fileName;
		if (StringUtils.contains(file, "/")) {
			filePath = StringUtils.substringBeforeLast(file, "/");
			fileName = StringUtils.substringAfterLast(file, "/");
		} else {
			fileName = file;
		}
		File files = new File(filePath);
		return files.listFiles(new FileFilter(fileName));
	}
}
