package com.game.gameServer.msg.netty;

import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

import com.game.gameServer.framework.Player;
import com.game.part.msg.MsgLog;

/**
 * 自定义会话管理器
 *
 * @author hjj2017
 * @since 2016/1/9
 *
 */
public final class IoSessionManager {
    /** 单例对象 */
    public static final IoSessionManager OBJ = new IoSessionManager();
    /** 会话字典 */
    private final ConcurrentHashMap<Long, IoSession> _sessionMap = new ConcurrentHashMap<>();
    /** 平台 UId =>  会话 UId 字典 */
    private final ConcurrentHashMap<String, Long> _platformUIdStrToSessionUIdMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     *
     */
    private IoSessionManager() {
    }

    /**
     * 添加会话
     *
     *
     * @param newSession 新建对象
     * @return 新建会话
     * @see MyChannelHandler#channelRegistered(ChannelHandlerContext)
     *
     */
    IoSession addSession(IoSession newSession) {
        if (newSession == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 添加会话到字典
        this._sessionMap.put(
            newSession.getUId(), newSession
        );

        return newSession;
    }

    /**
     * 删除会话
     *
     * @param sessionUId 会话 UId
     * @return 被删除的会话
     *
     */
    public IoSession removeSession(long sessionUId) {
        return this._sessionMap.remove(sessionUId);
    }

    /**
     * 根据会话 UId 获取会话对象
     *
     * @param sessionUId 会话 UId
     * @return 会话对象
     *
     */
    public IoSession getSessionByUId(long sessionUId) {
        if (sessionUId <= 0) {
            return null;
        } else {
            return this._sessionMap.get(sessionUId);
        }
    }

    /**
     * 将玩家绑定到会话,
     * 同时修改玩家的会话 UId 和最后登录 IP 地址
     *
     * @param p 玩家
     * @param sessionUId 会话 UId
     *
     */
    public void bindPlayerToSession(Player p, long sessionUId) {
        if (p == null ||
            sessionUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            MsgLog.LOG.error("参数对象为空");
            return;
        }

        // 获取会话对象
        IoSession sessionObj = this.getSessionByUId(sessionUId);

        if (sessionObj == null) {
            // 如果未找到会话对象,
            // 则直接退出!
            MsgLog.LOG.error(MessageFormat.format(
                "未找到上下文对象, sessionUId = {0}",
                String.valueOf(sessionUId)
            ));
            return;
        }

        // 设置会话 UId
        p._sessionUId = sessionUId;
        // 获取 IP 地址
        InetSocketAddress ipAddr = (InetSocketAddress)sessionObj.getChannel().remoteAddress();

        if (ipAddr != null &&
            ipAddr.getAddress() != null) {
            // 设置登陆 IP 地址和时间
            p._loginIpAddr = ipAddr.getAddress().getHostAddress();
            p._loginTime = System.currentTimeMillis();
        }

        // 将玩家对象存入会话对象
        sessionObj.attachPlayer(p);
        // 管家玩家 ID 和 会话 ID
        this._platformUIdStrToSessionUIdMap.put(p._platformUIdStr, sessionUId);
    }

    /**
     * 取消会话与 Player 的关联
     *
     * @param sessionUId 会话 UId
     *
     */
    public void unbindPlayerFromSession(long sessionUId) {
        if (sessionUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取会话对象
        IoSession sessionObj = this.getSessionByUId(sessionUId);

        if (sessionObj == null) {
            // 如果未找到会话对象,
            // 则直接退出!
            return;
        }

        // 获取玩家并解绑
        Player p = sessionObj.getPlayer();
        sessionObj.attachPlayer(null);

        if (p != null) {
            // 如果玩家对象不为空,
            // 则取消玩家平台 UId 字符串与会话 UId 的关联关系!
            this._platformUIdStrToSessionUIdMap.remove(p._platformUIdStr);
        }
    }

    /**
     * 根据平台 UId 获取会话对象
     *
     * @param platformUIdStr 平台 UId 字符串
     * @return 会话对象
     *
     */
    public IoSession getSessionByPlatformUIdStr(String platformUIdStr) {
        if (platformUIdStr == null ||
            platformUIdStr.isEmpty()) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取会话 UId
        Long sessionUId = this._platformUIdStrToSessionUIdMap.get(platformUIdStr);

        if (sessionUId == null ||
            sessionUId <= 0) {
            return null;
        }

        // 获取 IO 会话对象
        IoSession sessionObj = this._sessionMap.get(sessionUId);

        if (sessionObj == null) {
            //
            // 如果 IO 会话对象为空,
            // 则取平台 UId 与会话 UId 的关联关系!
            // 注意: 一定是先有会话, 然后才有的 Player...
            // 如果会话已经不存在了,
            // 那么 Player 也必然不存在!
            //
            this._platformUIdStrToSessionUIdMap.remove(platformUIdStr);
        }

        return sessionObj;
    }

    /**
     * 根据会话 UId 获取玩家对象
     *
     * @param sessionUId 会话 UId
     * @return 玩家
     *
     */
    public Player getPlayerBySessionUId(long sessionUId) {
        if (sessionUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取会话对象
        IoSession sessionObj = this.getSessionByUId(sessionUId);

        if (sessionObj == null) {
            return null;
        } else {
            return sessionObj.getPlayer();
        }
    }

    /**
     * 获取会话 UId 集合
     *
     * @return 会话 UId 集合
     *
     */
    public Set<Long> getSessionUIdSet() {
        return this._sessionMap.keySet();
    }
}
