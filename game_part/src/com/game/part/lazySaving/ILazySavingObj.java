package com.game.part.lazySaving;

/**
 * 延迟保存的对象接口
 * 
 * @author hjj2017
 * @since 2015/4/3
 * 
 * @param <TEntity> 对应的实体类型
 * @param <TThreadEnum> 线程枚举
 * 
 */
public interface ILazySavingObj<TEntity, TThreadEnum extends Enum<TThreadEnum>> {
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
	 * 获取运行线程枚举
	 * 
	 * @return
	 * 
	 */
	TThreadEnum getThreadEnum();

	/**
	 * 获得此持久化业务对象的生命周期
	 * 
	 * @return
	 * 
	 */
	LifeCycle getLifeCycle();

	/**
	 * 保存已修改的部分
	 * 
	 */
	default void saveOrUpdate() {
		LazySavingHelper.OBJ.addUpdate(this);
	}

	/**
	 * 删除当前业务对象
	 * 
	 */
	default void del() {
		LazySavingHelper.OBJ.addDel(this);
	}
}
