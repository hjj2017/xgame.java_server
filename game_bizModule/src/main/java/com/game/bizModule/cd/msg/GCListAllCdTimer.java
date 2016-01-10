package com.game.bizModule.cd.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.type.MsgArrayList;

/**
 * 列表所有的 Cd 计时器
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class GCListAllCdTimer extends AbstractGCMsgObj {
    /** CdTimer 列表 */
    public MsgArrayList<CdTimerMO> _cdTimerList = new MsgArrayList<>(() -> new CdTimerMO());

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_LIST_ALL_TIMER;
    }
}
