package com.game.bizModule.building.msg;

import com.game.bizModule.building.handler.Handler_CGBuildingLevelUp;
import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.global.AllMsgSerialUId;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.type.MsgInt;

/**
 * 建筑升级
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class CGBuildingLevelUp extends AbstractCGMsgObj {
    /** 建筑类型整数值 */
    public MsgInt _buildingTypeInt;

    /**
     * 类默认构造器
     *
     */
    public CGBuildingLevelUp() {
    }

    /**
     * 类参数构造器
     *
     * @param bt
     *
     */
    public CGBuildingLevelUp(BuildingTypeEnum bt) {
        this(bt.getIntVal());
    }

    /**
     * 类参数构造器
     *
     * @param buildingTypeInt
     *
     */
    public CGBuildingLevelUp(int buildingTypeInt) {
        this._buildingTypeInt = new MsgInt(buildingTypeInt);
    }

    /**
     * 获取建筑类型枚举
     *
     * @return
     *
     */
    public BuildingTypeEnum getBuildingType() {
        return BuildingTypeEnum.parse(this._buildingTypeInt.getIntVal());
    }

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.CG_BUILDING_LEVEL_UP;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AbstractCGMsgHandler<CGBuildingLevelUp> newHandlerObj() {
        return new Handler_CGBuildingLevelUp();
    }
}
