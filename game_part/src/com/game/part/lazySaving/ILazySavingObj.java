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
	 * 获取分组 UId
	 * 
	 * @return
	 * 
	 */
	default String getGroupUId() {
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
	 * 获取运行线程枚举
	 * 
	 * @return
	 * 
	 */
	TThreadEnum getThreadEnum();

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
