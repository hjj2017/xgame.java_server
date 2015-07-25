package com.game.bizModule.building.model;

import com.game.part.util.EnumHelper;
import com.game.part.util.EnumHelper.ICustomEnum;

/**
 * 建筑类型枚举
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
public enum BuildingTypeEnum implements ICustomEnum {
    /** 主城 */
    home("home", 1001),
    /** 酒馆 */
    pub("pub", 1002),
    /** 铁匠铺 */
    forge("forge", 1003),
;

    /** 字符串数值 */
    private String _strVal = null;
    /** 整数数值 */
    private int _intVal = 0;

    /**
     * 类参数构造器
     *
     * @param strVal
     * @param intVal
     *
     */
    BuildingTypeEnum(String strVal, int intVal) {
        this._strVal = strVal;
        this._intVal = intVal;
    }

    @Override
    public String getStrVal() {
        return this._strVal;
    }

    @Override
    public int getIntVal() {
        return this._intVal;
    }

    /**
     * 将整数值解析为枚举
     *
     * @param intVal
     * @return
     *
     */
    public static BuildingTypeEnum parse(int intVal) {
        return EnumHelper.parse(intVal, BuildingTypeEnum.values());
    }
}
