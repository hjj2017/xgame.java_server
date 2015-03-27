package com.game.part.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.game.part.Error;

/**
 * 名称空间实用工具
 * 
 * @author hjj2017
 * 
 */
public class PackageUtil {
	/**
	 * 类默认构造器
	 * 
	 */
	private PackageUtil() {
	}

	/**
	 * 列表指定 URL 文件、指定包中的所有类
	 * 
	 * @param filePath
	 * @param recursive 
	 * @param filter
	 * @return 
	 * 
	 */
	public static Set<Class<?>> listClazz(
		String filePath, 
		boolean recursive, 
		IClazzFilter filter) {

		if (filePath == null || 
			filePath.isEmpty()) {
			return null;
		}

		// 创建文件对象
		File fileObj = new File(filePath);

		if (fileObj.isDirectory()) {
			// 给定参数是目录, 
			// 那么从目录中获取类列表
			return listClazzFromDir(fileObj, recursive, filter);
		} else if (fileObj.isFile() && 
			filePath.endsWith(".jar")) {
			// 给定参数是 jar 文件, 
			// 那么从 jar 文件中获取类列表
			return listClazzFromJar(fileObj, recursive, filter);
		} else {
			// 给定参数即不是目录也不是 jar 文件, 
			// 则直接返回空值!
			return null;
		}
	}

	/**
	 * 从目录中获取类列表
	 * 
	 * @param dir
	 * @param recursive 
	 * @param filter 
	 * @return 
	 * 
	 */
	private static Set<Class<?>> listClazzFromDir(
		File dir, 
		boolean recursive, 
		IClazzFilter filter) {

		if (!dir.exists() || 
			!dir.isDirectory()) {
			return null;
		}

		// 获取子文件列表
		File[] subFiles = dir.listFiles();

		if (subFiles == null || 
			subFiles.length <= 0) {
			return null;
		}

		// 文件队列
		Queue<File> fq = new LinkedList<File>();
		// 将子文件列表添加到队列
		fq.addAll(Arrays.asList(subFiles));

		// 结果对象
		Set<Class<?>> resultSet = new HashSet<Class<?>>();

		while (!fq.isEmpty()) {
			// 从队列中获取文件
			File currFile = fq.poll();
			
			if (currFile.isDirectory()) {
				// 如果当前文件是目录, 
				// 获取子文件列表
				subFiles = currFile.listFiles();
				// 添加文件到队列
				fq.addAll(Arrays.asList(subFiles));
				continue;
			}

			if (!currFile.isFile() || 
				!currFile.getName().endsWith(".class")) {
				// 如果当前文件不是文件, 
				// 或者文件名不是以 .class 结尾, 
				// 则直接跳过
				continue;
			}

			// 类名称
			String clazzName;

			// 设置类名称
			clazzName = currFile.getAbsolutePath();
			// 清除最后的 .class 结尾
			clazzName = clazzName.substring(dir.getAbsolutePath().length(), clazzName.lastIndexOf('.'));
			// 转换目录斜杠
			clazzName = clazzName.replace('\\', '/');
			// 清除开头的 /
			clazzName = StringUtil.trimLeft(clazzName, "/");
			// 将所有的 / 修改为 .
			clazzName = StringUtil.join(clazzName.split("/"), ".");

			try {
				// 加载类定义
				Class<?> clazzObj = Class.forName(clazzName);
	
				if ((filter != null) && 
				    !filter.accept(clazzObj)) {
					// 如果过滤器不为空, 
					// 且过滤器不接受当前类, 
					// 则直接跳过!
					continue;
				}

				// 添加类定义到集合
				resultSet.add(clazzObj);
			} catch (Exception ex) {
				// 抛出异常
				throw new Error(ex);
			}
		}

		return resultSet;
	}

	/**
	 * 从 .jar 文件中获取类列表
	 * 
	 * @param jarFilePath
	 * @param recursive 
	 * @param filter 
	 * @return 
	 * 
	 */
	private static Set<Class<?>> listClazzFromJar(
		File jarFilePath, 
		boolean recursive, 
		IClazzFilter filter) {

		if (jarFilePath == null || 
			jarFilePath.isDirectory()) {
			return null;
		}

		// 结果对象
		Set<Class<?>> resultSet = new HashSet<Class<?>>();

		try {
			// 创建 .jar 文件读入流
			JarInputStream jarIn = new JarInputStream(new FileInputStream(jarFilePath));
			// 进入点
			JarEntry entry;

			while ((entry = jarIn.getNextJarEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}

				// 获取进入点名称
				String entryName = entry.getName();

				if (!entryName.endsWith(".class")) {
					// 如果不是以 .class 结尾, 
					// 则说明不是 JAVA 类文件, 直接跳过!
					continue;
				}

				String clazzName;

				// 清除最后的 .class 结尾
				clazzName = entryName.substring(0, entryName.lastIndexOf('.'));
				// 将所有的 / 修改为 .
				clazzName = StringUtil.join(clazzName.split("/"), ".");

				// 加载类定义
				Class<?> clazzObj = Class.forName(clazzName);

				if ((filter != null) && 
				    !filter.accept(clazzObj)) {
					// 如果过滤器不为空, 
					// 且过滤器不接受当前类, 
					// 则直接跳过!
					continue;
				}

				// 添加类定义到集合
				resultSet.add(clazzObj);
			}

			// 关闭 jar 输入流
			jarIn.close();
		} catch (Exception ex) {
			// 抛出异常
			throw new Error(ex);
		}

		return resultSet;
	}

	/**
	 * 类名称过滤器
	 * 
	 * @author hjj2019
	 *
	 */
	@FunctionalInterface
	public static interface IClazzFilter {
		/**
		 * 是否接受当前类?
		 * 
		 * @param clazz
		 * @return 
		 * 
		 */
		boolean accept(Class<?> clazz);
	}
}
