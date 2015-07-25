package com.game.bizModule.cd.handler;

import com.game.bizModule.cd.bizServ.CdManager;
import com.game.bizModule.cd.bizServ.CdServ;
import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.cd.msg.CdTimerMO;
import com.game.bizModule.cd.msg.GCListChangedCdTimer;
import com.game.bizModule.human.Human;

/**
 * Cd 系统 GC 消息帮助类
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public final class CdGCMsgHelper {
    /**
     * 类默认构造器
     *
     */
    private CdGCMsgHelper() {
    }

    /**
     * 创建 Cd 计时器 GC 消息
     *
     * @param h
     * @param cdTypeArr
     * @return
     *
     */
    public static GCListChangedCdTimer createCdTimerGCMsg(Human h, CdTypeEnum ... cdTypeArr) {
        // 获取 Cd 管理器
        CdManager mngrObj = CdServ.OBJ._mngrMap.get(h._humanUId);
        // 创建 GC 消息
        GCListChangedCdTimer gcMSG = new GCListChangedCdTimer();

        for (CdTypeEnum cdType : cdTypeArr) {
            // 获取 Cd 计时器
            CdTimer timerObj = mngrObj.getCdTimer(cdType);

            if (timerObj == null) {
                // 如果计时器对象为空,
                // 则直接跳过!
                continue;
            }

            // 创建消息对象并添加到列表
            CdTimerMO mo = timerObj.toMsgObj();
            gcMSG._changedCdTimerList.add(mo);
        }

        return gcMSG;
    }
}
