package com.game.part.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 字符串工具类
 */
public final class StringUtil {
    /**
     * 类默认构造器
     */
    private StringUtil() {
    }

    /**
     * 使用连接符连接字符串数组
     *
     * @param strArr 字符串数组
     * @param conn   连接符
     * @return 连接后的字符串
     */
    static public String join(String[] strArr, char conn) {
        return join(strArr, String.valueOf(conn));
    }

    /**
     * 使用连接符连接字符串数组
     *
     * @param strArr 字符串数组
     * @param conn   连接符
     * @return 连接后的字符串
     */
    static public String join(String[] strArr, String conn) {
        if (null == strArr ||
            strArr.length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) {
                // 添加连接符
                sb.append(conn);
            }

            // 添加字符串
            sb.append(strArr[i]);
        }

        return sb.toString();
    }

    /**
     * 清除源字符串左边的字符串
     *
     * @param src     原字符串
     * @param trimStr 需要被清除的字符串
     * @return 清除后的字符串
     */
    static public String trimLeft(String src, String trimStr) {
        if (null == src ||
            src.isEmpty()) {
            return "";
        }

        if (null == trimStr ||
            trimStr.isEmpty()) {
            return src;
        }

        if (src.equals(trimStr)) {
            return "";
        }

        while (src.startsWith(trimStr)) {
            src = src.substring(trimStr.length());
        }

        return src;
    }

    /**
     * 清除源字符串右边的字符串
     *
     * @param src     原字符串
     * @param trimStr 需要被清除的字符串
     * @return 清除后的字符串
     */
    static public String trimRight(String src, String trimStr) {
        if (null == src ||
            src.isEmpty()) {
            return "";
        }

        if (null == trimStr ||
            trimStr.isEmpty()) {
            return src;
        }

        if (src.equals(trimStr)) {
            return "";
        }

        while (src.endsWith(trimStr)) {
            src = src.substring(0, src.length() - trimStr.length());
        }

        return src;
    }

    /**
     * 字符串是否为空 ?
     *
     * @param value 字符串值
     * @return true = 为空, false = 不为空;
     */
    static public boolean isNullOrEmpty(String value) {
        return null == value || value.isEmpty();
    }

    /**
     * 字符串不为空 ?
     *
     * @param value 字符串值
     * @return true = 不为空, false = 为空
     */
    static public boolean notNullOrEmpty(String value) {
        return !isNullOrEmpty(value);
    }

    /**
     * 将字符串中的单引号 ' 替换成斜杠 + 单引号 \'
     *
     * @param src 原字符串
     * @return 替换后的字符串
     */
    static public String addSlash(String src) {
        if (null == src ||
            src.isEmpty()) {
            return src;
        } else {
            return src.replaceAll("\'", "\\\'");
        }
    }

    /**
     * 获取子字符串
     *
     * @param src       原字符串
     * @param fromIndex 起始位置, 当该值小于 0 时, 则说明从右侧起算, 向左数 N 个位置
     * @return 子字符串
     */
    static public String subString(String src, int fromIndex) {
        if (null == src ||
            src.isEmpty()) {
            return src;
        }

        if (fromIndex == 0) {
            return src;
        }

        if (fromIndex > 0) {
            if (fromIndex >= src.length()) {
                return "";
            } else {
                return src.substring(fromIndex);
            }
        } else /* if (fromIndex < 0) */ {
            // 重新计算起始位置
            fromIndex = src.length() + fromIndex;

            if (fromIndex <= 0) {
                return src;
            } else {
                return src.substring(fromIndex);
            }
        }
    }

    /**
     * 将字符串切分为 int 数组
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return int 数组
     */
    static public Integer[] splitToInt32Array(String src, String regex) {
        if (null == src || src.isEmpty()) {
            return new Integer[0];
        }

        if (null == regex || regex.isEmpty()) {
            return new Integer[] {
                Integer.parseInt(src)
            };
        }

        return Arrays.stream(src.split(regex))
            .map(String::trim)
            .map(Integer::valueOf)
            .toArray(Integer[]::new);
    }

    /**
     * 将字符串切分为 int 数值数组
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return int 数值数组
     */
    static public int[] splitToIntValArray(String src, String regex) {
        return ArrayUtil.unboxing(
            splitToInt32Array(src, regex)
        );
    }

    /**
     * 将字符串切分为 int 列表
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return int 列表
     */
    static public List<Integer> splitToInt32List(String src, String regex) {
        return Arrays.asList(
            StringUtil.splitToInt32Array(src, regex)
        );
    }

    /**
     * 将字符串切分为 int 集合
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return int 集合
     */
    static public Set<Integer> splitToInt32Set(String src, String regex) {
        return new HashSet<>(
            StringUtil.splitToInt32List(src, regex)
        );
    }

    /**
     * 将字符串切分为 long 数组
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return int 数组
     */
    static public Long[] splitToInt64Array(String src, String regex) {
        if (null == src || src.isEmpty()) {
            return new Long[0];
        }

        if (null == regex || regex.isEmpty()) {
            return new Long[] {
                Long.parseLong(src)
            };
        }

        return Arrays.stream(src.split(regex))
            .map(String::trim)
            .map(Long::valueOf)
            .toArray(Long[]::new);
    }

    /**
     * 将字符串切分为 long 数值数组
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return long 数值数组
     */
    static public long[] splitToLongValArray(String src, String regex) {
        return ArrayUtil.unboxing(
            splitToInt64Array(src, regex)
        );
    }

    /**
     * 将字符串切分为 long 列表
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return long 列表
     */
    static public List<Long> splitToInt64List(String src, String regex) {
        return Arrays.asList(
            StringUtil.splitToInt64Array(src, regex)
        );
    }

    /**
     * 将字符串切分为 long 集合
     *
     * @param src   原字符串
     * @param regex 分隔字符串
     * @return long 集合
     */
    static public Set<Long> splitToInt64Set(String src, String regex) {
        return new HashSet<>(
            StringUtil.splitToInt64List(src, regex)
        );
    }
}
