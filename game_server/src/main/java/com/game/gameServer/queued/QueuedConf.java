package com.game.gameServer.queued;

/**
 * 队列配置
 *
 */
public class QueuedConf {
    /** 单例对象 */
    public static final QueuedConf OBJ = new QueuedConf();

    /**
     * 类默认构造器
     *
     */
    private QueuedConf() {
    }

    /** BokerUrl */
    public String _bokerUrl = "tcp://localhost:61616";
    /** 私有主题名称 */
    public String _privateDestination = "S00";
}
