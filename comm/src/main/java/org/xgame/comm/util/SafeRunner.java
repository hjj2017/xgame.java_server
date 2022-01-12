package org.xgame.comm.util;

import org.slf4j.Logger;
import org.xgame.comm.CommLog;

/**
 * 安全运行者
 */
public final class SafeRunner implements Runnable {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 内置运行实例
     */
    private final Runnable _innerR;

    /**
     * 类参数构造器
     *
     * @param innerR 内置运行实例
     */
    public SafeRunner(Runnable innerR) {
        _innerR = innerR;
    }

    @Override
    public void run() {
        if (null == _innerR) {
            return;
        }

        try {
            // 运行
            _innerR.run();
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
