package com.game.part.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可重用的单例对象
 * 
 * @author hjj2019
 *
 */
public class ThreadLocalObjectPool {
    /** 类字典 */
    private static final Map<Class<?>, ThreadLocal<?>> CLAZZ_MAP = new ConcurrentHashMap<>();

    /**
     * 获取类对象, <font color="#990000">注意 : 该类对象必须提供默认构造器</font>
     *
     * @param clazz
     * @return
     *
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }

        ThreadLocal<T> tl = (ThreadLocal<T>)CLAZZ_MAP.get(clazz);

        if (tl != null) {
            return tl.get();
        }

        synchronized (clazz) {
            // 重新获取本地线程对象
            tl = (ThreadLocal<T>)CLAZZ_MAP.get(clazz);

            if (tl == null) {
                // 创建线程本地对象
                tl = newThreadLocal(clazz);
                // 并加入到字典
                Object oldObj = CLAZZ_MAP.putIfAbsent(clazz, tl);

                if (oldObj != null) {
                    return (T)oldObj;
                }
            }
        }

        return tl.get();
    }

    /**
     * 创建线程本地对象
     *
     * @param clazz
     * @return
     *
     */
    private static <T> ThreadLocal<T> newThreadLocal(final Class<T> clazz) {
        if (clazz == null) {
            return null;
        }

        // 创建新的线程本地对象
        return new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                // 创建并返回新对象
                return newObj(clazz);
            }
        };
    }

    /**
     * 创建对象
     *
     * @param clazz
     * @return
     *
     */
    private static <T> T newObj(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }

        try {
            // 返回新对象
            return clazz.newInstance();
        } catch (Exception ex) {
            // 抛出异常
            throw new RuntimeException(ex);
        }
    }
}
