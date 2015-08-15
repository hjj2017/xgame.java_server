package com.game.bizModule.chat.handler;

import com.game.bizModule.chat.bizServ.ChatServ;
import com.game.bizModule.chat.msg.AbstractCGChat;
import com.game.bizModule.human.Human;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 聊天消息处理器, 将处理所有的聊天 CG 消息
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public class Handler_AllCGChat extends AbstractCGMsgHandler<AbstractCGChat> {
    @Override
    public void handle(AbstractCGChat msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取角色
        final Human h = Human.getHumanByPlayer(this.getPlayer());
        // 直接调用聊天服务
        ChatServ.OBJ.chat(h, msgObj);
    }
}
