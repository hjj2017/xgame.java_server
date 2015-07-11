package com.game.bizModule.human.handler;

import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.msg.GGQueryHumanEntryListOk;
import com.game.gameServer.msg.AbstractGGMsgHandler;

/**
 * 查询玩家角色入口列表完成
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class Handler_GGQueryHumanEntryListOk extends AbstractGGMsgHandler<GGQueryHumanEntryListOk> {
    @Override
    public void handle(GGQueryHumanEntryListOk msgObj) {
        if (msgObj == null ||
            msgObj._p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取玩家角色状态表
        HumanStateTable stateTbl = msgObj._p.getPropValOrCreate(HumanStateTable.class);

        if (msgObj._heel != null &&
            msgObj._heel.isEmpty() == false) {
            // 如果角色入口列表不为空,
            // 则修改状态表
            stateTbl._hasHuman = true;
        } else {
            // 没有角色!
            stateTbl._hasHuman = false;
        }


    }
}
