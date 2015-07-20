package com.game.part.lazySaving;

/**
 * 延迟保存的对象接口
 * 
 * @author hjj2017
 * @since 2015/4/3
 * 
 * @param <TEntity> 对应的实体类型
 * 
 */
public interface ILazySavingObj<TEntity> {
	/**
	 * 获取存储关键字
	 * 
	 * @return
	 * 
	 */
	String getStoreKey();

	/**
	 * 获取分组关键字
	 * 
	 * @return
	 * 
	 */
	default String getGroupKey() {
		return null;
	}

	/**
	 * 将业务对象转为数据库实体对象
	 * 
	 * @return
	 * 
	 */
	TEntity toEntity();

	/**
	 * 获取运行线程关键字
	 * 
	 * @return
	 * 
	 */
	String getThreadKey();

	/**
	 * 保存已修改的部分
	 * 
	 */
	default void saveOrUpdate() {
		// 通过帮助类执行更新操作
		LazySavingHelper.OBJ.addUpdate(this);
	}

	/**
	 * 删除当前业务对象
	 * 
	 */
	default void del() {
		// 通过帮助类执行删除操作
		LazySavingHelper.OBJ.addDel(this);
	}
}
