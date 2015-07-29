package com.game.bizModule.human.handler;

import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.msg.CGEnterHuman;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 进入角色消息处理器
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class Handler_CGEnterHuman extends AbstractCGMsgHandler<CGEnterHuman> {
    @Override
    public void handle(CGEnterHuman msgObj) {
        // 异步方式进入角色
        HumanServ.OBJ.asyncEnterHuman(
            this.getPlayer(), msgObj._humanUId.getLongVal()
        );
    }
}
