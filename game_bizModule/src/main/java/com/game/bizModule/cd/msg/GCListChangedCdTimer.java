package com.game.bizModule.cd.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgArrayList;

/**
 * 列表变化的 Cd 计时器
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class GCListChangedCdTimer extends AbstractGCMsgObj {
    /** Cd 计时器列表 */
    public MsgArrayList<CdTimerMO> _changedCdTimerList = new MsgArrayList<>(() -> {
        return new CdTimerMO();
    });

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_LIST_CHANGED_TIMER;
    }
}
