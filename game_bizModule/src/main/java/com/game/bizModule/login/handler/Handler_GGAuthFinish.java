package com.game.bizModule.login.handler;

import com.game.bizModule.login.LoginStateTable;
import com.game.bizModule.login.msg.GCLogin;
import com.game.bizModule.login.msg.GGAuthFinish;
import com.game.bizModule.login.queued.LoginQueuedMsg;
import com.game.gameServer.msg.AbstractGGMsgHandler;
import com.game.part.queued.MsgQueue;

/**
 * 验证完成
 *
 * @author hjj2017
 * @since 2015/7/10
 *
 */
public class Handler_GGAuthFinish extends AbstractGGMsgHandler<GGAuthFinish> {
    @Override
    public void handle(GGAuthFinish msgObj) {
        if (msgObj == null ||
            msgObj._p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取登陆状态表
        LoginStateTable stateTbl = msgObj._p.getPropValOrCreate(LoginStateTable.class);
        // 结束授权过程
        stateTbl._execAuth = false;
        // 如果授权成功, 则标志登陆完成!
        stateTbl._loginFinished = msgObj._success;

        // 给客户端发消息
        this.sendMsgToClient(
            new GCLogin(msgObj._success), msgObj._p
        );

        LoginQueuedMsg qMsg = new LoginQueuedMsg();
        qMsg._playerName = msgObj._p._userName;
        MsgQueue.OBJ.sendPublicMsg(qMsg);
    }
}
