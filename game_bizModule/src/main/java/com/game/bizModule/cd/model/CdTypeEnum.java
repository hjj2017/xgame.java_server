package com.game.bizModule.cd.model;

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
	battle("battle", 1001),
	/** 建筑队列 - 1 */
	building1("building1", 2001),
	/** 建筑队列 - 2 */
	building2("building2", 2002),
	/** 建筑队列 - 3 */
	building3("building3", 2003),
	/** 征收 */
	levy("levy", 4001),
	/** 装备强化 */
	equipEnhance("equipEnhance", 3001),
	/** 单人竞技场 */
	singleArena("singleArena", 5001),
	/** 世界聊天 */
	worldChat("worldChat", 6001),
;

	/** 字符串值 */
	private String _strVal = null;
	/** 整数值 */
	private int _intVal = -1;

	/**
	 * 枚举参数构造器
	 * 
	 * @param intVal
	 * @param strVal
	 * 
	 */
	CdTypeEnum(String strVal, int intVal) {
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
