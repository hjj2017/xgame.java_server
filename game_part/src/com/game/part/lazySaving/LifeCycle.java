package com.game.core.persistance;

import java.io.Serializable;
import java.text.MessageFormat;

import org.springframework.util.Assert;

/**
 * 对象的生命周期状态
 * 
 * @author Unknown
 * @since 2015/3/31
 * 
 */
public class LifeCycleImpl implements ILifeCycle {
	/** 当前状态 */
	private LifeCycleStateEnum _currState = LifeCycleStateEnum.deactive;
	/** 所指向的 PO 对象 */
	private IPersistanceObject<?, ?> _po;

	/**
	 * 类参数构造器
	 * 
	 * @param po
	 * 
	 */
	public LifeCycleImpl(IPersistanceObject<?, ?> po) {
		// 断言参数对象不为空
		Assert.notNull(po, "po");
		// 设置 PO 对象
		this._po = po;
	}

	@Override
	public boolean isActive() {
		return this._currState == LifeCycleStateEnum.active;
	}

	@Override
	public void activate() {
		if (this._currState == LifeCycleStateEnum.destoryed) {
			// 如果已经处于销毁状态, 
			// 则直接抛出异常!
			throw new IllegalStateException(MessageFormat.format(
				"对象 ( key = {0} ) 已经处于销毁状态, 无法激活", 
				String.valueOf(this.getKey())
			));
		}

		this._currState = LifeCycleStateEnum.active;
	}

	@Override
	public void deactivate() {
		if (this._currState == LifeCycleStateEnum.destoryed) {
			// 如果已经处于销毁状态, 
			// 则直接抛出异常!
			throw new IllegalStateException(MessageFormat.format(
				"对象 ( key = {0} ) 已经处于销毁状态, 无法注销", 
				String.valueOf(this.getKey())
			));
		}

		this._currState = LifeCycleStateEnum.deactive;
	}

	@Override
	public void checkModifiable() {
		if (this._currState == LifeCycleStateEnum.destoryed) {
			// 如果已经处于销毁状态, 
			// 则直接抛出异常!
			throw new IllegalStateException(MessageFormat.format(
				"对象 ( key = {0} ) 已经处于销毁状态, 无法被修改", 
				String.valueOf(this.getKey())
			));
		}
	}

	@Override
	public void destroy() {
		this._currState = LifeCycleStateEnum.destoryed;
	}

	@Override
	public boolean isDestroyed() {
		return this._currState == LifeCycleStateEnum.destoryed;
	}

	@Override
	public Serializable getKey() {
		return this._po.getGUID();
	}

	@Override
	public IPersistanceObject<?, ?> getPO() {
		return this._po;
	}
}
