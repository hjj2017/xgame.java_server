package com.game.bizModule.login.handler;

import com.game.bizModule.login.LoginStateTable;
import com.game.bizModule.login.msg.GCLogin;
import com.game.bizModule.login.msg.GGAuthFinished;
import com.game.gameServer.msg.AbstractGGMsgHandler;

/**
 * 验证完成
 *
 * @author hjj2017
 * @since 2015/7/10
 *
 */
public class Handler_GGAuthFinished extends AbstractGGMsgHandler<GGAuthFinished> {
    @Override
    public void handle(GGAuthFinished msgObj) {
        if (msgObj == null ||
            msgObj._p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取登陆状态表
        LoginStateTable stateTbl = msgObj._p.getPropValOrCreate(LoginStateTable.class);
        // 登陆授权完成!
        stateTbl._authOk = true;

        // 给客户端发消息
        this.sendMsgToClient(
            new GCLogin(msgObj._ok), msgObj._p
        );
    }
}
