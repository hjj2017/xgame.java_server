package com.game.part.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.game.part.utils.Assert;

/**
 * 通用 DAO
 * 
 * @author hjj2019
 * @since 2014/9/16
 * 
 */
public class CommDao implements IDao_Save, IDao_Del, IDao_GetResultList, IDao_GetSingleResult, IDao_ExecNativeSQL {
	/** 单例对象 */
	public static final CommDao OBJ = new CommDao();

	/** 非法线程 Id 集合 */
	private Set<Long> _illegalThreadIdSet = null;
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
	 * @param clazz
	 * @param id
	 * @return 
	 * 
	 */
	public<TEntity> TEntity find(Class<TEntity> clazz, Object id) {
		if (clazz == null || 
			id == null) {
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
			return null;
		}

		// 保存实体
		return em.find(clazz, id);
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
}
