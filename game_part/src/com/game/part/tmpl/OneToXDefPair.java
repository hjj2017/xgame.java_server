package com.game.part.tmpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
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
		Map<String, Annotation> annoMap = collectOneToXAnno(clazz);
		// 返回配对列表
		return annoMap.values().stream().map(anno -> {
			// 获取键值定义
			final Member kDef = findKeyDef(clazz, anno);
			final Member vDef = findMapDef(clazz, anno);

			return new OneToXDefPair(
				kDef, vDef, 
				anno instanceof OneToOne
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
	private static Map<String, Annotation> collectOneToXAnno(Class<?> clazz) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");

		// 创建注解字典
		Map<String, Annotation> annoMap = new HashMap<>();

		// 找到标注 OneToOne 和 OneToMany 注解的字段
		ClazzUtil.listField(
			clazz, f -> {
				putToMap(
					f.getAnnotationsByType(OneToOne.class), 
					annoMap
				);
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
					m.getAnnotationsByType(OneToOne.class), 
					annoMap
				);
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
			} else {
				// 抛出异常!
				throw new XlsxTmplError(MessageFormat.format(
					"不支持的注解类型 {0}", 
					anno.getClass().getName()
				));
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

		Member m = findMember(
			fromClazz, anno.annotationType(), groupName, false
		);

		return m;
	}

	/**
	 * 查找 map 字段定义
	 * 
	 * @param fromClazz
	 * @param groupName
	 * @return 
	 * 
	 */
	private static Member findMapDef(Class<?> fromClazz, Annotation anno) {
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
			fromClazz, anno.annotationType(), groupName, true
		);

		if (m instanceof Method) {
			if ((m.getModifiers() & Modifier.PUBLIC) == 0 ||
				(m.getModifiers() & Modifier.STATIC) == 0) {
				// 如果不是公共的静态方法, 
				// 则抛出异常!
				throw new XlsxTmplError(MessageFormat.format(
					"{0} 类方法 {1} 不是公共的、静态的, 这是不允许的!, 请使用 public static {2} {1}() { return ...; } 这样的代码", 
					fromClazz.getName(), 
					m.getName(),
					((Method)m).getReturnType().getSimpleName()
				));
			}

			try {
				// 获取函数返回值
				Object obj = ((Method)m).invoke(m.getDeclaringClass());
	
				if (obj == null) {
					// 如果返回值为空, 
					// 则抛出异常!
					throw new XlsxTmplError(MessageFormat.format(
						"{0} 类静态方法 {1} 返回值为空, 这是不允许的!", 
						fromClazz.getName(), 
						m.getName()
					));
				}
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(ex);
			}
		} else if (m instanceof Field) {
			if ((m.getModifiers() & Modifier.PUBLIC) == 0 ||
				(m.getModifiers() & Modifier.STATIC) == 0) {
				// 如果返回值为空, 
				// 则抛出异常!
				throw new XlsxTmplError(MessageFormat.format(
					"{0} 类字段 {1} 不是公共的、静态的, 这是不允许的!, 请使用 public static {2} {1} = new HashMap<>(); 这样的代码", 
					fromClazz.getName(), 
					m.getName(), 
					((Field)m).getType().getSimpleName()
				));
			}

			try {
				// 获取字段值
				Object obj = ((Field)m).get(fromClazz);
				
				if (obj == null) {
					// 如果返回值为空, 
					// 则抛出异常!
					throw new XlsxTmplError(MessageFormat.format(
						"{0} 类静态字段 {1} 为空值, 这是不允许的!", 
						fromClazz.getName(), 
						m.getName()
					));
				}
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(ex);
			}
		}

		return m;
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
	private static Member findMember(
		Class<?> fromClazz, Class<? extends Annotation> annoClazz, String groupName, boolean findMap) {
		// 断言参数对象不为空
		Assert.notNull(fromClazz, "fromClazz");
		Assert.notNullOrEmpty(groupName, "groupName");

		// 找到字段定义
		List<Field> foundFL = ClazzUtil.listField(
			fromClazz, 
			f -> hasOneToXAnno(f, annoClazz, groupName)
		);

		// 再找到函数定义
		List<Method> foundML = ClazzUtil.listMethod(
			fromClazz, 
			f -> hasOneToXAnno(f, annoClazz, groupName)
		);

		int annoNum = 0;
		if (foundFL != null) { annoNum += foundFL.size(); }
		if (foundML != null) { annoNum += foundML.size(); }

		if (annoNum == 1) {
			// 如果不是成对儿出现, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 @{1}(groupName = \"{2}\") 这个注解不是成对儿出现的!", 
				fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(),
				groupName
			));
		} else if (annoNum > 2) {
			// 如果重复定义, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 @{1}(groupName = \"{2}\") 这个注解重复定义, 这是不允许的!", 
				fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(),
				groupName
			));
		}

		if (foundFL != null) {
			for (Field F : foundFL) {
				if (F != null && 
					Map.class.isAssignableFrom(F.getType()) == findMap) {
					return F;
				}
			};
		}

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
	 * @param annoClazz, 
	 * @param groupName
	 * @return 
	 * 
	 */
	private static boolean hasOneToXAnno(
		Member member, 
		Class<?> annoClazz, 
		String groupName) {
		if (annoClazz.equals(OneToOne.class)) {
			// 看看有没有标注 OnToOne 的
			return hasOneToOne(member, groupName);
		} else if (annoClazz.equals(OneToMany.class)) {
			// 看看有没有标注 OneToMany 的
			return hasOneToMany(member, groupName);
		} else {
			return false;
		}
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
