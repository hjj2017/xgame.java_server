package com.game.part.tmpl;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.game.part.tmpl.anno.OneToOne;
import com.game.part.utils.Assert;

/**
 * OneToXDefPair_X
 * 
 * @author hjj2019
 * @since 2015/2/28
 * 
 */
class OneToXDefPair_X {
	/** 分组名称 */
	private String _groupName;
	/** 来自于哪个类 ? */
	private Class<?> _fromClazz;
	/** 注解类集合 */
	final Set<Class<?>> _annoClazzSet = new HashSet<>();
	/** 成员集合 */
	final Set<Member> _memberSet = new HashSet<>();
	/** 关键字定义 */
	private Member _keyDef;
	/** 字典定义 */
	private Member _mapDef;

	/**
	 * 类参数构造器
	 * 
	 * @param groupName
	 * @param fromClazz 
	 * 
	 */
	OneToXDefPair_X(String groupName, Class<?> fromClazz) {
		// 断言参数不为空
		Assert.notNull(groupName, "groupName");
		Assert.notNull(fromClazz, "fromClazz");
		// 设置属性
		this._groupName = groupName;
		this._fromClazz = fromClazz;
	}

	/**
	 * 获取关键字定义
	 * 
	 * @return 
	 * 
	 */
	public Member getKeyDef() {
		return this._keyDef;
	}

	/**
	 * 获取字典定义
	 * 
	 * @return 
	 * 
	 */
	public Member getMapDef() {
		return this._mapDef;
	}

	/**
	 * 是否为 OneToOne 类型
	 * 
	 * @return 
	 * 
	 */
	public boolean isOneToOne() {
		// 获取注解类
		Class<?> annoClazz = this._annoClazzSet.stream().findFirst().orElse(null);
		// 注解类不为空并且是 OneToOne 类型
		return annoClazz != null 
			&& annoClazz.equals(OneToOne.class);
	}

	/**
	 * 验证
	 * 
	 */
	void validate() {
		if (this._annoClazzSet.isEmpty() || 
			this._memberSet.isEmpty()) {
			// 如果注解集合或成员集合为空, 
			// 则直接退出!
			XlsxTmplLog.LOG.warn("注解类集合或成员集合为空");
			return;
		}

		// 获取注解类数量
		final int annoClazzNum = this._annoClazzSet.size();
		// 获取注解类
		final Class<?> annoClazz = this._annoClazzSet.stream().findFirst().get();

		if (annoClazzNum > 1) {
			// 如果同时存在两个以上不同类型的注解, 
			// 则直接抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 groupName = \"{1}\" 的注解类型不一致! 一个是 OneToOne 而另一个是 OneToMany", 
				this._fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(),
				this._groupName
			));
		}

		// 获取成员数量
		final int memberNum = this._memberSet.size();

		if (memberNum == 1) {
			// 如果不是成对儿出现, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 @{1}(groupName = \"{2}\") 这个注解不是成对儿出现的!", 
				this._fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(),
				this._groupName
			));
		} else if (memberNum > 2) {
			// 如果重复定义, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 @{1}(groupName = \"{2}\") 这个注解重复定义, 这是不允许的!", 
				this._fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(),
				this._groupName
			));
		}

		// 设置关键字定义和字典定义
		this._keyDef = findMember(_memberSet, false);
		this._mapDef = findMember(_memberSet, true);
		// 验证关键字和字典
		this.validate_keyDef();
		this.validate_mapDef();
	}

	/**
	 * 查找成员
	 * 
	 * @param fromClazz
	 * @param groupName
	 * @param findMap
	 * @return 
	 * 
	 */
	private static Member findMember(Set<Member> ms, boolean findMap) {
		// 断言参数不为空
		Assert.notNull(ms, "ms");
		
		return ms.stream().filter(m -> {
			// 定义成员类型
			final Class<?> mType;
			
			if (m instanceof Field) {
				// 转型为字段
				mType = ((Field)m).getType();
			} else if (m instanceof Method) {
				// 转型为函数
				mType = ((Method)m).getReturnType();
			} else {
				// 如果不是字段也不是函数, 
				// 则直接退出!
				return false;
			}

			// 是否继承自 Map 类型?
			return Map.class.isAssignableFrom(mType) == findMap;
		}).findFirst().orElse(null);
	}

	/**
	 * 验证 Key 定义
	 * 
	 */
	private void validate_keyDef() {
		// 获取成员定义
		Member m = this._keyDef;
		// 获取注解类
		final Class<?> annoClazz = this._annoClazzSet.stream().findFirst().get();

		if (m == null) {
			// 如果成员为空
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中没有 ( 非 Map 类型的 ) 字段或方法标注 @{1}(groupName = \"{2}\") 注解", 
				this._fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(), 
				this._groupName
			));
		}
	}

	/**
	 * 验证 Map 定义
	 * 
	 */
	private void validate_mapDef() {
		// 获取 Map 定义
		Member m = this._mapDef;
		// 获取注解类
		final Class<?> annoClazz = this._annoClazzSet.stream().findFirst().get();

		if (m == null) {
			// 如果成员为空, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中没有 ( Map 类型的 ) 字段或方法标注 @{1}(groupName = \"{2}\") 注解", 
				this._fromClazz.getSimpleName(), 
				annoClazz.getSimpleName(), 
				this._groupName
			));
		}
		
		if (m instanceof Method) {
			if ((m.getModifiers() & Modifier.PUBLIC) == 0 ||
				(m.getModifiers() & Modifier.STATIC) == 0) {
				// 如果不是公共的静态方法, 
				// 则抛出异常!
				throw new XlsxTmplError(MessageFormat.format(
					"{0} 类方法 {1} 不是公共的、静态的, 这是不允许的!, 请使用 public static {2} {1}() { return ...; } 这样的代码", 
					this._fromClazz.getSimpleName(), 
					m.getName(),
					((Method)m).getReturnType().getSimpleName()
				));
			}

			if (Map.class.isAssignableFrom(((Method)m).getReturnType()) == false) {
				throw new XlsxTmplError("不是 Map 类型");
			}

			try {
				// 获取函数返回值
				Object obj = ((Method)m).invoke(m.getDeclaringClass());
	
				if (obj == null) {
					// 如果返回值为空, 
					// 则抛出异常!
					throw new XlsxTmplError(MessageFormat.format(
						"{0} 类静态方法 {1} 返回值为空, 这是不允许的!", 
						this._fromClazz.getSimpleName(), 
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
					this._fromClazz.getSimpleName(), 
					m.getName(), 
					((Field)m).getType().getSimpleName()
				));
			}

			if (Map.class.isAssignableFrom(((Field)m).getType()) == false) {
				throw new XlsxTmplError("不是 Map 类型");
			}
			
			try {
				// 获取字段值
				Object obj = ((Field)m).get(this._fromClazz);
				
				if (obj == null) {
					// 如果返回值为空, 
					// 则抛出异常!
					throw new XlsxTmplError(MessageFormat.format(
						"{0} 类静态字段 {1} 为空值, 这是不允许的!", 
						this._fromClazz.getSimpleName(), 
						m.getName()
					));
				}
			} catch (Exception ex) {
				// 抛出异常
				throw new XlsxTmplError(ex);
			}
		}
	}
}
