package com.game.gameServer.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳日志
 * 
 * @author haijiang
 * @since 2014/5/2
 * 
 */
class HeartbeatLog {
    /** 单例对象 */
    static final Logger LOG = LoggerFactory.getLogger(HeartbeatLog.class);

    /**
     * 类默认构造器
     * 
     */
    private HeartbeatLog() {
    }
}
