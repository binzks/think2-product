package org.think2framework.utils;

import org.think2framework.exception.SimpleException;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描包工具
 */
public class PackageUtils {

	/**
	 * 扫描一个包名下所有的类
	 * 
	 * @param packageDirName
	 *            包名
	 * @return 类的数组
	 */
	public static List<Class> scanPackage(String packageDirName) {
		List<Class> list = new ArrayList<>();
		Enumeration<URL> dirs;
		try {
			String packageName = packageDirName.replace('.', '/');
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					scanDirectory(packageDirName, filePath, list);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件 定义一个JarFile
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					// 从此jar包 得到一个枚举类
					Enumeration<JarEntry> entries = jar.entries();
					// 同样的进行循环迭代
					while (entries.hasMoreElements()) {
						// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						// 如果是以/开头的
						if (name.charAt(0) == '/') {
							// 获取后面的字符串
							name = name.substring(1);
						}
						// 如果前半部分和定义的包名相同
						if (name.startsWith(packageName)) {
							// 如果可以迭代下去 并且是一个包
							if (name.lastIndexOf('/') != -1) {
								// 如果是一个.class文件 而且不是目录
								if (name.endsWith(".class") && !entry.isDirectory()) {
									// 去掉后面的".class" 获取真正的类名
									String className = name.substring(packageDirName.length() + 1, name.length() - 6);
									list.add(Class.forName(packageDirName + "." + className));
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {
			throw new SimpleException(e);
		}
		return list;
	}

	/**
	 * 以文件的形式来获取包下的所有Class,并且添加dao模型
	 *
	 * @param packageDirName
	 *            包名
	 * @param path
	 *            文件夹路径
	 */
	private static void scanDirectory(String packageDirName, String path, List<Class> list) {
		// 获取此包的目录 建立一个File
		File dir = new File(path);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在就获取包下的所有文件包括目录
		File[] dirFiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) { // 自定义过滤规则,如果可以循环(包含子目录)或则是以.class结尾的文件(编译好的java类文件)
				return (pathname.isDirectory()) || (pathname.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirFiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				scanDirectory(packageDirName + "." + file.getName(), file.getAbsolutePath(), list);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					list.add(Class.forName(packageDirName + "." + className));
				} catch (ClassNotFoundException e) {
					throw new SimpleException(e);
				}
			}
		}
	}
}
