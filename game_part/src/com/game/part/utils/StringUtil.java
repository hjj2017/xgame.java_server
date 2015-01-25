package com.game.part.utils;

/**
 * 字符串工具类
 * 
 * @author hjj2017
 *
 */
public class StringUtil {
	/**
	 * 类默认构造器
	 * 
	 */
	private StringUtil() {
	}

	/**
	 * 使用连接符连接字符串数组
	 * 
	 * @param strs
	 * @param conn
	 * @return
	 */
	public static String join(String[] strs, char conn) {
		return join(strs, String.valueOf(conn));
	}

	/**
	 * 使用连接符连接字符串数组
	 * 
	 * @param strs
	 * @param conn
	 * @return 
	 * 
	 */
	public static String join(String[] strs, String conn) {
		if (strs == null || 
			strs.length <= 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strs.length; i++) {
			if (i > 0) {
				// 添加连接符
				sb.append(conn);
			}

			// 添加字符串
			sb.append(strs[i]);
		}

		return sb.toString();
	}

	/**
	 * 清除源字符串左边的字符串
	 * 
	 * @param src
	 * @param s
	 * @return 
	 * 
	 */
	public static String trimLeft(String src, String s) {
		if (src == null || 
			src.isEmpty()) {
			return "";
		}

		if (s == null || 
			s.isEmpty()) {
			return src;
		}

		if (src.equals(s)) {
			return "";
		}

		while (src.startsWith(s)) {
			src = src.substring(s.length());
		}

		return src;
	}

	/**
	 * 清除源字符串右边的字符串
	 * 
	 * @param src
	 * @param s
	 * @return 
	 * 
	 */
	public static String trimRight(String src, String s) {
		if (src == null || 
			src.isEmpty()) {
			return "";
		}

		if (s == null || 
			s.isEmpty()) {
			return src;
		}

		if (src.equals(s)) {
			return "";
		}

		while (src.endsWith(s)) {
			src = src.substring(0, src.length() - s.length());
		}

		return src;
	}
}
