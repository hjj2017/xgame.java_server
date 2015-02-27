package com.game.part.tmpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.game.part.tmpl.anno.OneToMany;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;

/**
 * OneToOne, OneToMany 关键字及字典对应关系定义
 * 
 * @author hjj2019
 * @since 2015/2/27
 * 
 */
final class OneToXDefPair {
	/** 关键字定义 */
	final Member _keyDef;
	/** 字典的字段定义 */
	final Field _mapDef;
	/** 是否为一对一 */
	final boolean _oneToOne;

	/**
	 * 类参数构造器
	 * 
	 * @param keyDef
	 * @param mapDef
	 * @param oneToOne 
	 * 
	 */
	private OneToXDefPair(Member keyDef, Field mapDef, boolean oneToOne) {
		// 断言参数对象不为空
		Assert.notNull(keyDef, "keyDef");
		Assert.notNull(mapDef, "mapDef");
		// 设置属性值
		this._keyDef = keyDef;
		this._mapDef = mapDef;
		this._oneToOne = oneToOne;
	}

	/**
	 * 列表出所有的 key 和 map 字段的配对, 包括 OneToOne, OneToMany
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	public static List<OneToXDefPair> listAll(Class<?> clazz) {
		if (clazz == null) {
			// 如果参数对象为空, 
			// 则返回空列表
			return Collections.emptyList();
		}

		// 收集分组名称
		Map<String, Annotation> annoMap = collectOneToXAnno(clazz);
		// 返回配对列表
		return annoMap.values().stream().map(V ->
			new OneToXDefPair(
				findKeyDef(clazz, V), 
				findMapDef(clazz, V), 
				V instanceof OneToOne
			)
		).collect(Collectors.toList());
	}

	/**
	 * 遍历所有标注了 OneToOne, OneToMany 的字段或方法, 收集 groupName 值
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	private static Map<String, Annotation> collectOneToXAnno(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 创建注解字典
		Map<String, Annotation> annoMap = new HashMap<>();

		// 找到标注 OneToOne 和 OneToMany 注解的字段
		ClazzUtil.listField(
			clazz, f -> {
				putToMap(
					f.getAnnotationsByType(OneToMany.class), 
					annoMap
				); return true;
			}
		);

		// 找到标注 OneToOne 和 OneToMany 注解的函数
		ClazzUtil.listMethod(
			clazz, m -> {
				putToMap(
					m.getAnnotationsByType(OneToMany.class), 
					annoMap
				); return true;
			}
		);

		return annoMap;
	}

	/**
	 * 将注解添加到字典
	 * 
	 * @param annoArr
	 * @param targetMap
	 * 
	 */
	private static void putToMap(
		Annotation[] annoArr, Map<String, Annotation> targetMap) {
		if (annoArr == null || 
			annoArr.length <= 0 || 
			targetMap == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		for (Annotation anno : annoArr) {
			if (anno == null) {
				// 如果注解为空, 
				// 则直接跳过!
				continue;
			}

			if (anno instanceof OneToOne) {
				targetMap.put(
					((OneToOne)anno).groupName(), anno
				);
			} else if (anno instanceof OneToMany) {
				targetMap.put(
					((OneToMany)anno).groupName(), anno
				);
			}
		}
	}

	/**
	 * 查找 key 字段定义
	 * 
	 * @param fromClazz
	 * @param groupName
	 * @return 
	 * 
	 */
	private static Member findKeyDef(Class<?> fromClazz, Annotation anno) {
		// 断言参数不为空
		Assert.notNull(fromClazz, "fromClazz");
		Assert.notNull(anno, "anno");

		// 分组名称
		final String groupName;

		if (anno instanceof OneToOne) {
			groupName = ((OneToOne)anno).groupName();
		} else if (anno instanceof OneToMany) {
			groupName = ((OneToMany)anno).groupName();
		} else {
			return null;
		}

		return findMember(
			fromClazz, groupName, false
		);
	}

	/**
	 * 查找 map 字段定义
	 * 
	 * @param fromClazz
	 * @param groupName
	 * @return 
	 * 
	 */
	private static Field findMapDef(Class<?> fromClazz, Annotation anno) {
		// 断言参数不为空
		Assert.notNull(fromClazz, "fromClazz");
		Assert.notNull(anno, "anno");

		// 分组名称
		final String groupName;

		if (anno instanceof OneToOne) {
			groupName = ((OneToOne)anno).groupName();
		} else if (anno instanceof OneToMany) {
			groupName = ((OneToMany)anno).groupName();
		} else {
			return null;
		}

		Member m = findMember(
			fromClazz, groupName, true
		);

		return (Field)m;
	}

	/**
	 * 获取关键字字段
	 * 
	 * @param fromClazz
	 * @param groupName
	 * @param isMap 
	 * @return
	 * 
	 */
	private static Member findMember(Class<?> fromClazz, String groupName, boolean findMap) {
		// 断言参数对象不为空
		Assert.notNull(fromClazz, "fromClazz");
		Assert.notNullOrEmpty(groupName, "groupName");

		// 找到字段定义
		List<Field> foundFL = ClazzUtil.listField(
			fromClazz, 
			f -> hasOneToXAnno(f, groupName)
		);

		if (foundFL != null) {
			for (Field F : foundFL) {
				if (F != null && 
					Map.class.isAssignableFrom(F.getType()) == findMap) {
					return F;
				}
			};
		}

		// 如果没有符合条件的字段, 
		// 那么就查找函数
		List<Method> foundML = ClazzUtil.listMethod(
			fromClazz, 
			f -> hasOneToXAnno(f, groupName)
		);

		if (foundML != null) {
			for (Method M : foundML) {
				if (M != null && 
					Map.class.isAssignableFrom(M.getReturnType()) == findMap) {
					return M;
				}
			}
		}

		return null;
	}

	/**
	 * 判断字段或函数是否标注了 OneToOne 或者 OneToMany 注解, 并且分组名为指定名称
	 * 
	 * @param member
	 * @param groupName
	 * @return 
	 * 
	 */
	private static boolean hasOneToXAnno(
		Member member, 
		String groupName) {
		return hasOneToOne(member, groupName) || hasOneToMany(member, groupName);
	}

	/**
	 * 是否有 OneToOne 注解
	 * 
	 * @param m
	 * @param groupName
	 * @return
	 * 
	 */
	private static boolean hasOneToOne(Member m, String groupName) {
		if (m == null || 
			groupName == null || 
			groupName.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 定义 OneToOne 注解数组
		final OneToOne[] annoArr;
		
		if (m instanceof AccessibleObject) {
			// 获取 OneToOne 注解数组
			annoArr = ((AccessibleObject)m).getAnnotationsByType(OneToOne.class);
		} else {
			annoArr = null;
		}

		for (OneToOne anno : annoArr) {
			if (anno.groupName().equals(groupName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否有 OneToMany 注解
	 * 
	 * @param m
	 * @param groupName
	 * @return 
	 * 
	 */
	private static boolean hasOneToMany(Member m, String groupName) {
		if (m == null || 
			groupName == null || 
			groupName.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		
		// 定义 OneToMany 注解数组
		final OneToMany[] annoArr;
		
		if (m instanceof AccessibleObject) {
			// 获取 OneToMany 注解数组
			annoArr = ((AccessibleObject)m).getAnnotationsByType(OneToMany.class);
		} else {
			annoArr = null;
		}

		if (annoArr == null || 
			annoArr.length <= 0) {
			// 如果没有 OneToOne, OneToMany 注解, 
			// 则直接退出!
			return false;
		}

		for (OneToMany anno : annoArr) {
			if (anno.groupName().equals(groupName)) {
				return true;
			}
		}

		return false;
	}
}
