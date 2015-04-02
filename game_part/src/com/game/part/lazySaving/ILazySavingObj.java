package com.game.part.lazySaving;

/**
 * 延迟保存的对象接口
 * 
 * @param <TEntity> 对应的实体类型
 * 
 */
public interface ILazySavingObj<TEntity> {
	/**
	 * 获取全局 Id
	 * 
	 * @return
	 * 
	 */
	String getUId();

	/**
	 * 取得分组 UId
	 * 
	 * @return
	 * 
	 */
	String getGroupUId();

	/**
	 * 将业务对象转为数据库实体对象
	 * 
	 * @return
	 * 
	 */
	TEntity toEntity();

	/**
	 * 获得此持久化业务对象的生命周期
	 * 
	 * @return
	 * 
	 */
	LifeCycle getLifeCycle();

	/**
	 * 保存已修改的数据
	 * 
	 */
	void saveModified();
}
