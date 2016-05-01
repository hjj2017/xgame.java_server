package com.game.part.queued;

/**
 * 消息执行调用者
 *
 * @author hjj2019
 * @since 2016/05/01
 */
public interface IMsgExeCaller {
    /**
     * 调用消息对象的 execute 函数
     *
     * @param queuedMsg
     * @see AbstractQueuedMsg#execute()
     */
    void callExec(AbstractQueuedMsg queuedMsg);
}
