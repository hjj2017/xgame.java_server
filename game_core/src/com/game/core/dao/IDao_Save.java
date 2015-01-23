package com.game.core.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.game.core.entity.GenericEntity;

/**
 * 保存数据库实体
 * 
 * @author hjj2017
 * @since 2014/9/19
 * 
 */
interface IDao_Save {
	/**
	 * 添加数据库实体
	 * 
	 * @param <TEntity>
	 * @param e
	 * 
	 */
	default<TEntity extends GenericEntity<?>> void save(TEntity e) {
		if (e == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取实体管理器
		EntityManager em = CommDao.OBJ._emf.createEntityManager();

		if (em == null) {
			// 如果实体管理器为空, 
			// 则直接退出!
			return;
		}

		try {
			// 获取数据库事务
			EntityTransaction tranx = em.getTransaction();
			// 开始事务过程
			tranx.begin();
			// 保存实体
			em.merge(e);
			em.flush();
			// 提交事务
			tranx.commit();
		} catch (Exception ex) {
			// 记录错误日志
			DaoLog.LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 添加数据库实体列表
	 * 
	 * @param <TEntity>
	 * @param el 
	 * 
	 */
	default<TEntity extends GenericEntity<?>> void saveAll(List<TEntity> el) {
		if (el == null || 
			el.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取实体管理器
		EntityManager em = CommDao.OBJ._emf.createEntityManager();

		if (em == null) {
			// 如果实体管理器为空, 
			// 则直接退出!
			return;
		}

		try {
			// 获取数据库事务
			EntityTransaction tranx = em.getTransaction();
			// 开始事务过程
			tranx.begin();

			el.forEach(newEntity -> {
				// 保存实体
				em.merge(newEntity);
			});
	
			em.flush();
			// 提交事务
			tranx.commit();
		} catch (Exception ex) {
			// 记录错误日志
			DaoLog.LOG.error(ex.getMessage(), ex);
		}
	}
}
