package com.game.bizModule.human.handler;

import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.msg.CGCreateHuman;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 创建角色消息处理器
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class Handler_CGCreateHuman extends AbstractCGMsgHandler<CGCreateHuman> {
    @Override
    public void handle(CGCreateHuman msgObj) {
        // 异步方式创建角色
        HumanServ.OBJ.asyncCreateHuman(
            this.getPlayer(),
            msgObj._serverName.getStrVal(),
            msgObj._humanName.getStrVal(),
            msgObj._humanTmplId.getIntVal()
        );
    }
}
