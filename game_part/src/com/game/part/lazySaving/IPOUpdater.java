package com.game.core.persistance;


/**
 * 可持久化业务对象更新器
 * 
 * @author hjj2017
 * @since 2015/3/31
 * @see IPersistanceObject
 * 
 */
public interface IPOUpdater {
	/**
	 * 保存
	 * 
	 * @param po
	 * 
	 */
	public void save(IPersistanceObject<?, ?> po);

	/**
	 * 删除
	 * 
	 * @param po
	 * 
	 */
	public void delete(IPersistanceObject<?, ?> po);
}
