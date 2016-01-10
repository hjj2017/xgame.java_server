package com.game.gameServer.msg;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import io.netty.channel.ChannelHandlerContext;

import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.netty.IoSession;
import com.game.gameServer.msg.netty.IoSessionManager;
import com.game.part.msg.MsgLog;

/**
 * 可执行消息的处理器
 *
 * @author hjj2017
 * @since 2015/7/10
 *
 */
abstract class AbstractExecutableMsgHandler<TMsgObj extends AbstractExecutableMsgObj> {
    /**
     * 处理消息对象
     *
     * @param msgObj
     *
     */
    public abstract void handle(TMsgObj msgObj);

    /**
     * 发送消息给客户端
     *
     * @param msgObj
     * @param p
     *
     */
    protected void sendMsgToClient(AbstractGCMsgObj msgObj, Player p) {
        if (msgObj == null ||
            p == null) {
            // 如果消息对象为空,
            // 则直接退出!
            return;
        } else {
            // 发送消息给客户端
            this.sendMsgToClient(msgObj, p._sessionUId);
        }
    }

    /**
     * 发送消息给客户端
     *
     * @param msgObj 消息对象
     * @param toSessionUId 会话 UId
     *
     */
    protected void sendMsgToClient(AbstractGCMsgObj msgObj, long toSessionUId) {
        if (msgObj == null ||
            toSessionUId <= 0L) {
            // 如果消息对象为空,
            // 则直接退出!
            return;
        }

        IoSession sessionObj = IoSessionManager.OBJ.getSessionByUId(toSessionUId);

        if (sessionObj == null) {
            // 如果会话对象为空,
            // 则直接退出!
            MsgLog.LOG.error(MessageFormat.format(
                "会话对象为空, sessionUId = {0}",
                String.valueOf(toSessionUId)
            ));
            return;
        } else {
            sessionObj.writeAndFlush(msgObj);
        }
    }

    /**
     * 给所有在线玩家广播消息
     *
     * @param msgObj
     *
     */
    protected void broadcast(AbstractGCMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取会话 UId 集合
        Set<Long> sessionUIdSet = IoSessionManager.OBJ.getSessionUIdSet();

        if (sessionUIdSet == null ||
            sessionUIdSet.size() <= 0) {
            // 如果会话 UId 列表为空,
            // 则直接退出!
            return;
        }

        sessionUIdSet.forEach(sessionUId -> {
            if (sessionUId != null &&
                sessionUId > 0L) {
                // 发送消息
                this.sendMsgToClient(msgObj, sessionUId);
            }
        });
    }

    /**
     * 给所有在线的玩家广播消息
     *
     * @param msgObj
     * @param pl
     *
     */
    protected void broadcast(AbstractGCMsgObj msgObj, List<Player> pl) {
        if (msgObj == null ||
            pl == null ||
            pl.size() <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        pl.forEach(p -> {
            if (p != null) {
                this.sendMsgToClient(msgObj, p);
            }
        });
    }

    /**
     * 给所有在线玩家广播消息
     *
     * @param msgObj
     * @param toSessionUIdArr
     *
     */
    protected void broadcast(AbstractGCMsgObj msgObj, long[] toSessionUIdArr) {
        if (msgObj == null ||
            toSessionUIdArr == null ||
            toSessionUIdArr.length <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        for (long sessionUId : toSessionUIdArr) {
            if (sessionUId > 0L) {
                // 发送消息
                this.sendMsgToClient(msgObj, sessionUId);
            }
        }
    }

    /**
     * 断开会话
     *
     * @param p
     *
     */
    protected void disconnect(Player p) {
        if (p != null) {
            this.disconnect(p._sessionUId);
        }
    }

    /**
     * 断开会话
     *
     * @param sessionUId
     *
     */
    protected void disconnect(long sessionUId) {
        if (sessionUId <= 0L) {
            // 如果会话 UId 为空,
            // 则直接退出!
            return;
        }

        // 获取会话对象
        IoSession sessionObj = IoSessionManager.OBJ.getSessionByUId(sessionUId);

        if (sessionObj == null) {
            // 如果会话对象为空,
            // 则直接退出!
            return;
        } else {
            // 断开连接
            sessionObj.getChannel().close();
        }
    }

    /**
     * 根据会话 UId 获取玩家对象
     *
     * @param sessionUId
     * @return
     *
     */
    protected Player getPlayerBySessionUId(long sessionUId) {
        if (sessionUId <= 0L) {
            // 如果会话 UId 为空,
            // 则直接退出!
            return null;
        } else {
            // 获取玩家对象
            return IoSessionManager.OBJ.getPlayerBySessionUId(sessionUId);
        }
    }
}
