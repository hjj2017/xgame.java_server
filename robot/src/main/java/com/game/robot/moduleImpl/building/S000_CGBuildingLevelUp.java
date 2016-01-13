package com.game.robot.moduleImpl.building;

import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.building.msg.CGBuildingLevelUp;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 建筑升级
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S000_CGBuildingLevelUp extends AbstractModuleReady {
    @Override
    public void ready(Robot robotObj) {
        if (robotObj == null) {
            // 如果参数对象为空, 
            // 则直接退出!
            return;
        }

        // 升级主城
        robotObj.sendMsg(new CGBuildingLevelUp(BuildingTypeEnum.home));
        robotObj.gotoNextModuleAndReady();
    }
}
