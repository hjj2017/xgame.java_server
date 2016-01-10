package com.game.bizModule.cd.handler;

import java.util.Map;
import java.util.Set;

import com.game.bizModule.cd.bizServ.CdManager;
import com.game.bizModule.cd.bizServ.CdServ;
import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.cd.msg.CGListAllCdTimer;
import com.game.bizModule.cd.msg.CdTimerMO;
import com.game.bizModule.cd.msg.GCListAllCdTimer;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.part.msg.type.MsgArrayList;

/**
 * 列表所有 Cd 计时器
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class Handler_CGListAllCdTimer extends AbstractCGMsgHandler<CGListAllCdTimer> {
    @Override
    public void handle(CGListAllCdTimer cgMSG) {
        if (cgMSG == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取 Cd 管理器对象
        CdManager mngrObj = CdServ.OBJ._mngrMap.get(this._sessionUId);

        // 创建 GC 消息
        GCListAllCdTimer gcMSG = new GCListAllCdTimer();
        // 添加 Cd 计时器
        mngrObj._cdTimerMap.values().forEach(cdT -> gcMSG._cdTimerList.add(
            CdTimerMO.newObj(cdT)
        ));

        // 发送 GC 消息
        this.sendMsgToClient(gcMSG);
    }
}
