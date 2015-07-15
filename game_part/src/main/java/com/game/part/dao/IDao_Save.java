package com.game.part.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
	 * @param entityObj
	 * 
	 */
	default void save(Object entityObj) {
		if (entityObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 检查线程 Id
		CommDao.OBJ.checkThreadId();

		// 获取实体管理器
		EntityManager em = CommDao.OBJ._emf.createEntityManager();

		if (em == null) {
			// 如果实体管理器为空, 
			// 则直接退出!
			DaoLog.LOG.error("实体管理器为空");
			return;
		}

		// 获取数据库事务
		EntityTransaction tranx = em.getTransaction();

		try {
			// 开始事务过程
			tranx.begin();
			// 保存实体
			em.merge(entityObj);
			em.flush();
			// 提交事务
			tranx.commit();
		} catch (Exception ex) {
			// 记录错误日志
			DaoLog.LOG.error(ex.getMessage(), ex);
			// 回滚事务
			tranx.rollback();
		} finally {
			em.close();
		}
	}

	/**
	 * 添加数据库实体列表
	 * 
	 * @param entityObjList 
	 * 
	 */
	default<TEntity> void saveAll(List<TEntity> entityObjList) {
		if (entityObjList == null || 
			entityObjList.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 检查线程 Id
		CommDao.OBJ.checkThreadId();

		// 获取实体管理器
		EntityManager em = CommDao.OBJ._emf.createEntityManager();

		if (em == null) {
			// 如果实体管理器为空, 
			// 则直接退出!
			DaoLog.LOG.error("实体管理器为空");
			return;
		}

		// 获取数据库事务
		EntityTransaction tranx = em.getTransaction();

		try {
			// 开始事务过程
			tranx.begin();
			// 保存数据实体
			entityObjList.forEach(newEntity -> em.merge(newEntity));
			em.flush();
			// 提交事务
			tranx.commit();
		} catch (Exception ex) {
			// 记录错误日志
			DaoLog.LOG.error(ex.getMessage(), ex);
			// 回滚事务
			tranx.rollback();
		} finally {
			em.close();
		}
	}
}
