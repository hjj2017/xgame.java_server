package com.game.part.lazySaving;

import com.game.part.util.Assert;

/**
 * 更新入口
 * 
 * @author hjj2017
 * @since 2015/3/30
 * 
 */
final class UpdateEntry {
	/** 保存或更新操作 */
	static final int OPT_saveOrUpdate = 1;
	/** 删除操作 */
	static final int OPT_del = 2;

	/** 待更新的业务对象引用 */
	final LifeCycle _lifeCycle;
	/** 操作类型 */
	final int _operTypeInt;
	/** 最后修改时间 */
	long _lastModifiedTime = 0L;

	/**
	 * 类参数构造器
	 * 
	 * @param lc
	 * @param operTypeInt
	 * 
	 */
	UpdateEntry(LifeCycle lc, int operTypeInt) {
		// 断言参数不为空
		Assert.notNull(lc, "lc");
		// 设置 LifeCycle
		this._lifeCycle = lc;
		// 设置操作类型数值
		this._operTypeInt = operTypeInt;
	}
}
