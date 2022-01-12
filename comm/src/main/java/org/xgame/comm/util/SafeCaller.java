package org.xgame.comm.util;

import org.slf4j.Logger;
import org.xgame.comm.CommLog;

import java.util.concurrent.Callable;

/**
 * 安全运行
 *
 * @param <T> 模板参数 -- 返回值类型
 */
public final class SafeCaller<T> implements Callable<T> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 内置运行实例
     */
    private final Callable<T> _innerC;

    /**
     * 类参数构造器
     *
     * @param innerC 内置运行实例
     */
    public SafeCaller(Callable<T> innerC) {
        _innerC = innerC;
    }

    @Override
    public T call() {
        if (null == _innerC) {
            return null;
        }

        try {
            // 运行
            return _innerC.call();
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }
}
