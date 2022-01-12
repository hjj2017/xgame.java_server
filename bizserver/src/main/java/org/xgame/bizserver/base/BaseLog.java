package org.xgame.bizserver.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类
 */
public final class BaseLog {
    /**
     * 日志对象
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(BaseLog.class);

    /**
     * 私有化类默认构造器
     */
    private BaseLog() {
    }
}
