package com.game.part.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳日志
 *
 * @author hjj2019
 * @since 2016/05/02
 *
 */
class HeartbeatLog {
    /** 单例对象 */
    static final Logger LOG = LoggerFactory.getLogger("game.heartbeat");

    /**
     * 类默认构造器
     *
     */
    private HeartbeatLog() {
    }
}
