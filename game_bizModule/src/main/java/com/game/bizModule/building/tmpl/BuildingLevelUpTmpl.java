package com.game.bizModule.building.tmpl;

import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgLong;
import com.game.part.tmpl.type.AbstractXlsxTmpl;

/**
 * 建筑升级 Cd 配置
 *
 * @author hjj2019
 * @since 2015/7/25
 *
 */
public class BuildingLevelUpTmpl extends AbstractXlsxTmpl {
    /** 建筑类型 */
    public MsgInt _buildingTypeInt;
    /** 建筑等级 */
    public MsgInt _buildingLevel;
    /** 银两 */
    public MsgInt _needSilver;
    /** 所需 Cd 时间 */
    public MsgLong _needCdTime;

    public static BuildingLevelUpTmpl getByBuildingTypeAndLevel(BuildingTypeEnum bt, int level) {
        return null;
    }
}
