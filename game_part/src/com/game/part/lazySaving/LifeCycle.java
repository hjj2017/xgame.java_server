package com.game.part.lazySaving;

import java.text.MessageFormat;

import com.game.part.util.Assert;

/**
 * 对象的生命周期状态
 * 
 * @author Unknown
 * @since 2015/3/31
 * 
 */
public class LifeCycle {
	/** 当前状态, 默认为冬眠状态 */
	LifeCycleStateEnum _currState = LifeCycleStateEnum.hibernate;
	/** 所指向的业务对象 */
	ILazySavingObj<?, ?> _lazySavingObj;

	/**
	 * 类参数构造器
	 * 
	 * @param lso
	 * 
	 */
	public LifeCycle(ILazySavingObj<?, ?> lso) {
		// 断言参数对象不为空
		Assert.notNull(lso, "lso");
		// 设置业务对象
		this._lazySavingObj = lso;
	}

	/**
	 * 令对象进入活跃状态, 
	 * 此时该对象才可以参与保存操作
	 * 
	 */
	public void activate() {
		if (this._currState == LifeCycleStateEnum.destoryed) {
			// 如果已经处于销毁状态, 
			// 则直接抛出异常!
			throw new IllegalStateException(MessageFormat.format(
				"对象 ( key = {0} ) 已经处于销毁状态, 无法激活", 
				this._lazySavingObj.getUId()
			));
		}

		this._currState = LifeCycleStateEnum.active;
	}

	/**
	 * 令对象进入冬眠状态, 
	 * 此时该对象将不参与保存或删除操作
	 * 
	 */
	public void hibernate() {
		if (this._currState == LifeCycleStateEnum.destoryed) {
			// 如果已经处于销毁状态, 
			// 则直接抛出异常!
			throw new IllegalStateException(MessageFormat.format(
				"对象 ( key = {0} ) 已经处于销毁状态, 无法注销", 
				this._lazySavingObj.getUId()
			));
		}

		this._currState = LifeCycleStateEnum.hibernate;
	}

	/**
	 * 销毁对象, 此时对象将被删除
	 * 
	 */
	void destroy() {
		this._currState = LifeCycleStateEnum.destoryed;
	}

	/**
	 * 获取业务对象的 UId
	 * 
	 * @return 
	 * 
	 */
	String getUId() {
		return this._lazySavingObj.getUId();
	}
}
