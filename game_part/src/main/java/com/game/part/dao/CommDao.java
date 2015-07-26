package com.game.part.dao;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;

import com.game.part.util.Assert;
import com.game.part.util.ClazzUtil;

/**
 * 通用 DAO
 * 
 * @author hjj2019
 * @since 2014/9/16
 * 
 */
public final class CommDao implements IDao_Save, IDao_Del, IDao_GetResultList, IDao_GetSingleResult, IDao_GetMaxId {
	/** 单例对象 */
	public static final CommDao OBJ = new CommDao();

	/** 非法线程 Id 集合 */
	private Set<Long> _illegalThreadIdSet = null;
	/** Id 字段名称字典 */
	private final Map<Class<?>, String> _IdFieldNameMap = new ConcurrentHashMap<>();
	/** 实体管理器工厂 */
	EntityManagerFactory _emf = null;

	/**
	 * 类参数构造器
	 * 
	 */
	private CommDao() {
	}

	/**
	 * 设置实体管理器工厂
	 * 
	 * @param value
	 * @return 
	 * 
	 */
	public CommDao putEMF(EntityManagerFactory value) {
		// 断言参数不为空
		Assert.notNull(value);

		if (this._emf != null) {
			throw new DaoError("重复设置实体管理器工厂");
		}

		this._emf = value;
		return this;
	}

	/**
	 * 设置非法的线程 Id, 
	 * 即调用 CommDao 时当前线程 Id 不能出现在非法线程列表里...
	 * 
	 * @param threadId
	 * @return
	 * 
	 */
	public CommDao putIllegalThreadId(long ... threadId) {
		if (threadId == null || 
			threadId.length <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			return this;
		}

		if (this._illegalThreadIdSet == null) {
			this._illegalThreadIdSet = new HashSet<>();
		}

		for (long illegalThreadId : threadId) {
			this._illegalThreadIdSet.add(illegalThreadId);
		}

		return this;
	}

	/**
	 * 查找数据库实体
	 * 
	 * @param <TEntity> 
	 * @param entityClazz
	 * @param Id
	 * @return 
	 * 
	 */
	public<TEntity> TEntity find(Class<TEntity> entityClazz, Object Id) {
		if (entityClazz == null || 
			Id == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 检查线程 Id
		this.checkThreadId();

		// 获取实体管理器
		EntityManager em = this._emf.createEntityManager();

		if (em == null) {
			// 如果实体管理器为空, 
			// 则直接退出!
			DaoLog.LOG.error("实体管理器为空");
			return null;
		}

		// 获取实体
		TEntity entityObj = em.find(entityClazz, Id);
		// 关闭实体管理器
		em.close();

		return entityObj;
	}

	/**
	 * 检查线程 Id
	 * 
	 */
	void checkThreadId() {
		// 获取当前线程 Id
		final long threadId = Thread.currentThread().getId();

		if (this._illegalThreadIdSet != null && 
			this._illegalThreadIdSet.contains(threadId)) {
			// 如果是在非法线程中执行操作, 
			// 则直接抛出异常!
			throw new DaoError("在非法线程中使用 CommDao 这是不允许的");
		}
	}

	/**
	 * 获取标注了 @Id 注解的字段名称
	 *
	 * @param fromClazz
	 * @return
	 *
	 */
	String getIdFieldName(Class<?> fromClazz) {
		if (fromClazz == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return null;
		}

		//
		// 首先从字典里找一下这个类对应的 Id 字段名称,
		// 获取 Id 字段名称
		String IdFieldName = this._IdFieldNameMap.get(fromClazz);

		if (IdFieldName != null) {
			// 如果字典里有,
			// 则直接返回...
			return IdFieldName;
		}

		//
		// 接下来就要处理在字典中没找到的情况,
		// 从类中获取标注了 Id 的字段
		Field idField = ClazzUtil.getField(
			fromClazz, f -> f != null && f.getAnnotation(Id.class) != null
		);

		if (idField == null) {
			// 如果字段为空,
			// 则抛出异常!
			throw new DaoError(MessageFormat.format(
				"在 {0} 类中没有找到标注了 @Id 注解的字段", fromClazz.getName()
			));
		}

		// 获取字段名称
		IdFieldName = idField.getName();
		// 添加 Id 字段名称到字典
		this._IdFieldNameMap.put(fromClazz, IdFieldName);

		return IdFieldName;
	}
}
