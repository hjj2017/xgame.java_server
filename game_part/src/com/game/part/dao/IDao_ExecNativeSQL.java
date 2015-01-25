package com.game.part.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * 执行本地查询
 * 
 * @author hjj2017
 * @since 2014/9/27
 * 
 */
interface IDao_ExecNativeSQL {
	/**
	 * 执行本地查询
	 * 
	 * @param nativeSQL
	 * @param paramsMap 
	 * @return
	 * 
	 */
	default int execNativeSQL(String nativeSQL, Map<String, Object> paramsMap) {
		if (nativeSQL == null || 
			nativeSQL.isEmpty()) {
			// 如果 SQL 语句为空, 
			// 则直接退出!
			return -1;
		}

		// 获取实体管理器
		EntityManager em = CommDao.OBJ._emf.createEntityManager();

		if (em == null) {
			// 如果实体管理器为空, 
			// 则直接退出!
			return -1;
		}

		// 创建本地查询
		Query q = em.createNativeQuery(nativeSQL);

		if (paramsMap != null && 
			paramsMap.isEmpty() == false) {
			paramsMap.entrySet().forEach(entry -> {
				if (entry == null) {
					// 如果进入点为空, 
					// 则直接退出!
					return;
				}

				// 设置参数
				q.setParameter(entry.getKey(), entry.getValue());
			});
		}

		return q.executeUpdate();
	}
}
