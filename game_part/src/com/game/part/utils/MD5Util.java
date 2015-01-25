package com.game.part.utils;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * MD5 加密编码工具类
 *
 *
 */
public class MD5Util {
	
	/**
	 * 将输入的字符串进行MD5加密（编码）
	 * 
	 * @param inputString
	 * @return
	 */
	public static String createMD5String(String inputString) {
		return encodeByMD5(inputString);
	}

	/**
	 * 验证MD5密码是否正确
	 * 
	 * @param pass
	 * @param inputString
	 * @return
	 */
	public static boolean authMD5String(String md5, String inputString) {
		if (md5.equals(encodeByMD5(inputString))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 对字符串进行 MD5 编码
	 * 
	 * @param origStr
	 * @return 
	 * 
	 */
	public static String encodeByMD5(String origStr) {
		if (origStr == null) {
			return null;
		} else {
			return DigestUtils.md5Hex(origStr);
		}
	}

	/**
	 * 对字节进行加密
	 * 
	 * @param byteArr
	 * @return 
	 * 
	 */
	public static String encodeByMD5(byte[] byteArr) {
		return DigestUtils.md5Hex(byteArr);
	}
}
