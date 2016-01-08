package com.game.part.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * 获取结果列表
 * 
 * @author hjj2017
 * @since 2014/9/19
 * 
 */
interface IDao_GetResultList {
    /** 选取数据列表 */
    String JPQL_selectFrom = "select entity from {0} entity where {1}";

    /**
     * 获取结果列表
     *
     * @param entityClazz
     * @param jpqlWhere JPQL 查询语句中 where 后面的语句, 注意 : where 语句中需要使用 "entity." 前缀! 例如 : entity._userName
     * @param paramMap
     * @param start
     * @param count
     * @return
     *
     */
    default<TEntity> List<TEntity> getResultList(
        Class<TEntity> entityClazz,
        String jpqlWhere,
        Map<String, Object> paramMap,
        int start,
        int count) {
        if (entityClazz == null ||
            count <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 检查线程 Id
        CommDao.OBJ.checkThreadId();

        // 获取实体管理器
        EntityManager em = CommDao.OBJ._emf.createEntityManager();

        if (em == null) {
            // 如果实体管理器为空,
            // 则直接退出!
            DaoLog.LOG.error("实体管理器为空");
            return null;
        }

        if (jpqlWhere == null) {
            // 如果查询条件为空,
            // 那么给一个永远为真的条件 ...
            jpqlWhere = "0 = 0";
        }

        // 获取 HQL 查询
        final String jpql = MessageFormat.format(JPQL_selectFrom, entityClazz.getName(), jpqlWhere);
        // 创建查询
        Query q = em.createQuery(jpql)
            .setFirstResult(start)
            .setMaxResults(count);

        if (paramMap != null &&
            paramMap.isEmpty() == false) {
            paramMap.entrySet().forEach(entry -> {
                if (entry == null) {
                    // 如果进入点为空,
                    // 则直接退出!
                    return;
                }

                // 设置参数
                q.setParameter(entry.getKey(), entry.getValue());
            });
        }

        @SuppressWarnings("unchecked")
        List<TEntity> objList = q.getResultList();

        // 关闭实体管理器
        em.close();

        return objList;
    }

    /**
     * 获取结果列表
     *
     * @param <TEntity>
     * @param clazz
     * @param hqlWhere
     * @param paramMap
     * @return
     *
     */
    default<TEntity> List<TEntity> getResultList(
        Class<TEntity> clazz,
        String hqlWhere,
        Map<String, Object> paramMap) {
        return this.getResultList(
            clazz, hqlWhere, paramMap, 0, Integer.MAX_VALUE
        );
    }

    /**
     * 获取结果列表
     *
     * @param <TEntity>
     * @param clazz
     * @param hqlWhere
     * @return
     */
    default<TEntity> List<TEntity> getResultList(
        Class<TEntity> clazz,
        String hqlWhere) {
        return this.getResultList(
            clazz, hqlWhere, null, 0, Integer.MAX_VALUE
        );
    }

    /**
     * 获取结果列表
     *
     * @param <TEntity>
     * @param clazz
     * @param start
     * @param count
     * @return
     */
    default<TEntity> List<TEntity> getResultList(
        Class<TEntity> clazz,
        int start,
        int count) {
        return this.getResultList(
            clazz, null, null, start, count
        );
    }

    /**
     * 获取结果列表
     *
     * @param <TEntity>
     * @param clazz
     * @return
     *
     */
    default<TEntity> List<TEntity> getResultList(
        Class<TEntity> clazz) {
        return this.getResultList(
            clazz, null, null, 0, Integer.MAX_VALUE
        );
    }
}
