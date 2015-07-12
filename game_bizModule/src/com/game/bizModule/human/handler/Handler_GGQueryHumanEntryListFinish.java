package com.game.bizModule.human.handler;

import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.msg.GCQueryHumanEntryList;
import com.game.bizModule.human.msg.GGQueryHumanEntryListFinish;
import com.game.bizModule.human.msg.HumanEntryMO;
import com.game.gameServer.msg.AbstractGGMsgHandler;

/**
 * 查询玩家角色入口列表完成
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class Handler_GGQueryHumanEntryListFinish extends AbstractGGMsgHandler<GGQueryHumanEntryListFinish> {
    @Override
    public void handle(GGQueryHumanEntryListFinish ggMSG) {
        if (ggMSG == null ||
            ggMSG._p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取玩家角色状态表
        HumanStateTable stateTbl = ggMSG._p.getPropValOrCreate(HumanStateTable.class);
        // 执行结束, 更新服务器名称
        stateTbl._execQueryHumanEntryList = false;
        stateTbl._serverName = ggMSG._serverName;

        // 创建 GC 消息
        GCQueryHumanEntryList gcMSG = new GCQueryHumanEntryList();

        if (ggMSG._heel != null &&
            ggMSG._heel.isEmpty() == false) {
            // 如果角色入口列表不为空,
            // 则修改状态表
            stateTbl._hasHuman = true;
            // 添加角色入口列表
            gcMSG._humanEntryList.addAll(HumanEntryMO.fromEntityList(ggMSG._heel));
        } else {
            // 没有角色!
            stateTbl._hasHuman = false;
        }

        // 给客户端发消息
        this.sendMsgToClient(gcMSG, ggMSG._p);
    }
}
