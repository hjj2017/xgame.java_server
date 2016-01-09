package com.game.gameServer.msg;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import io.netty.channel.ChannelHandlerContext;

import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.netty.CtxManager;
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
            this.sendMsgToClient(msgObj, p._ctxUId);
        }
    }

    /**
     * 发送消息给客户端
     *
     * @param msgObj
     * @param toCtxUId
     *
     */
    protected void sendMsgToClient(AbstractGCMsgObj msgObj, long toCtxUId) {
        if (msgObj == null ||
            toCtxUId <= 0L) {
            // 如果消息对象为空,
            // 则直接退出!
            return;
        }

        ChannelHandlerContext ctx = CtxManager.OBJ.getCtxByUId(toCtxUId);

        if (ctx == null) {
            // 如果会话对象为空,
            // 则直接退出!
            MsgLog.LOG.error(MessageFormat.format(
                "会话对象为空, ctxUId = {0}",
                String.valueOf(toCtxUId)
            ));
            return;
        } else {
            ctx.writeAndFlush(msgObj);
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
        Set<Long> ctxUIdSet = CtxManager.OBJ.getCtxUIdSet();

        if (ctxUIdSet == null ||
            ctxUIdSet.size() <= 0) {
            // 如果会话 UId 列表为空,
            // 则直接退出!
            return;
        }

        ctxUIdSet.forEach(ctxUId -> {
            if (ctxUId != null &&
                ctxUId > 0L) {
                // 发送消息
                this.sendMsgToClient(msgObj, ctxUId);
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
     * @param toCtxUIdArr
     *
     */
    protected void broadcast(AbstractGCMsgObj msgObj, long[] toCtxUIdArr) {
        if (msgObj == null ||
            toCtxUIdArr == null ||
            toCtxUIdArr.length <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        for (long ctxUId : toCtxUIdArr) {
            if (ctxUId > 0L) {
                // 发送消息
                this.sendMsgToClient(msgObj, ctxUId);
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
            this.disconnect(p._ctxUId);
        }
    }

    /**
     * 断开会话
     *
     * @param ctxUId
     *
     */
    protected void disconnect(long ctxUId) {
        if (ctxUId <= 0L) {
            // 如果会话 UId 为空,
            // 则直接退出!
            return;
        }

        // 获取会话对象
        ChannelHandlerContext ctx = CtxManager.OBJ.getCtxByUId(ctxUId);

        if (ctx == null) {
            // 如果会话对象为空,
            // 则直接退出!
            return;
        } else {
            // 断开连接
            ctx.disconnect();
            ctx.close();
        }
    }

    /**
     * 根据会话 UId 获取玩家对象
     *
     * @param ctxUId
     * @return
     *
     */
    protected Player getPlayerByCtxUId(long ctxUId) {
        if (ctxUId <= 0L) {
            // 如果会话 UId 为空,
            // 则直接退出!
            return null;
        } else {
            // 获取玩家对象
            return CtxManager.OBJ.getPlayerByCtxUId(ctxUId);
        }
    }
}
