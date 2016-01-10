package com.game.gameServer.msg.netty;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;

import com.game.gameServer.framework.Player;

/**
 * IO 会话
 *
 * @author 2017
 * @since 2016/1/10
 *
 */
public final class IoSession {
    /** 计数器, 用于生成会话 UId */
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    /** 会话 UId */
    private final long _UId;
    /** Netty 信道 */
    private Channel _ch;
    /** 玩家对象 */
    private Player _p = null;

    /**
     * 类参数构造器
     *
     * @param ch Netty 信道
     *
     */
    IoSession(Channel ch) {
        // 断言参数不为空
        assert ch != null : "null ch";
        // 设置 UId 和信道
        this._UId = COUNTER.incrementAndGet();
        this._ch = ch;
    }

    /**
     * 获取 UId
     *
     * @return
     *
     */
    public long getUId() {
        return this._UId;
    }

    /**
     * 获取 Netty 信道
     *
     * @return
     *
     */
    public Channel getChannel() {
        return this._ch;
    }

    /**
     * 获取玩家对象
     *
     * @return
     *
     */
    public Player getPlayer() {
        return this._p;
    }
    /**
     * 写出消息对象
     *
     * @param obj
     *
     */
    public void writeAndFlush(Object obj) {
        if (obj != null) {
            this._ch.writeAndFlush(obj);
        }
    }

    /**
     * 绑定玩家对象
     *
     * @param p
     * @return 玩家对象
     *
     */
    public Player bindPlayer(Player p) {
        if (p != null) {
            this._p = p;
        }

        return p;
    }

    /**
     * 解除玩家绑定
     *
     * @return 玩家对象
     *
     */
    public Player unbindPlayer() {
        Player tmpP = this._p;
        this._p = null;
        return tmpP;
    }

    /**
     * 释放会话
     *
     */
    public void release() {
        this.unbindPlayer();
        this._ch = null;
    }
}
