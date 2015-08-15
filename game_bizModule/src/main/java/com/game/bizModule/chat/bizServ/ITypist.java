package com.game.bizModule.chat.bizServ;

import java.util.List;

import com.game.bizModule.chat.msg.AbstractCGChat;
import com.game.bizModule.chat.msg.GCChat;
import com.game.bizModule.human.Human;

/**
 * 聊天消息打字员接口,
 * 该接口的主要作用是将 CGChat 消息翻译成 GCChat 消息,
 * 并且提供接收者名单
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public interface ITypist<TCGChat extends AbstractCGChat> {
    /**
     * 打字员开始工作, 即, 将 CG 消息翻译成 GC 消息
     *
     * @param h
     * @param cgMSG
     * @param outHumanUIdList 输出角色 UId 列表, 这些角色将可以收到聊天消息
     * @return
     *
     */
    GCChat type(Human h, TCGChat cgMSG, List<Long> outHumanUIdList);
}
