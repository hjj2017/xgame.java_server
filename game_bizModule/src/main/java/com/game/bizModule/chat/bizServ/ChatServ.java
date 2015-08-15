package com.game.bizModule.chat.bizServ;

import com.game.bizModule.chat.msg.AbstractCGChat;
import com.game.bizModule.chat.msg.GGType;
import com.game.bizModule.human.Human;
import com.game.part.msg.MsgServ;

/**
 * 聊天服务
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public final class ChatServ {
    /** 单例对象 */
    public static final ChatServ OBJ = new ChatServ();

    /**
     * 类默认构造器
     *
     */
    private ChatServ() {
    }

    /**
     * 给玩家发送聊天消息
     *
     * @param h
     * @param cgMSG
     * @param <TChatMsg>
     *
     */
    public<TChatMsg extends AbstractCGChat> void chat(Human h, TChatMsg cgMSG) {
        if (cgMSG == null) {
            // 如果消息对象为空,
            // 则直接退出!
            return;
        }

        // TODO : 看看玩家在不在禁言列表里?

        // 创建 GG 消息对象
        GGType ggMSG = new GGType();
        ggMSG._h = h;
        ggMSG._cgMSG = cgMSG;
        // 将聊天消息交给 "打字员" 处理
        // 发送 GG 消息
        MsgServ.OBJ.post(ggMSG);
    }
}
