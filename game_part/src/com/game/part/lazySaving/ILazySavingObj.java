package com.game.core.persistance;

import java.io.Serializable;

import com.game.core.orm.IBaseEntity;

/**
 * 可持久化的业务对象实现此接口
 * 
 * @param <TId> 主键类型
 * @param <TEntity> 对应的实体类型
 * 
 */
public interface IPersistanceObject<TId extends Serializable, TEntity extends IBaseEntity<TId>> {
	/**
	 * 设置数据库 Id
	 * 
	 * @param value
	 * 
	 */
	void setDbId(TId value);

	/**
	 * 获取数据库 Id
	 * 
	 * @return
	 * 
	 */
	TId getDbId();

	/**
	 * 获取全局 Id
	 * 
	 * @return
	 * 
	 */
	String getGUID();

	/**
	 * 数据对象是否已经在数据库中?
	 * 
	 * @return
	 * 
	 */
	boolean isInDb();

	/**
	 * 设置对象已经在数据库中了
	 * 
	 * @param
	 * 
	 */
	void setInDb(boolean value);

	/**
	 * 取得该对象所属的角色 Id
	 * 
	 * @return
	 * 
	 */
	long getCharId();

	/**
	 * 将业务对象转为实体对象
	 * 
	 * @return
	 * 
	 */
	TEntity toEntity();

	/**
	 * 从实体对象转换到业务对象
	 * 
	 */
	void fromEntity(TEntity e);

	/**
	 * 获得此持久化业务对象的生命周期
	 * 
	 * @return
	 * 
	 */
	ILifeCycle getLifeCycle();

	/**
	 * 设置当前对象为已修改状态
	 * 
	 */
	void setModified();
}
