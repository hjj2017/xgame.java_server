package com.game.core.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.game.core.entity.GenericEntity;
import com.game.core.utils.Assert;

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
	 * 查找数据库实体
	 * 
	 * @param <TEntity> 
	 * @param clazz
	 * @param id
	 * @return 
	 * 
	 */
	public<TEntity extends GenericEntity<?>> TEntity find(Class<TEntity> clazz, Object id) {
		if (clazz == null || 
			id == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

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
}
