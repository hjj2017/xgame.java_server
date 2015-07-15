package com.game.part.util;

import com.game.part.GameError;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;

/**
 * 扫描 jar 文件添加到 classpath 中
 *
 * @author hjj2017
 * @since 2015/7/15
 *
 */
final class ClazzUtil_ScanAllJar {
    /**
     * 类默认构造器
     *
     */
    private ClazzUtil_ScanAllJar() {
    }

    /**
     * 从指定目录中扫描 jar 文件添加到 classpath 中
     *
     * @param fromDir
     *
     */
    static void process(String fromDir) {
        // 获取类库路径
        File libPath = new File(fromDir);

        if (!libPath.exists() ||
            !libPath.isDirectory()) {
            // 如果不存在或者不是目录,
            // 则抛出异常!
            throw new GameError(MessageFormat.format(
                "{0} 不存在或不是目录",
                fromDir
            ));
        }

        // 获取所有的 jar 文件
        File[] jarFileArr = libPath.listFiles((dir, name) -> {
            return name.endsWith(".jar");
        });

        if (jarFileArr == null ||
            jarFileArr.length <= 0) {
            // 如果文件数组为空,
            // 则直接退出!
            return;
        }

        try {
            // 获取 addURL 方法
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            // 获取该方法的权限
            boolean oldAccessible = method.isAccessible();

            if (!oldAccessible) {
                // 设置为可访问
                method.setAccessible(true);
            }

            // 获取系统类加载器
            URLClassLoader cl = (URLClassLoader)ClassLoader.getSystemClassLoader();

            for (File jarFile : jarFileArr) {
                // 获取 JAR 文件的 URL
                URL url = jarFile.toURI().toURL();
                // 调用 addURL 添加到类加载器
                method.invoke(cl, url);
            }

            // 还原 addURL 方法权限
            method.setAccessible(oldAccessible);
        }catch (Exception ex) {
            // 如果遇到异常,
            // 直接抛出!
            throw new GameError(
                ex.getMessage(), ex
            );
        }
    }
}
