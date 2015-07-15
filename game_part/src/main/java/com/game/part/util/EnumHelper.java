package com.game.part.util;

import java.text.MessageFormat;

/**
 * 枚举帮着类
 * 
 * @author haijiang.jin
 * @since 2013/1/22
 * 
 */
public final class EnumHelper {
	/**
	 * 类默认构造器
	 *
	 */
	private EnumHelper() {
	}

	/**
	 * 转换整数值为枚举
	 * 
	 * @param ordinalOrIntVal
	 * @param referEnumArr
	 * @return 
	 * 
	 */
	public static<T extends Enum<T>> T parse(
		int ordinalOrIntVal,
		T[] referEnumArr) {
		// 断言参数不为空
		Assert.notNullOrEmpty(referEnumArr, "null referEnumArr");

		for (T $enum : referEnumArr) {
			if ($enum.ordinal() == ordinalOrIntVal) {
				return $enum;
			} else if ($enum instanceof ICustomEnum) {
				if (((ICustomEnum)$enum).getIntVal() == ordinalOrIntVal) {
					return $enum;
				}
			}
		}

		// 无法解析则抛出异常!
		throw new RuntimeException(MessageFormat.format(
			"无法将 {0} 解析为 {1} 枚举 ",
			ordinalOrIntVal,
			referEnumArr[0].getClass().getName()
		));
	}

	/**
	 * 转换字符串为枚举
	 * 
	 * @param nameOrStrVal
	 * @param referEnumArr
	 * @return 
	 * 
	 */
	public static<T extends Enum<T>> T parse(
		String nameOrStrVal,
		T[] referEnumArr) {
		// 断言参数不为空
		Assert.notNullOrEmpty(referEnumArr, "null referEnumArr");

		for (T $enum : referEnumArr) {
			if ($enum.name().equals(nameOrStrVal)) {
				return $enum;
			} else if ($enum instanceof ICustomEnum) {
				if (((ICustomEnum)$enum).getStrVal().equals(nameOrStrVal)) {
					return $enum;
				}
			}
		}

		// 无法解析则抛出异常!
		throw new RuntimeException(MessageFormat.format(
			"无法将 {0} 解析为 {1} 枚举 ",
			nameOrStrVal,
			referEnumArr[0].getClass().getName()
		));
	}

	/**
	 * 自定义枚举
	 *
	 * @author haijiang.jin
	 * @see 2013/1/22
	 *
	 */
	public static interface ICustomEnum {
		/**
		 * 获取整数值
		 *
		 * @return
		 *
		 */
		int getIntVal();

		/**
		 * 获取字符串值
		 *
		 * @return
		 *
		 */
		String getStrVal();
	}
}
