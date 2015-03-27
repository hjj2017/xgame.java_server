package com.game.bizModules.cd.model;

import com.game.part.util.EnumHelper;

/**
 * Cd 类型
 * 
 * @author hjj2019
 * @since 2014/5/18
 * 
 */
public enum CdTypeEnum implements EnumHelper.ICustomEnum {
	/** 战斗冷却 */
	battle(1, "battle"),

	/** 建筑队列 - 1 */
	building1(2001, "building1"),
	/** 建筑队列 - 2 */
	building2(2002, "building2"),
	/** 建筑队列 - 3 */
	building3(2003, "building3"),
;

	/** 建筑类型 Cd 数组 */
	private static final CdTypeEnum[] BUILDING_CD_TYPE_ARR = { building1, building2, building3 };

	/** 整数值 */
	private int _intVal = -1;
	/** 字符串值 */
	private String _strVal = null;

	/**
	 * 枚举参数构造器
	 * 
	 * @param intVal
	 * @param strVal
	 * 
	 */
	private CdTypeEnum(int intVal, String strVal) {
		this._intVal = intVal;
		this._strVal = strVal;
	}

	@Override
	public int intVal() {
		return this._intVal;
	}

	@Override
	public String strVal() {
		return this._strVal;
	}

	/**
	 * 获取建筑 Cd 类型数组
	 * 
	 * @return 
	 * 
	 */
	public static CdTypeEnum[] getBuildingCdTypeArray() {
		return BUILDING_CD_TYPE_ARR;
	}

	/**
	 * 将整数值解析为枚举对象
	 * 
	 * @param intVal
	 * @return
	 * 
	 */
	public static CdTypeEnum parse(int intVal) {
		return EnumHelper.parse(intVal, CdTypeEnum.values());
	}
}
