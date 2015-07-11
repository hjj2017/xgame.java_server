package com.game.bizModule.human;

import com.game.part.util.EnumHelper;

/**
 * 角色属性枚举, 根据战斗系统中的属性而来.
 * <br />
 * <font color="#990000">注意 : 我在这里将战斗系统中的 1 级属性复制出来, 
 * 其目的在于不让其它系统依赖战斗系统!</font>
 * <br />
 * 鉴于 JAVA 语言对于类文件的封装性不太严密, 
 * 所以才使用这种方法!
 * 
 * @author haijiang.jin
 * @since 2013/1/22 
 * @see BattlePropsTypeEnum 
 * 
 */
public enum CharPropsTypeEnum implements EnumHelper.ICustomEnum {
;

	@Override
	public int getIntVal() {
		return -1;
	}

	@Override
	public String getStrVal() {
		return null;
	}

	/**
	 * 将整数值转换为枚举对象
	 * 
	 * @param intVal
	 * @return 
	 * 
	 */
	public static CharPropsTypeEnum parse(int intVal) {
		return EnumHelper.parse(
			intVal, 
			CharPropsTypeEnum.values()
		);
	}

	/**
	 * 将字符串转换为枚举对象
	 * 
	 * @param strVal
	 * @return 
	 * 
	 */
	public static CharPropsTypeEnum parse(String strVal) {
		return EnumHelper.parse(
			strVal, 
			CharPropsTypeEnum.values()
		);
	}
}
