package com.game.part.io;

import java.text.MessageFormat;

import com.game.part.utils.Assert;

/**
 * 有状态的异步操作
 * 
 * @author haijiang
 * 
 */
class StatefulIoOper<E extends Enum<E>> implements IIoOper {
	/** 异步操作 */
	private IIoOper _innerOper = null;
	/** 线程枚举 */
	private E _threadEnum = null;
	/** 当前状态 */
	private IoOperStateEnum _currState = null;

	/**
	 * 类参数构造器
	 * 
	 * @param oper
	 * @param threadEnum 
	 * 
	 */
	public StatefulIoOper(IIoOper oper, E threadEnum) {
		Assert.notNull(oper);
		this._innerOper = oper;
		this._threadEnum = threadEnum;
	}

	/**
	 * 获取内嵌工作
	 * 
	 * @return
	 */
	public IIoOper getInnerOper() {
		return this._innerOper;
	}

	/**
	 * 获取线程枚举
	 * 
	 * @return 
	 * 
	 */
	public E getThreadEnum() {
		return this._threadEnum;
	}

	/**
	 * 获取当前状态
	 * 
	 * @return
	 */
	public IoOperStateEnum getCurrState() {
		return this._currState;
	}

	/**
	 * 设置当前状态
	 * 
	 * @param value
	 */
	private void setCurrState(IoOperStateEnum value) {
		if (value == null) {
			return;
		}

		this._currState = value;
	}

	@Override
	public boolean doInit() {
		// 记录日志信息
		IoOperLog.LOG.info(MessageFormat.format(
			"StatefulIoOper[name={0}].doInit", 
			this.getThreadEnum().name()
		));

		// 获取执行结果
		boolean result = this._innerOper.doInit();

		// 如果继续向下执行, 
		// 则设置当前状态为: 初始化成功
		this.setCurrState(
			result 
			? IoOperStateEnum.initOk 
			: IoOperStateEnum.exit
		);

		return result;
	}

	@Override
	public boolean doIo() {
		// 记录日志信息
		IoOperLog.LOG.info(MessageFormat.format(
			"StatefulIoOper[name={0}].doAsyncProc", 
			this.getThreadEnum().name()
		));

		// 获取执行结果
		boolean result = this._innerOper.doIo();

		if (!result) {
			// 记录日志信息
			IoOperLog.LOG.error(MessageFormat.format(
				"{0} 异步操作执行失败", 
				this._innerOper.getClass().getSimpleName()
			));
		}

		// 如果执行成功, 
		// 则设置当前状态为: 异步操作完成
		this.setCurrState(
			result 
			? IoOperStateEnum.ioFinished 
			: IoOperStateEnum.exit
		);

		return result;
	}
}
