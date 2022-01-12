package org.xgame.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类
 */
public final class CommLog {
    /**
     * 日志对象
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MainThreadProcessor.class);

    /**
     * 私有化类默认构造器
     */
    private CommLog() {
    }
}
