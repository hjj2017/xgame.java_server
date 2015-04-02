package com.game.core.persistance;

import java.io.Serializable;

/**
 * 有生命周期的对象接口
 * 
 * @author Unknown
 * @since 2015/3/31
 * 
 */
public interface ILifeCycle {
	/**
	 * 取得该对象的 key, 
	 * 注意 : 每个对象的 key 在系统全局应该是惟一的!
	 * 
	 * @return
	 * 
	 */
	public Serializable getKey();

	/**
	 * 检查对象的生命期是否是活动状态
	 * 
	 * @return 
	 * 
	 */
	public boolean isActive();

	/**
	 * 检查对象的生命周是否是销毁状态
	 * 
	 * @return 
	 * 
	 */
	public boolean isDestroyed();

	/**
	 * 检查当前的状态是否是可以修改的
	 * 
	 */
	public void checkModifiable();

	/**
	 * 将对象的生命期设置为活动
	 * 
	 */
	public void activate();

	/**
	 * 将对象的生命期设置为非活动
	 * 
	 */
	public void deactivate();

	/**
	 * 将生命期设置为死亡, 
	 * 注意 : 对象一旦进入到此状态, 对象应该不可以再进行修改!
	 * 
	 */
	public void destroy();

	/**
	 * 获取存储对象
	 * 
	 * @return 
	 * 
	 */
	public IPersistanceObject<?, ?> getPO();
}
