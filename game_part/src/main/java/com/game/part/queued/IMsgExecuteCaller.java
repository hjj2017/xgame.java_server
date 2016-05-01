package com.game.part.queued;

/**
 * 消息执行呼叫者
 *
 * @author hjj2019
 * @since 2016/05/01
 */
public interface IMsgExecuteCaller {
    /**
     * 调用消息对象的 execute 函数
     *
     * @param queuedMsg
     */
    void callExec(AbstractQueuedMsg queuedMsg);
}
