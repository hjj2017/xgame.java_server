package com.game.part.tmpl.codeGen;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.game.part.tmpl.XlsxTmplError;
import com.game.part.tmpl.codeGen.impl.Read_Plain;
import com.game.part.utils.Assert;

/**
 * 代码生成管理器
 * 
 * @author hjj2019
 * @since 2014/10/2
 * 
 */
class RegisteredReadCodeGen {
	/** 基本类型字段代码生成 */
	private static final Read_Plain R_plain = new Read_Plain();

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

		// 获取字段泛型类型, 例如 :
		// Col<List<Integer>> f;
		// 将取得 List<Integer> 这个类型
		final ParameterizedTypeImpl TType = (ParameterizedTypeImpl)f.getGenericType();

		if (isPlainField(f, TType)) {
			return R_plain;
		} else if (isPlainListField(f, TType)) {
		} else if (isObjField(f, TType)) {
		}

		return null;
	}

	/**
	 * 是否为普通字段类型
	 * 
	 * @param f 
	 * @param rawType
	 * @return 
	 * 
	 */
	public static boolean isPlainField(Field f, ParameterizedTypeImpl TType) {
		if (f == null || 
			TType == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 获取原始类型
		final Class<?> rawType = TType.getRawType();
		// 是否为基本类型 ?
		return isBasicType(rawType);
	}

	/**
	 * 是否为基本类型
	 * 
	 * @param typeClazz
	 * @return
	 * 
	 */
	public static boolean isBasicType(Class<?> typeClazz) {
		return typeClazz.equals(Integer.class) 
			|| typeClazz.equals(Long.class) 
			|| typeClazz.equals(Short.class)
			|| typeClazz.equals(String.class)
			|| typeClazz.equals(Boolean.class);
	}

	/**
	 * 是否为简单的列表型字段
	 * 
	 * @param f
	 * @param TType
	 * @return 
	 * 
	 */
	public static boolean isPlainListField(Field f, ParameterizedTypeImpl TType) {
		if (isListField(f, TType)) {
			// 如果不是列表型字段, 
			// 则直接退出!
			return false;
		}

		// 获取泛型类型
		Class<?> aType = (Class<?>)TType.getActualTypeArguments()[0];
		// 看看泛型参数是不是基本类型?
		return isBasicType(aType);
	}

	/**
	 * 是否为列表字段
	 * 
	 * @param f
	 * @param TType
	 * @return 
	 * 
	 */
	public static boolean isListField(Field f, ParameterizedTypeImpl TType) {
		if (f == null || 
			TType == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		if (TType.getRawType().isAssignableFrom(List.class) == false) {
			// 如果不是列表行字段, 
			// 则直接退出!
			return false;
		}

		if (TType.getActualTypeArguments().length <= 0) {
			// 如果不是 List 类型,
			// 或者是不带有泛型参数,
			// 则直接退出!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类 {1} 字段没有定义为 List 类型或者没有声明泛型类型, 应使用类似 List<MyObj> fieldName; 这样的定义",
				f.getDeclaringClass().getName(),
				f.getName()
			));
		}

		return true;
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
		if (isPlainField(f, TType)) {
			// 如果是普通类型的字段, 
			// 则直接退出!
			return false;
		} else {
			// 不能是列表类型字段
			return TType != null 
				&& TType.getRawType().isAssignableFrom(List.class) == false;
		}
	}
}
