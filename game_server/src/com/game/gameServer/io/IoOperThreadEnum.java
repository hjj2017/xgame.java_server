package com.game.gameServer.io;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.game.core.utils.EnumHelper;
import com.game.gameServer.framework.FrameworkLog;

/**
 * IO 操作线程枚举
 * 
 * @author hjj2019
 *
 */
public enum IoOperThreadEnum implements EnumHelper.ICustomEnum {
	// 
	// 登陆线程 16 个
	//
	login_00(GroupEnum.login, +0, "login_00"), 
	login_01(GroupEnum.login, +1, "login_01"), 
	login_02(GroupEnum.login, +2, "login_02"), 
	login_03(GroupEnum.login, +3, "login_03"), 
	login_04(GroupEnum.login, +4, "login_04"), 
	login_05(GroupEnum.login, +5, "login_05"), 
	login_06(GroupEnum.login, +6, "login_06"), 
	login_07(GroupEnum.login, +7, "login_07"), 
	login_08(GroupEnum.login, +8, "login_08"), 
	login_09(GroupEnum.login, +9, "login_09"), 
	login_10(GroupEnum.login, 10, "login_10"), 
	login_11(GroupEnum.login, 11, "login_11"), 
	login_12(GroupEnum.login, 12, "login_12"), 
	login_13(GroupEnum.login, 13, "login_13"), 
	login_14(GroupEnum.login, 14, "login_14"), 
	login_15(GroupEnum.login, 15, "login_15"), 

	// 
	// 绑定 UUID 的线程 16 个
	// 
	bind_00(GroupEnum.bind, +0, "bind_00"),
	bind_01(GroupEnum.bind, +1, "bind_01"),
	bind_02(GroupEnum.bind, +2, "bind_02"),
	bind_03(GroupEnum.bind, +3, "bind_03"),
	bind_04(GroupEnum.bind, +4, "bind_04"),
	bind_05(GroupEnum.bind, +5, "bind_05"),
	bind_06(GroupEnum.bind, +6, "bind_06"),
	bind_07(GroupEnum.bind, +7, "bind_07"),
	bind_08(GroupEnum.bind, +8, "bind_08"),
	bind_09(GroupEnum.bind, +9, "bind_09"),
	bind_10(GroupEnum.bind, 10, "bind_10"),
	bind_11(GroupEnum.bind, 11, "bind_11"),
	bind_12(GroupEnum.bind, 12, "bind_12"),
	bind_13(GroupEnum.bind, 13, "bind_13"),
	bind_14(GroupEnum.bind, 14, "bind_14"),
	bind_15(GroupEnum.bind, 15, "bind_15"),

	// 
	// 战斗线程 16 个
	// 
	battle_00(GroupEnum.battle, +0, "battle_00"),
	battle_01(GroupEnum.battle, +1, "battle_01"),
	battle_02(GroupEnum.battle, +2, "battle_02"),
	battle_03(GroupEnum.battle, +3, "battle_03"),
	battle_04(GroupEnum.battle, +4, "battle_04"),
	battle_05(GroupEnum.battle, +5, "battle_05"),
	battle_06(GroupEnum.battle, +6, "battle_06"),
	battle_07(GroupEnum.battle, +7, "battle_07"),
	battle_08(GroupEnum.battle, +8, "battle_08"),
	battle_09(GroupEnum.battle, +9, "battle_09"),
	battle_10(GroupEnum.battle, 10, "battle_10"),
	battle_11(GroupEnum.battle, 11, "battle_11"),
	battle_12(GroupEnum.battle, 12, "battle_12"),
	battle_13(GroupEnum.battle, 13, "battle_13"),
	battle_14(GroupEnum.battle, 14, "battle_14"),
	battle_15(GroupEnum.battle, 15, "battle_15"),
;

	/** 绑定 UUID 的操作分组 */
	private static final ConcurrentHashMap<GroupEnum, List<IoOperThreadEnum>>  _enumListMap = new ConcurrentHashMap<>();

	/** 分组 Id */
	private GroupEnum _group = null;
	/** 整数值 */
	private int _intVal = -1;
	/** 字符串值 */
	private String _strVal = null;

	/**
	 * 枚举参数构造器
	 * 
	 * @param group 
	 * @param intVal
	 * @param strVal
	 */
	private IoOperThreadEnum(GroupEnum group, int intVal, String strVal) {
		this._group = group;
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
	 * 获取枚举数组
	 * 
	 * @param group 
	 * @return 
	 * 
	 */
	public static List<IoOperThreadEnum> getEnumList(GroupEnum group) {
		if (group == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取枚举列表
		List<IoOperThreadEnum> enumList = _enumListMap.get(group);

		if (enumList != null) {
			// 如果枚举数组不为空, 
			// 则直接返回...
			return null;
		}

		// 过滤数组
		enumList = Arrays.asList(IoOperThreadEnum.values())
			.stream().filter(($enum) -> { 
				return $enum._group == group;
			}).collect(Collectors.toList());

		// 设置枚举列表
		List<IoOperThreadEnum> oldList = _enumListMap.putIfAbsent(group, enumList);

		if (oldList != null) {
			FrameworkLog.LOG.error("已经设置过枚举列表");
		}

		return enumList;
	}

	/**
	 * 分组
	 * 
	 * @author hjj2017
	 *
	 */
	public static enum GroupEnum {
		/** 登陆的分组 */
		login,
		/** 绑定 UUID 的分组 */
		bind,
		/** 战斗分组 */
		battle,
;
	}
}

