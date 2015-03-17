package com.game.part.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 类实用工具
 * 
 * @author hjj2019
 * @since 2014/9/20
 * 
 */
public final class ClazzUtil {
	/**
	 * 类默认构造器
	 * 
	 */
	private ClazzUtil() {
	}

	/**
	 * 判断 A 是否为 B 的派生类
	 * 
	 * @param clazzA
	 * @param clazzB
	 * @return 
	 * 
	 */
	public static boolean isDrivedClazz(
		Class<?> clazzA, 
		Class<?> clazzB) {
		if (clazzA == null || 
			clazzB == null) {
			return false;
		} else if (clazzA.equals(clazzB)) {
			// 类自己不能作为自己的派生类, 
			// 所以返回 false
			return false;
		} else {
			// 判断 A 是否为 B 的派生类
			return clazzB.isAssignableFrom(clazzA);
		}
	}

	/**
	 * 是否为具体的派生类, 即, 派生类不是接口或抽象类
	 * 
	 * @param currClazz
	 * @param superClazz
	 * @return
	 */
	public static boolean isConcreteDrivedClass(
		Class<?> currClazz, 
		Class<?> superClazz) {
		if (!isDrivedClazz(currClazz, superClazz)) {
			// 如果连派生类的条件都不满足, 
			// 则直接返回 false
			return false;
		}

		// 获取修饰符
		int mod = currClazz.getModifiers();

		if (Modifier.isAbstract(mod) || 
			Modifier.isInterface(mod)) {
			// 如果使用了 abstract 或者 interface, 
			// 则直接返回 false
			return false;
		}

		return true;
	}

	/**
	 * 列表指定类的所有字段, 包括从父类继承来的字段
	 * 
	 * @param fromClazz
	 * @return 
	 * 
	 */
	public static List<Field> listField(Class<?> fromClazz) {
		if (fromClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 类集成关系堆栈
		LinkedList<Class<?>> clazzStack = new LinkedList<>();
		// 当前类
		Class<?> currClazz;

		for (currClazz = fromClazz; 
			 currClazz != null; 
			 currClazz = currClazz.getSuperclass()) {
			// 将当前类压入堆栈
			clazzStack.offerFirst(currClazz);
		}

		// 创建字段表
		List<Field> fl = new ArrayList<>();

		while ((currClazz = clazzStack.pollFirst()) != null) {
			// 获取方法数组
			Field[] fArr = currClazz.getDeclaredFields();

			for (Field f : fArr) {
				// 添加方法对象到列表
				fl.add(f);
			}
		}

		return fl;
	}

	/**
	 * 从指定类中获取满足条件的字段
	 * 
	 * @param fromClazz
	 * @param pred 
	 * @return 
	 * 
	 */
	public static List<Field> listField(Class<?> fromClazz, Predicate<Field> pred) {
		if (fromClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		List<Field> fl = listField(fromClazz);

		if (fl == null || 
			fl.isEmpty()) {
			return null;
		}

		if (pred == null) {
			// 如果条件为空, 
			// 则直接返回!
			return fl;
		} else {
			// 过滤字段列表
			return fl.stream().filter(pred).collect(Collectors.toList());
		}
	}

	/**
	 * 从指定类中获取满足条件的字段
	 * 
	 * @param fromClazz 
	 * @param pred 
	 * @return 
	 * 
	 */
	public static Field getField(Class<?> fromClazz, Predicate<Field> pred) {
		if (fromClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		List<Field> fl = listField(fromClazz, pred);

		if (fl == null || 
			fl.isEmpty()) {
			return null;
		} else {
			return fl.get(0);
		}
	}

	/**
	 * 获取指定名称的字段
	 * 
	 * @param fromClazz
	 * @param fieldName
	 * @return 
	 * 
	 */
	public static Field getField(Class<?> fromClazz, String fieldName) {
		if (fromClazz == null || 
			fieldName == null || 
			fieldName.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		} else {
			return getField(fromClazz, (f) -> {
				return f != null && f.getName().equals(fieldName);
			});
		}
	}

	/**
	 * 列表类方法, 包括从父类继承来的方法
	 * 
	 * @param fromClazz
	 * @return 
	 * 
	 */
	public static List<Method> listMethod(Class<?> fromClazz) {
		if (fromClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 类集成关系堆栈
		LinkedList<Class<?>> clazzStack = new LinkedList<>();
		// 当前类
		Class<?> currClazz;

		for (currClazz = fromClazz; 
			 currClazz != null; 
			 currClazz = currClazz.getSuperclass()) {
			// 将当前类压入堆栈
			clazzStack.offerFirst(currClazz);
		}

		// 创建方法列表
		List<Method> ml = new ArrayList<>();

		while ((currClazz = clazzStack.pollFirst()) != null) {
			// 获取方法数组
			Method[] mArr = currClazz.getDeclaredMethods();

			for (Method m : mArr) {
				// 添加方法对象到列表
				ml.add(m);
			}
		}

		return ml;
	}

	/**
	 * 从指定类中获得满足条件的方法列表
	 * 
	 * @param fromClazz
	 * @return 
	 * 
	 */
	public static List<Method> listMethod(Class<?> fromClazz, Predicate<Method> pred) {
		if (fromClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		List<Method> ml = listMethod(fromClazz);

		if (ml == null || 
			ml.isEmpty()) {
			return null;
		}

		if (pred == null) {
			// 如果条件为空, 
			// 则直接返回!
			return ml;
		} else {
			// 过滤字段列表
			return ml.stream().filter(pred).collect(Collectors.toList());
		}
	}

	/**
	 * 列表 get 方法
	 * 
	 * @param fromClazz
	 * @return 
	 * 
	 */
	public static <T extends Annotation> List<Method> listGetterMethod(Class<?> fromClazz) {
		return listMethod(fromClazz, (m) -> {
			return (m != null && (
					m.getName().startsWith("get") || 
					m.getName().startsWith("is")
			));
		});
	}

	/**
	 * 从类中获取满足条件的方法
	 * 
	 * @param fromClazz
	 * @param pred
	 * @return 
	 * 
	 */
	public static Method getMethod(Class<?> fromClazz, Predicate<Method> pred) {
		if (fromClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		List<Method> ml = listMethod(fromClazz, pred);

		if (ml == null || 
			ml.isEmpty()) {
			return null;
		} else {
			return ml.get(0);
		}
	}

	/**
	 * 从类中获取指定名称的方法
	 * 
	 * @param methodName 
	 * @param fromClazz
	 * @return 
	 * 
	 */
	public static Method getMethod(Class<?> fromClazz, String methodName) {
		if (fromClazz == null || 
			methodName == null ||
			methodName.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		} else {
			return getMethod(fromClazz, (m) -> {
				return m != null && m.getName().equals(methodName);
			});
		}
	}

	/**
	 * 判断字段是否含有泛型类型
	 * 
	 * @param f
	 * @return
	 * 
	 */
	public static boolean hasGenericType(Field f) {
		if (f == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		if (!(f.getGenericType() instanceof ParameterizedType)) {
			// 如果不是泛型类型, 
			// 则直接退出!
			return false;
		}

		// 获取泛型参数
		ParameterizedType tType = (ParameterizedType)f.getGenericType();

		if (tType.getActualTypeArguments().length <= 0) {
			// 如果泛型参数太少, 
			// 则直接退出!
			return false;
		}

		return true;
	}
}
