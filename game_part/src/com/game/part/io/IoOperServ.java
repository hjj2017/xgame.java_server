package com.game.part.io;

import com.game.part.util.Assert;

/**
 * IO 操作处理器
 * 
 * @author haijiang
 * 
 */
public final class IoOperServ {
	/** 单例对象 */
	public static final IoOperServ OBJ = new IoOperServ();
	/** 异步模式 */
	public boolean _asyncMode = false;

	/**
	 * 类默认构造器
	 * 
	 */
	private IoOperServ() {
	}

	/**
	 * 执行游戏内的 IO 操作
	 * 
	 * @param oper
	 * 
	 */
	public void execute(IIoOper oper) {
		// 断言参数不为空
		Assert.notNull(oper);

		if (this._asyncMode) {
			// 工作于同步模式
			SyncIoOperProc.OBJ.execute(oper);
		} else {
			// 工作于异步模式
			AsyncIoOperProc.OBJ.execute(oper);
		}
	}
}
