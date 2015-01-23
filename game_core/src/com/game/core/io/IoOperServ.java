package com.game.core.io;

import com.game.core.utils.Assert;

/**
 * IO 操作处理器
 * 
 * @author haijiang
 * 
 */
public class IoOperServ<E extends Enum<E>> {
	/** 异步模式 */
	private boolean _asyncMode = false;
	/** IO 操作过程 */
	private IIoOperProcedure<IIoOper, E> _operProc = null;

	/**
	 * 类参数构造器, 默认工作模式为同步
	 * 
	 * @param threadEnums 
	 * 
	 */
	public IoOperServ(E[] threadEnums) {
		this(false, threadEnums);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param asyncMode 
	 * @param threadEnums 
	 * 
	 * @see IoOperModeEnum  
	 * 
	 */
	public IoOperServ(boolean asyncMode, E[] threadEnums) {
		if (asyncMode) {
			// 异步工作方式
			this._asyncMode = true;
			this._operProc = new AsyncIoOperProcedure<E>(this, threadEnums);
		} else {
			// 同步工作方式
			this._asyncMode = false;
			this._operProc = new SyncIoOperProcedure<E>();
		}
	}

	/**
	 * 是否工作于异步模式 ?
	 * 
	 */
	public boolean isAsyncMode() {
		return this._asyncMode;
	}

	/**
	 * 执行游戏内的 IO 操作
	 * 
	 * @param oper
	 * @param threadEnum 
	 * 
	 */
	public void execute(IIoOper oper, E threadEnum) {
		Assert.notNull(oper);
		Assert.notNull(threadEnum);
		this._operProc.execute(oper, threadEnum);
	}
}
