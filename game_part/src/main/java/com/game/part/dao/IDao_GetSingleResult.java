package com.game.part.dao;

import java.util.List;
import java.util.Map;

/**
 * 获取单个结果
 *
 * @author hjj2017
 * @since 2014/9/19
 */
interface IDao_GetSingleResult {
    /**
     * 获取单个结果
     *
     * @param entityClazz 实体类
     * @param jpqlWhere   JPQL 查询语句中 where 后面的语句, 注意 : where 语句中需要使用 "entity." 前缀! 例如 : entity._userName
     * @param paramMap    参数字典
     * @param <TEntity>   实体类型
     * @return 实体对象
     * @see IDao_GetResultList#getResultList(Class, String, Map, int, int)
     */
    default <TEntity> TEntity getSingleResult(
        Class<TEntity> entityClazz,
        String jpqlWhere,
        Map<String, Object> paramMap) {
        // 获取实体列表
        List<TEntity> el = CommDao.OBJ.getResultList(entityClazz, jpqlWhere, paramMap, 0, 1);

        if (el == null ||
            el.isEmpty()) {
            return null;
        } else {
            return el.get(0);
        }
    }

    /**
     * 获取单个结果
     *
     * @param entityClazz 实体类
     * @param jpqlWhere   查询条件
     * @param <TEntity>   实体类型
     * @return 实体对象
     * @see #getSingleResult(Class, String, Map)
     */
    default <TEntity> TEntity getSingleResult(
        Class<TEntity> entityClazz,
        String jpqlWhere) {
        return this.getSingleResult(entityClazz, jpqlWhere, null);
    }

    /**
     * 获取单个结果
     *
     * @param entityClazz 实体类
     * @param <TEntity>   实体类型
     * @return 实体对象
     * @see #getSingleResult(Class, String, Map)
     */
    default <TEntity> TEntity getSingleResult(
        Class<TEntity> entityClazz) {
        return this.getSingleResult(entityClazz, null, null);
    }
}
