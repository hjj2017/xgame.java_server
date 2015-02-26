package com.game.part.tmpl;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.game.part.tmpl.anno.OneToMany;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;
import com.sun.javafx.collections.MappingChange.Map;


/**
 * 打包工器构建者
 * 
 * @author hjj2017
 * @since 2015/2/27
 * 
 */
class XlsxPackerMaker {
	/**
	 * 类默认构造器
	 * 
	 */
	private XlsxPackerMaker() {
	}

	/**
	 * 构建打包器
	 * 
	 * @return 
	 * 
	 */
	public static IXlsxPacker make(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");
		// 获取键值对列表
		List<MyPair> pl = listAllPair(clazz);

		return null;
	}

	/**
	 * 列表出所有的 key 和 map 字段的配对
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	private static List<MyPair> listAllPair(Class<?> clazz) {
		if (clazz == null) {
			// 如果参数对象为空, 
			// 则返回空列表
			return Collections.emptyList();
		}

		// 收集分组名称
		Set<String> groupNameSet = collectGroupName(clazz);

		return groupNameSet.stream().map(
			name -> new MyPair(findMember(clazz, name, false), findMember(clazz, name, true)
		)).collect(Collectors.toList());
	}

	/**
	 * 收集分组名称
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	private static Set<String> collectGroupName(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 找到标注 OneToOne 和 OneToMany 注解的字段
		List<Field> fl = ClazzUtil.listField(clazz, c ->
			c.getAnnotation(OneToOne.class) != null || c.getAnnotation(OneToMany.class) != null
		);

		// 分组名称集合
		Set<String> groupNameSet = new HashSet<>();

		fl.forEach(f -> {
			// 获取 OneToOne 注解数组
			OneToOne[] o2oAnnoArr = f.getAnnotationsByType(OneToOne.class);

			for (OneToOne o2oAnno : o2oAnnoArr) {
				if (o2oAnno != null) {
					groupNameSet.add(o2oAnno.groupName());
				}
			}

			// 获取 OneToMany 注解数组
			OneToMany[] o2mAnnoArr = f.getAnnotationsByType(OneToMany.class);

			for (OneToMany o2mAnno : o2mAnnoArr) {
				if (o2mAnno != null) {
					groupNameSet.add(o2mAnno.groupName());
				}
			}
		});

		return groupNameSet;
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
	private static Member findMember(Class<?> fromClazz, String groupName, boolean isMap) {
		// 断言参数对象不为空
		Assert.notNull(fromClazz, "fromClazz");
		Assert.notNullOrEmpty(groupName, "groupName");

		// 找到字段定义
		Field foundF = ClazzUtil.getField(
			fromClazz, 
			f -> hasOneToXAnno(f, groupName)
		);

		if (foundF != null && 
			Map.class.isAssignableFrom(foundF.getType()) == isMap) {
			return foundF;
		}

		// 如果没有符合条件的字段, 
		// 那么就查找函数
		Method foundM = ClazzUtil.getMethod(
			fromClazz, 
			f -> hasOneToXAnno(f, groupName)
		);

		if (foundM != null &&
			Map.class.isAssignableFrom(foundM.getReturnType()) == isMap) {
			return foundM;
		}

		return null;
	}

	/**
	 * 判断字段或函数是否标注了 OneToOne 或者 OneToMany 注解, 并且分组名为指定名称
	 * 
	 * @param m
	 * @param groupName
	 * @return 
	 * 
	 */
	private static boolean hasOneToXAnno(Member m, String groupName) {
		if (m == null || 
			groupName == null || 
			groupName.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 定义 OneToOne 注解数组
		OneToOne[] o2oAnnoArr = null;
		// 定义 OneToMany 注解数组
		OneToMany[] o2mAnnoArr = null;

		if (m instanceof Field) {
			// 获取字段上的注解
			o2oAnnoArr = ((Field)m).getAnnotationsByType(OneToOne.class);
			o2mAnnoArr = ((Field)m).getAnnotationsByType(OneToMany.class);
		} else if (m instanceof Method) {
			// 获取方法上的注解
			o2oAnnoArr = ((Method)m).getAnnotationsByType(OneToOne.class);
			o2mAnnoArr = ((Method)m).getAnnotationsByType(OneToMany.class);
		} else {
			return false;
		}

		for (OneToOne o2oAnno : o2oAnnoArr) {
			if (o2oAnno.groupName().equals(groupName)) {
				return true;
			}
		}

		for (OneToMany o2mAnno : o2mAnnoArr) {
			if (o2mAnno.groupName().equals(groupName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 自定义配对, 主要用来匹配 key 和 map 的字段
	 * 
	 * @author hjj2017
	 * @since 2015/2/27
	 * 
	 */
	private static class MyPair {
		/** 关键字定义 */
		private final Member _keyDef;
		/** 字典的字段定义 */
		private final Member _mapDef;

		/**
		 * 类参数构造器
		 * 
		 * @param keyDef
		 * @param mapDef
		 */
		public MyPair(Member keyDef, Member mapDef) {
			this._keyDef = keyDef;
			this._mapDef = mapDef;
		}
	}
}
