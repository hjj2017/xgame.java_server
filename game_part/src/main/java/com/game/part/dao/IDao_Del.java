package com.game.part.dao;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * 删除数据库实体
 * 
 * @author hjj2017
 * @since 2014/9/19
 * 
 */
interface IDao_Del {
    /** 删除实体 */
    String JPQL_del = "delete from {0} entity where entity.{1} = :Id";

    /**
     * 删除数据实体
     *
     * @param <TEntity>
     * @param entityObj
     *
     */
    default<TEntity> void del(TEntity entityObj) {
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

        // 定义数据库事务对象
        EntityTransaction tranx = em.getTransaction();

        try {
            // 开始事务过程
            tranx.begin();
            // 删除实体数据
            em.remove(entityObj);
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
     * 删除数据实体
     *
     * @param <TEntity>
     * @param entityClazz
     * @param Id
     *
     */
    default<TEntity> void del(Class<TEntity> entityClazz, Object Id) {
        if (entityClazz == null ||
            Id == null) {
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

        // 定义数据库事务对象
        EntityTransaction tranx = em.getTransaction();

        // 获取 Id 字段名称
        String IdFieldName = CommDao.OBJ.getIdFieldName(entityClazz);
        // 构建 JPQL 查询
        final String jpql = MessageFormat.format(
            JPQL_del,
            entityClazz.getName(),
            IdFieldName
        );

        try {
            // 开始事务过程
            tranx.begin();
            // 创建并执行 SQL 查询
            em.createQuery(jpql).setParameter("Id", Id).executeUpdate();
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
     * 删除数据实体列表,
     * <font color='#990000'>注意 : 删除过程是以事务方式进行的! 如果其中出现失败, 则该操作会全部回滚!</font>
     *
     * @param <TEntity>
     * @param entityClazz
     * @param IdList
     *
     */
    default<TEntity> void delAll(Class<TEntity> entityClazz, List<?> IdList) {
        if (entityClazz == null ||
            IdList == null ||
            IdList.isEmpty()) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取实体管理器
        EntityManager em = CommDao.OBJ._emf.createEntityManager();

        if (em == null) {
            // 如果实体管理器为空,
            // 则直接退出!
            DaoLog.LOG.error("实体管理器为空");
            return;
        }

        // 获取 Id 字段名称
        String IdFieldName = CommDao.OBJ.getIdFieldName(entityClazz);
        // 构建 HQL 查询
        final String hql = MessageFormat.format(
            JPQL_del,
            entityClazz.getName(),
            IdFieldName
        );

        // 创建数据库事务
        EntityTransaction tranx = em.getTransaction();

        try {
            // 开始事务过程
            tranx.begin();
            // 创建并执行 SQL 查询
            Query q = em.createQuery(hql);

            IdList.forEach(Id -> {
                // 设置 Id 参数
                q.setParameter("Id", Id);
                q.executeUpdate();
            });

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
