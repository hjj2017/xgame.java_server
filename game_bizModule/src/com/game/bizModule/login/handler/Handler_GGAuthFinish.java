package com.game.bizModule.login.handler;

import com.game.bizModule.login.LoginStateTable;
import com.game.bizModule.login.msg.GCLogin;
import com.game.bizModule.login.msg.GGAuthFinish;
import com.game.gameServer.msg.AbstractGGMsgHandler;

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
        // 结束授权过程, 授权是否成功?
        stateTbl._execAuth = false;
        stateTbl._authSuccess = msgObj._success;

        // 给客户端发消息
        this.sendMsgToClient(
            new GCLogin(msgObj._success), msgObj._p
        );
    }
}
