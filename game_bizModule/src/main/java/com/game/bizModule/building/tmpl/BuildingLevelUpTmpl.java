package com.game.bizModule.building.tmpl;

import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxLong;

import java.util.HashMap;
import java.util.Map;

/**
 * 建筑升级 Cd 配置
 *
 * @author hjj2019
 * @since 2015/7/25
 *
 */
@FromXlsxFile(fileName = "building.xlsx", sheetIndex = 1)
public class BuildingLevelUpTmpl extends AbstractXlsxTmpl {
    /** 建筑类型 */
    public XlsxInt _buildingTypeInt;
    /** 建筑等级 */
    public XlsxInt _buildingLevel;
    /** 银两 */
    public XlsxInt _needSilver;
    /** 所需 Cd 时间 */
    public XlsxLong _needCdTime;

    /** 建筑类型 + 建筑等级字典 */
    @OneToOne(groupName = "_typeIntAndLevel")
    public static final Map<String, BuildingLevelUpTmpl> _typeIntAndLevelMap = new HashMap<>();

    /**
     * 获取建筑类型 + 建筑等级关键字
     *
     * @return
     *
     */
    @OneToOne(groupName = "_typeIntAndLevel")
    public String getTypeIntAndLevelKey() {
        return getTypeIntAndLevelKey(
            this._buildingTypeInt.getIntVal(), this._buildingLevel.getIntVal()
        );
    }

    /**
     * 获取建筑类型 + 建筑等级关键字,
     * <font color="#990000">注意: 建筑类型 + 建筑等级属于一种复合主键</font>
     *
     * @param buildingTypeInt
     * @param buildingLevel
     * @return
     *
     */
    private static String getTypeIntAndLevelKey(
        int buildingTypeInt,
        int buildingLevel) {
        return buildingTypeInt + "-" + buildingLevel;
    }

    /**
     * 根据建筑类型和建筑等级获取建筑升级模版
     *
     * @param bt
     * @param buildingLevel
     * @return
     *
     */
    public static BuildingLevelUpTmpl getByBuildingTypeAndLevel(
        BuildingTypeEnum bt, int buildingLevel) {
        if (bt == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取关键字
        String key = getTypeIntAndLevelKey(bt.getIntVal(), buildingLevel);
        // 获取升级模版
        return _typeIntAndLevelMap.get(key);
    }
}
