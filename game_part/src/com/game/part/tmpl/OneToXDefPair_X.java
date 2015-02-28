package com.game.part.tmpl;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	String _groupName;
	/** 注解类 */
	Class<?> _annoClazz;
	/** 成员集合 */
	final Set<Member> _memberSet = new HashSet<>();

	/** 定义类 */
	private Class<?> _fromClazz;
	/** 关键字定义 */
	private Member _keyDef;
	/** 字典定义 */
	private Member _mapDef;

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
	 * 验证
	 * 
	 */
	void validate() {
		// 获取成员数量
		final int memberNum = this._memberSet.size();

		if (this._groupName == null || 
			this._groupName.isEmpty() || 
			this._memberSet.isEmpty()) {
			return;
		}

		// 获取第一个成员
		final Member firstMember = this._memberSet.stream().findFirst().get();
		// 获取定义该成员的类
		final Class<?> fromClazz = firstMember.getDeclaringClass();
		this._fromClazz = fromClazz;

		if (memberNum == 1) {
			// 如果不是成对儿出现, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 @{1}(groupName = \"{2}\") 这个注解不是成对儿出现的!", 
				fromClazz.getSimpleName(), 
				this._annoClazz.getSimpleName(),
				this._groupName
			));
		} else if (memberNum > 2) {
			// 如果重复定义, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中 @{1}(groupName = \"{2}\") 这个注解重复定义, 这是不允许的!", 
				fromClazz.getSimpleName(), 
				this._annoClazz.getSimpleName(),
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
		// 获取定义该成员的类
		final Class<?> fromClazz = this._fromClazz;

		if (m == null) {
			// 如果成员为空
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中没有 ( 非 Map 类型的 ) 字段或方法标注 @{1}(groupName = \"{2}\") 注解", 
				fromClazz.getSimpleName(), 
				this._annoClazz.getSimpleName(), 
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
		// 获取定义该成员的类
		Class<?> fromClazz = this._fromClazz;

		if (m == null) {
			// 如果成员为空, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"{0} 类中没有 ( Map 类型的 ) 字段或方法标注 @{1}(groupName = \"{2}\") 注解", 
				fromClazz.getSimpleName(), 
				this._annoClazz.getSimpleName(), 
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
					fromClazz.getName(), 
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

			if (Map.class.isAssignableFrom(((Field)m).getType()) == false) {
				throw new XlsxTmplError("不是 Map 类型");
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
	}
}
