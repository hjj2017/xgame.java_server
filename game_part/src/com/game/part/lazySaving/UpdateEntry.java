package com.game.core.persistance;

import org.springframework.util.Assert;

/**
 * 更新入口
 * 
 * @author hjj2017
 * @since 2015/3/30
 * 
 */
public final class UpdateEntry {
	/** 插入或更新操作 */
	static final int OPER_INSERT_OR_UPDATE = 1;
	/** 删除操作 */
	static final int OPER_DELETE = 2;

	/** 操作类型 */
	private int _oper;
	/** 待更新的业务对象引用O */
	final ILifeCycle _lifeCycle;
	/** 最后修改时间 */
	long _lastModifiedTime = 0L;

	/**
	 * 类参数构造器
	 * 
	 * @param lc
	 * 
	 */
	private UpdateEntry(ILifeCycle lc) {
		// 断言参数不为空
		Assert.notNull(lc, "lc");
		// 设置 LifeCycle
		this._lifeCycle = lc;
	}

	/**
	 * 创建 insertOrUpdate 更新入口
	 * 
	 * @param lc
	 * @param nowTime 
	 * @return
	 * 
	 */
	static UpdateEntry createInsertOrUpdateEntry(ILifeCycle lc, long nowTime) {
		// 断言参数不为空
		Assert.notNull(lc, "lc");
		// 创建更新入口
		UpdateEntry entry = new UpdateEntry(lc);
		// 设置为更新操作
		entry._oper = OPER_INSERT_OR_UPDATE;
		// 设置当前时间
		entry._lastModifiedTime = nowTime;
		
		// 返回入口对象
		return entry;
	}

	/**
	 * 创建 delete 更新入口
	 * 
	 * @param lc
	 * @param nowTime
	 * @return
	 * 
	 */
	static UpdateEntry createDeleteEntry(ILifeCycle lc, long nowTime) {
		// 断言参数不为空
		Assert.notNull(lc, "lc");
		// 创建更新入口
		UpdateEntry entry = new UpdateEntry(lc);
		// 设置为更新操作
		entry._oper = OPER_DELETE;
		// 设置当前时间
		entry._lastModifiedTime = nowTime;

		return entry;
	}

	/**
	 * 是插入或更新操作 ?
	 * 
	 * @param entry
	 * @return 
	 * 
	 */
	static boolean isInsertOrUpdateOper(UpdateEntry entry) {
		if (entry == null) {
			return false;
		} else {
			return entry._oper == OPER_INSERT_OR_UPDATE;
		}
	}
}
