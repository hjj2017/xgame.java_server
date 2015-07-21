package com.game.bizModule.human.handler;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.msg.GCEnterHuman;
import com.game.bizModule.human.msg.GGLoadHumanFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractGGMsgHandler;

/**
 * 查询玩家角色入口列表完成
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class Handler_GGLoadHumanFinish extends AbstractGGMsgHandler<GGLoadHumanFinish> {
    @Override
    public void handle(GGLoadHumanFinish ggMSG) {
        if (ggMSG == null ||
            ggMSG._p == null ||
            ggMSG._h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取玩家和角色
        final Player p = ggMSG._p;
        final Human h = ggMSG._h;

        // 建立玩家和角色之间的关联
        h.bindPlayer(p);
        p.putPropVal(Human.class, h);

        // 获取角色状态表
        HumanStateTable hStateTbl = p.getPropValOrCreate(HumanStateTable.class);
        // 更新状态值
        hStateTbl._execEnterHuman = false;
        hStateTbl._humanLoadFromDbOk = true;

        // 发送 GC 消息
        this.sendMsgToClient(
            new GCEnterHuman(ggMSG._finish), p
        );
    }
}
