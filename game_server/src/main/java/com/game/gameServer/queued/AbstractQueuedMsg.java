package com.game.gameServer.queued;

/**
 * 抽象的队列消息
 *
 * @author hjj2017
 * @since 2016/04/30
 */
public abstract class AbstractQueuedMsg {
    /**
     * 执行消息
     *
     */
    public abstract void execute();
}
