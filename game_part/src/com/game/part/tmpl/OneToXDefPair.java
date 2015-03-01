package com.game.part.tmpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.ArrayList;
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
	final Member _mapDef;
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
	private OneToXDefPair(Member keyDef, Member mapDef, boolean oneToOne) {
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
		Map<String, OneToXDefPair_X> pairXMap = collectOneToXAnno(clazz);
		// 返回配对列表
		return pairXMap.values().stream().map(pairX -> {
			// 执行验证过程
			pairX.validate();

			// 获取键值定义
			final Member keyDef = pairX.getKeyDef();
			final Member mapDef = pairX.getMapDef();

			return new OneToXDefPair(
				keyDef, mapDef, 
				pairX.isOneToOne()
			);
		}).collect(Collectors.toList());
	}

	/**
	 * 遍历所有标注了 OneToOne, OneToMany 的字段或方法, 收集 groupName 值
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	private static Map<String, OneToXDefPair_X> collectOneToXAnno(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 创建注解字典
		Map<String, OneToXDefPair_X> xMap = new HashMap<>();

		// 找到标注 OneToOne 和 OneToMany 注解的字段
		ClazzUtil.listField(
			clazz, f -> {
				putToMap(f, xMap); 
				return true;
			}
		);

		// 找到标注 OneToOne 和 OneToMany 注解的函数
		ClazzUtil.listMethod(
			clazz, m -> {
				putToMap(m, xMap);
				return true;
			}
		);

		return xMap;
	}

	/**
	 * 将注解添加到字典
	 * 
	 * @param annoArr
	 * @param targetMap
	 * 
	 */
	private static void putToMap(
		Member m, Map<String, OneToXDefPair_X> targetMap) {
		if (m == null ||  
			targetMap == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		List<Annotation> annoList = new ArrayList<>();

		if (m instanceof AccessibleObject) {
			// 添加 OneToOne 注解到列表
			Collections.addAll(
				annoList, ((AccessibleObject)m).getAnnotationsByType(OneToOne.class)
			);

			// 添加 OneToMany 注解到列表
			Collections.addAll(
				annoList, ((AccessibleObject)m).getAnnotationsByType(OneToMany.class)
			);
		}

		annoList.forEach(anno -> {
			// 分组名称
			final String groupName;

			if (anno instanceof OneToOne) {
				groupName = ((OneToOne)anno).groupName();
			} else/* if (anno instanceof OneToMany) */{
				groupName = ((OneToMany)anno).groupName();
			}

			// 获取临时对象
			OneToXDefPair_X pairX = targetMap.get(groupName);
	
			if (pairX == null) {
				pairX = new OneToXDefPair_X(groupName, m.getDeclaringClass());
				// 添加到字典
				targetMap.put(groupName, pairX);
			}

			// 添加注解类和成员到集合
			pairX._annoClazzSet.add(anno.annotationType());
			pairX._memberSet.add(m);
		});
	}
}
