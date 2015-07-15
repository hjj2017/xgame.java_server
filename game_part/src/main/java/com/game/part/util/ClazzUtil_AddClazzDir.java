package com.game.part.util;

import com.game.part.GameError;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 将 class 目录添加到 classpath 中
 *
 * @author hjj2017
 * @since 2015/7/15
 *
 */
final class ClazzUtil_AddClazzDir {
    /**
     * 类默认构造器
     *
     */
    private ClazzUtil_AddClazzDir() {
    }

    /**
     * 将 class 目录添加到 classpath 中
     *
     * @param fromDir
     *
     */
    public static void process(String fromDir) {
        // 获取类库路径
        File clazzPath = new File(fromDir);

        if (!clazzPath.exists() ||
            !clazzPath.isDirectory()) {
            // 如果不存在或者不是目录,
            // 则抛出异常!
            throw new GameError(MessageFormat.format(
                "{0} 不存在或不是目录",
                fromDir
            ));
        }

        try {
            // 获取 addURL 方法
            final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            // 获取该方法的访问权限
            boolean oldAccessible = method.isAccessible();

            if (!oldAccessible) {
                // 设置为可访问
                method.setAccessible(true);
            }

            // 设置类加载器
            URLClassLoader cl = (URLClassLoader)ClassLoader.getSystemClassLoader();
            // 调用 addURL 添加类路径
            method.invoke(
                cl, clazzPath.toURI().toURL()
            );

            // 还原 addURL 的访问权限
            method.setAccessible(oldAccessible);
        } catch (Exception ex) {
            // 如果遇到异常,
            // 直接抛出!
            throw new GameError(
                ex.getMessage(), ex
            );
        }
    }
}
