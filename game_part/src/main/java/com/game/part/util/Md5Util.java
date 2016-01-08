package com.game.part.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Md5 加密编码工具类
 *
 *
 */
public class Md5Util {

    /**
     * 将输入的字符串进行MD5加密（编码）
     *
     * @param inputString
     * @return
     */
    public static String createMd5String(String inputString) {
        return encodeByMd5(inputString);
    }

    /**
     * 验证 MD5 密码是否正确
     *
     * @param md5
     * @param inputString
     * @return
     */
    public static boolean authMd5String(String md5, String inputString) {
        if (md5.equals(encodeByMd5(inputString))) {
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
    public static String encodeByMd5(String origStr) {
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
    public static String encodeByMd5(byte[] byteArr) {
        return DigestUtils.md5Hex(byteArr);
    }
}
