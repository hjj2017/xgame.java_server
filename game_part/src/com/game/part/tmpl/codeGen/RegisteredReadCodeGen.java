package com.game.part.tmpl.codeGen;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.codeGen.impl.Read_Plain;
import com.game.part.tmpl.codeGen.impl.Read_PlainList;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxPlainList;
import com.game.part.tmpl.type.XlsxStr;
import com.game.part.utils.Assert;

/**
 * 代码生成管理器
 * 
 * @author hjj2019
 * @since 2014/10/2
 * 
 */
class RegisteredReadCodeGen {
	/** 基本类型数组 */
	private static final Set<Class<?>> _basicTypeClazzSet = new HashSet<>(Arrays.asList(
		XlsxInt.class 
		, XlsxStr.class
	));

	/** 基本类型字段代码生成 */
	private static final Read_Plain R_plain = new Read_Plain();
	/** 基本类型列表字段代码生成 */
	private static final Read_PlainList R_plainList = new Read_PlainList();

	/**
	 * 类默认构造器
	 * 
	 */
	private RegisteredReadCodeGen() {
	}

	/**
	 * 获取代码生成器
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	public static IReadCodeGen getGen(Field f) {
		// 断言参数不为空
		Assert.notNull(f, "f");

		if (isPlainField(f)) {
			return R_plain;
		} else if (isPlainListField(f)) {
			return R_plainList;
		}

		return null;
	}

	/**
	 * 是否为普通字段类型
	 * 
	 * @param f 
	 * @return 
	 * 
	 */
	public static boolean isPlainField(Field f) {
		return f != null && _basicTypeClazzSet.contains(f.getType());
	}

	/**
	 * 是否为简单的列表型字段
	 * 
	 * @param f
	 * @param tType
	 * @return 
	 * 
	 */
	public static boolean isPlainListField(Field f) {
		return f != null && f.getType().equals(XlsxPlainList.class);
	}

	/**
	 * 是否为列表字段
	 * 
	 * @param f
	 * @param tType
	 * @return 
	 * 
	 */
	public static boolean isListField(Field f, ParameterizedTypeImpl tType) {
		if (f == null || 
			tType == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		if (tType.getActualTypeArguments().length <= 0) {
			// 如果不是 List 类型,
			// 或者是不带有泛型参数,
			// 则直接退出!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类 {1} 字段没有定义为 List 类型或者没有声明泛型类型, 应使用类似 List<MyObj> fieldName; 这样的定义",
				f.getDeclaringClass().getName(),
				f.getName()
			));
		}

		// 获取真实类型
		final Type aType = tType.getActualTypeArguments()[0];

		if (!(aType instanceof ParameterizedTypeImpl) ||
			((ParameterizedTypeImpl)aType).getActualTypeArguments().length <= 0) {
			// 如果不是模板参数, 
			// 则直接退出!
			return false;
		}

		// 获取真实类型
		final ParameterizedTypeImpl aaType = (ParameterizedTypeImpl)aType;
		// 获取原始类型
		final Class<?> rawType = aaType.getRawType();

		return rawType.equals(List.class);
	}

	/**
	 * 是否为对象型字段
	 * 
	 * @param f
	 * @param TType
	 * @return 
	 * 
	 */
	public static boolean isObjField(Field f, ParameterizedTypeImpl TType) {
		return false;
	}
}
