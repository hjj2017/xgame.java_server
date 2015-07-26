package com.game.bizModule.cd.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractGCMsgObj;

/**
 * 列表所有的 Cd 计时器
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class GCListAllCdTimer extends AbstractGCMsgObj {
    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.GC_LIST_ALL_TIMER;
    }
}
