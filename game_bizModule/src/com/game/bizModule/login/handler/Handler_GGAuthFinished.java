package com.game.bizModule.login.handler;

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
        // 给客户端发消息
        this.sendMsgToClient(
            new GCLogin(true), msgObj._p
        );
    }
}
