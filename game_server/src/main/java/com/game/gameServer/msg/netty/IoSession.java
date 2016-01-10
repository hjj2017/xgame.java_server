package com.game.gameServer.msg.netty;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;

import com.game.gameServer.framework.Player;
import com.game.part.msg.MsgError;

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
    /** Netty 信道软引用 */
    private WeakReference<Channel> _chRef;
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
        this._chRef = new WeakReference<>(ch);
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
        return this._chRef == null ? null : this._chRef.get();
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
        if (obj == null ||
            this._chRef == null) {
            return;
        }

        // 获取 Netty 信道
        Channel ch = this._chRef.get();

        if (ch != null) {
            ch.writeAndFlush(obj);
        }
    }

    /**
     * 绑定玩家对象
     *
     * @param p
     * @return 玩家对象
     *
     */
    public Player attachPlayer(Player p) {
        if (p == null) {
            this._p = p;
        } else/* if (p != null) */{
            if (this._p == null) {
                this._p = p;
            } else if (this._p != p) {
                throw new MsgError("已有绑定玩家");
            }
        }

        return p;
    }
}
