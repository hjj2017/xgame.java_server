package org.xgame.comm.db;

import org.xgame.comm.async.AsyncOperationProcessor;

import java.util.function.Function;

/**
 * 数据库代理类
 */
public final class DBAgent {
    /**
     * 单例对象
     */
    private static final DBAgent INSTANCE = new DBAgent();

    /**
     * 私有化类默认构造器
     */
    private DBAgent() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static DBAgent getInstance() {
        return INSTANCE;
    }

    public void execAsync(long bindId, String queryStr, Function<Boolean, Void> callback) {
        // 查询完成之后, 再次扔回到异步 IO 线程
        AsyncOperationProcessor.getInstance().process(
            bindId,
            () -> callback.apply(false)
        );
    }
}
