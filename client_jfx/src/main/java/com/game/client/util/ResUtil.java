package com.game.client.util;

import java.net.URL;

import com.game.part.BadArgError;
import com.game.part.util.StringUtil;

/**
 * 资源实用工具
 *
 * @author hjj2017
 * @since 2016/2/20
 *
 */
public final class ResUtil {
    /**
     * 类默认构造器
     *
     */
    private ResUtil() {
    }

    /**
     * 获取本地文件路径
     *
     * @param resUrl
     * @return
     *
     */
    public static String getLocalFilePath(String resUrl) {
        if (StringUtil.isNullOrEmpty(resUrl)) {
            // 如果参数对象为空,
            // 则抛出异常!
            throw new BadArgError("null resUrl");
        }

        // 获取资源 URL
        URL url = Thread.currentThread().getContextClassLoader().getResource(resUrl);

        if (url == null) {
            // 如果 URL 不存在,
            // 则抛出异常!
            throw new RuntimeException("未找到资源 : " + resUrl);
        }

        return "file://" + url.getPath();
    }
}
