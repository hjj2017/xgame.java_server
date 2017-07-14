package com.game.part.redis;

/**
 * 命令接收器
 */
@FunctionalInterface
public interface ICmdAcceptor {
    /**
     * 接收命令
     *
     * @param cmdObj 命令对象
     */
    void accept(AbstractSubscribeCmd cmdObj);
}