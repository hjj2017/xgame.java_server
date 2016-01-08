package com.game.part.util;

/**
 * 业务结果对象池, 所有的业务返回结果, 从对象池里借出即可, 不需要自己 new!
 * 
 * @author hjj2019
 * @since 2014/6/23
 * 
 */
public class BizResultPool {
    /**
     * 取得从对象池中返回的对象
     *
     * @param clazz
     * @return
     *
     */
    public static <T extends BizResultObj> T borrow(Class<T> clazz) {
        // 断言参数不为空
        Assert.notNull(clazz, "clazz");
        // 获取线程本地对象并清理内容
        T obj = ThreadLocalObjectPool.get(clazz);
        obj.clear();

        return obj;
    }
}
