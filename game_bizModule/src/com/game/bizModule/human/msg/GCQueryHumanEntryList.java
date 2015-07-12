package com.game.bizModule.human.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgArrayList;

/**
 * 查询角色入口列表
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class GCQueryHumanEntryList extends AbstractGCMsgObj {
    /** 玩家角色入口列表 */
    public MsgArrayList<HumanEntryMO> _humanEntryList = new MsgArrayList<>(() -> {
        return new HumanEntryMO();
    });

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_QUERY_HUMAN_ENTRY_LIST;
    }
}
