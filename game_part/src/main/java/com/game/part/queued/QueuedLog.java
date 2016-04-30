package com.game.part.queued;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 队列日志
 *
 * @author hjj2019
 * @since 2016/04/30
 *
 */
public final class QueuedLog {
    /** 单例对象 */
    public static final Logger LOG = LoggerFactory.getLogger("game.queued");

    /**
     * 类默认构造器
     *
     */
    private  QueuedLog() {
    }
}
