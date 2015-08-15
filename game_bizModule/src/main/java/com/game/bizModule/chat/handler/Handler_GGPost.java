package com.game.bizModule.chat.handler;

import java.util.List;

import com.game.bizModule.chat.msg.GCCommChat;
import com.game.bizModule.chat.msg.GGPost;
import com.game.bizModule.human.Human;
import com.game.gameServer.msg.AbstractGGMsgHandler;

/**
 * GG 消息处理器
 *
 */
public class Handler_GGPost extends AbstractGGMsgHandler<GGPost> {
    @Override
    public void handle(GGPost msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取 GC 消息
        final GCCommChat gcMSG = msgObj._gcMSG;
        // 获取角色 UId 列表
        final List<Long> humanUIdList = msgObj._humanUIdList;

        // 获取角色列表
        List<Human> humanList = Human.getHumanList(humanUIdList);

        if (humanList == null ||
            humanList.isEmpty()) {
            // 如果角色列表为空,
            // 则直接退出!
            return;
        }

        // 给玩家发送消息
        humanList.forEach(h -> this.sendMsgToClient(
            gcMSG, h.getPlayer()
        ));
    }
}
