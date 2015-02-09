package com.game.part.dao;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Query;

import com.game.part.utils.ClazzUtil;

/**
 * 删除数据库实体
 * 
 * @author hjj2017
 * @since 2014/9/19
 * 
 */
interface IDao_Del {
	/** Id 字段名称字典 */
	ConcurrentHashMap<Class<?>, String> _idFieldNameMap = new ConcurrentHashMap<>();
	/** 删除实体 */
	String JPQL_del = "delete from {0} obj where obj.{1} = :id";

	/**
	 * 删除数据实体
	 * 
	 * @param clazz
	 * @param id
	 * 
	 */
	default void del(Class<?> clazz, Object id) {
		if (clazz == null || 
			id == null) {
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

		// 获取 Id 字段名称
		String idFieldName = getIdFieldName(clazz);
		// 构建 HQL 查询
		final String hql = MessageFormat.format(
			JPQL_del, 
			clazz.getName(), 
			idFieldName
		);

		try {
			// 创建数据库事务
			EntityTransaction tranx = em.getTransaction();
			// 开始事务过程
			tranx.begin();
			// 创建并执行 SQL 查询
			em.createQuery(hql).setParameter("id", id).executeUpdate();
			em.flush();
			// 提交事务
			tranx.commit();
		} catch (Exception ex) {
			// 记录错误日志
			DaoLog.LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 获取标注了 @Id 注解的字段名称
	 * 
	 * @param fromClazz
	 * @return
	 * 
	 */
	static String getIdFieldName(Class<?> fromClazz) {
		if (fromClazz == null) {
			return null;
		}

		// 
		// 首先从字典里找一下这个类对应的 Id 字段名称, 
		// 获取 Id 字段名称
		String idFieldName = _idFieldNameMap.get(fromClazz);

		if (idFieldName != null) {
			// 如果字典里有, 
			// 则直接返回...
			return idFieldName;
		}

		// 
		// 接下来就要处理在字典中没找到的情况,
		// 从类中获取标注了 Id 的字段
		Field idField = ClazzUtil.getField(
			fromClazz, (f) -> { return f != null && f.getAnnotation(Id.class) != null; }
		);

		if (idField == null) {
			// 如果字段为空, 
			// 则抛出异常!
			throw new DaoError(MessageFormat.format(
				"在 {0} 类中没有找到标注了 @Id 注解的字段", fromClazz.getName()
			));
		}

		// 获取字段名称
		idFieldName = idField.getName();
		// 添加 Id 字段名称到字典
		_idFieldNameMap.put(fromClazz, idFieldName);

		return idFieldName;
	}

	/**
	 * 删除数据实体列表
	 * 
	 * @param clazz
	 * @param idList
	 * 
	 */
	default void delAll(Class<?> clazz, List<?> idList) {
		if (clazz == null || 
			idList == null || 
			idList.isEmpty()) {
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

		// 获取 Id 字段名称
		String idFieldName = getIdFieldName(clazz);
		// 构建 HQL 查询
		final String hql = MessageFormat.format(
			JPQL_del, 
			clazz.getName(), 
			idFieldName
		);

		try {
			// 创建数据库事务
			EntityTransaction tranx = em.getTransaction();
			// 开始事务过程
			tranx.begin();
			// 创建并执行 SQL 查询
			Query q = em.createQuery(hql);
	
			idList.forEach(id -> {
				// 设置 Id 参数
				q.setParameter("id", id);
				q.executeUpdate();
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
