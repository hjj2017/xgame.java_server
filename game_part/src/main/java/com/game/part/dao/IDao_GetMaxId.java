package com.game.part.dao;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * 获取最大 Id
 *
 * @author hjj2017
 * @since 2015/7/18
 *
 */
interface IDao_GetMaxId {
    /** 获取最大 Id */
    String JPQL_getMaxId = "select entity.{1} from {0} entity";

    /**
     * 删除数据实体
     *
     * @param <TEntity>
     * @param entityClazz
     *
     */
    default<TEntity, TId> TId getMaxId(Class<TEntity> entityClazz) {
        if (entityClazz == null) {
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

        try {
            // 获取 Id 字段名称
            String IdFieldName = CommDao.OBJ.getIdFieldName(entityClazz);
            // 构建 JPQL 查询
            final String jpql = MessageFormat.format(
                JPQL_getMaxId,
                entityClazz.getName(),
                IdFieldName
            );

            // 查询并返回结果
            List<?> resultList = em.createQuery(jpql).getResultList();

            if (resultList == null ||
                resultList.isEmpty()) {
                // 如果结果列表为空,
                // 则直接退出!
                return null;
            }

            // 转型为真实 Id
            @SuppressWarnings("unchecked")
			TId realId = (TId)resultList.get(0);
            // 返回 Id
            return realId;
        } catch (Exception ex) {
            // 记录错误日志
            DaoLog.LOG.error(ex.getMessage(), ex);
        } finally {
            em.close();
        }

        return null;
    }
}
