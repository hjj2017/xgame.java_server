package com.game.bizModule.building.bizServ;

import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.cd.bizServ.CdServ;
import com.game.bizModule.cd.bizServ.Result_FindAndDoAddTime;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;

/**
 * 建筑升级
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
interface IServ_LevelUp {
    /**
     * 建筑升级
     *
     * @param h
     * @param buildingType
     *
     */
    default void levelUp(Human h, BuildingTypeEnum buildingType) {
        if (h == null ||
            buildingType == null) {
            return;
        }

        // 获取建筑管理器
        BuildingManager mngrObj = h.getPropVal(BuildingManager.class);

        if (mngrObj == null) {
            return;
        }

        // 查找并增加 Cd 时间
        Result_FindAndDoAddTime result_2 = CdServ.OBJ.findAndDoAddTime(
            h._humanUId,
            CdTypeEnum.BUILDING_CD_TYPE_ARR,
            60000
        );

        if (result_2.isFail()) {
            // 如果增加 Cd 失败,
            // 则直接退出!
            return;
        }

        // 增加建筑等级并保存
        mngrObj.addLevel(buildingType, +1);
        mngrObj.saveOrUpdate();
    }
}
