package com.game.bizModule.building.handler;

import com.game.bizModule.building.bizServ.BuildingServ;
import com.game.bizModule.building.bizServ.Result_DoLevelUp;
import com.game.bizModule.building.msg.CGBuildingLevelUp;
import com.game.bizModule.cd.handler.CdGCMsgHelper;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.cd.msg.GCListChangedCdTimer;
import com.game.bizModule.human.Human;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractCGMsgHandler;

/**
 * 建筑升级
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class Handler_CGBuildingLevelUp extends AbstractCGMsgHandler<CGBuildingLevelUp> {
    @Override
    public void handle(CGBuildingLevelUp msgObj) {
        // 获取玩家对象
        Player p = this.getPlayer();
        // 获取角色对象
        Human h = Human.getHumanByPlayer(p);
        // 执行建筑升级
        Result_DoLevelUp result = BuildingServ.OBJ.doLevelUp(h, msgObj.getBuildingType());

        if (result.isFail()) {
        }

        if (result._usedCdType != null) {
            // 如果使用了 Cd,
            // 那么需要告知前端更新 Cd 列表
            GCListChangedCdTimer gcMSG = CdGCMsgHelper.createCdTimerGCMsg(
                h, result._usedCdType
            );
            // 发送 GC 消息
            this.sendMsgToClient(gcMSG);
        }
    }
}
