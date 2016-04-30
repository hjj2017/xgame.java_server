package com.game.gameServer.queued;

import com.game.part.GameError;

/**
 * 队列错误
 *
 * @author hjj2017
 * @since 2016/04/30
 *
 */
public class QueuedError extends GameError {
    /**
     * 类默认构造器
     *
     */
    public QueuedError() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param msg
     * @param err
     */
    public QueuedError(String msg, Throwable err) {
        super(msg, err);
    }

    /**
     * 类参数构造器
     *
     * @param err
     */
    public QueuedError(Throwable err) {
        super(err);
    }
}
