package com.game.gameServer.msg.netty;

import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import com.game.gameServer.framework.Player;
import com.game.part.msg.MsgLog;

/**
 * Netty 上下文管理器, 相当于会话管理器
 *
 * @author hjj2017
 * @since 2016/1/9
 *
 */
public final class CtxManager {
    /** 单例对象 */
    public static final CtxManager OBJ = new CtxManager();
    /** CTX_UID Key */
    private static final AttributeKey<Long> CTX_UID = AttributeKey.newInstance("CTX_UID");
    /** CTX_PLAYER Key */
    private static final AttributeKey<Player> CTX_PLAYER = AttributeKey.newInstance("CTX_PLAYER");

    /** 计数器, 专门用来生成 UID */
    private final AtomicLong _counter = new AtomicLong(0);
    /** 上下文字典 */
    private final ConcurrentHashMap<Long, ChannelHandlerContext> _ctxMap = new ConcurrentHashMap<>();
    /** 平台 UId =>  CtxUId 字典 */
    private final ConcurrentHashMap<String, Long> _platformUIdStrToCtxUIdMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     *
     */
    private CtxManager() {
    }

    /**
     * 添加会话
     *
     * @param ctx Netty 上下文对象, 相当于会话 ( Session )
     *
     */
    public void addCtx(ChannelHandlerContext ctx) {
        if (ctx == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 新建并设置 UId
        Long newUId = this._counter.incrementAndGet();
        Long oldUId = ctx.attr(CTX_UID).setIfAbsent(newUId);

        if (oldUId != null) {
            newUId = oldUId;
        }

        this._ctxMap.put(newUId, ctx);
    }

    /**
     * 获取会话 UId
     *
     * @param ctx Netty 上下文对象, 相当于会话 ( Session )
     * @return 返回会话 UId
     *
     */
    public long getUId(ChannelHandlerContext ctx) {
        if (ctx != null) {
            // 获取 CTX_UID
            Long val = ctx.attr(CTX_UID).get();

            if (val == null) {
                return 0;
            } else {
                return val;
            }
        } else {
            return 0;
        }
    }

    /**
     * 根据会话 UId 获取会话对象
     *
     * @param ctxUId 会话 UId
     * @return 会话对象
     *
     */
    public ChannelHandlerContext getCtxByUId(long ctxUId) {
        if (ctxUId <= 0) {
            return null;
        } else {
            return this._ctxMap.get(ctxUId);
        }
    }

    /**
     * 将玩家绑定到会话,
     * 同时修改玩家的会话 UId 和最后登录 IP 地址
     *
     * @param p 玩家
     * @param ctxUId 会话 UId
     *
     */
    public void bindPlayerToCtx(Player p, long ctxUId) {
        if (p == null ||
            ctxUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            MsgLog.LOG.error("参数对象为空");
            return;
        }

        // 获取会话对象
        ChannelHandlerContext ctx = this.getCtxByUId(ctxUId);

        if (ctx == null) {
            // 如果未找到会话 Id,
            // 则直接退出!
            MsgLog.LOG.error(MessageFormat.format(
                "未找到上下文对象, ctxUId = {0}",
                String.valueOf(ctxUId)
            ));
            return;
        }

        // 设置会话 UId
        p._ctxUId = ctxUId;
        // 获取 IP 地址
        InetSocketAddress ipAddr = (InetSocketAddress)ctx.channel().remoteAddress();

        if (ipAddr != null &&
            ipAddr.getAddress() != null) {
            // 设置登陆 IP 地址和时间
            p._loginIpAddr = ipAddr.getAddress().getHostAddress();
            p._loginTime = System.currentTimeMillis();
        }

        // 将玩家对象存入会话对象
        ctx.attr(CTX_PLAYER).setIfAbsent(p);
        // 管家玩家 ID 和 会话 ID
        this._platformUIdStrToCtxUIdMap.put(p._platformUIdStr, ctxUId);
    }

    /**
     * 取消会话与 Player 的关联
     *
     * @param ctxUId 会话 UId
     *
     */
    public void unbindPlayerFromCtx(long ctxUId) {
        if (ctxUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取会话对象
        ChannelHandlerContext ctx = this.getCtxByUId(ctxUId);

        if (ctx == null) {
            // 如果未找到会话对象,
            // 则直接退出!
            return;
        }

        // 获取玩家对象
        Player p = ctx.attr(CTX_PLAYER).getAndRemove();

        if (p != null) {
            // 如果玩家对象不为空,
            // 则取消玩家平台 UId 字符串与会话 UId 的关联关系!
            this._platformUIdStrToCtxUIdMap.remove(p._platformUIdStr);
        }
    }

    /**
     * 根据平台 UId 获取 IO 会话对象
     *
     * @param platformUIdStr 平台 UId 字符串
     * @return 会话对象
     *
     */
    public ChannelHandlerContext getCtxByPlatformUIdStr(String platformUIdStr) {
        if (platformUIdStr == null ||
            platformUIdStr.isEmpty()) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取会话 UId
        Long ctxUId = this._platformUIdStrToCtxUIdMap.get(platformUIdStr);

        if (ctxUId == null ||
            ctxUId <= 0) {
            return null;
        }

        // 获取 IO 会话对象
        ChannelHandlerContext ctx = this._ctxMap.get(ctxUId);

        if (ctx == null) {
            //
            // 如果 IO 会话对象为空,
            // 则取平台 UId 与会话 UId 的关联关系!
            // 注意: 一定是先有会话, 然后才有的 Player...
            // 如果会话已经不存在了,
            // 那么 Player 也必然不存在!
            //
            this._platformUIdStrToCtxUIdMap.remove(platformUIdStr);
        }

        return ctx;
    }

    /**
     * 根据会话 UId 获取玩家对象
     *
     * @param ctxUId 会话 UId
     * @return 玩家
     *
     */
    public Player getPlayerByCtxUId(long ctxUId) {
        if (ctxUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取会话对象
        ChannelHandlerContext ctx = this.getCtxByUId(ctxUId);

        if (ctx == null) {
            return null;
        } else {
            return ctx.attr(CTX_PLAYER).get();
        }
    }

    /**
     * 获取会话 UId 集合
     *
     * @return 会话 UId 集合
     *
     */
    public Set<Long> getCtxUIdSet() {
        return this._ctxMap.keySet();
    }
}
