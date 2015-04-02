package com.game.part.lazySaving;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.io.IoOperServ;

/**
 * 数据更新器
 * 
 * @author haijiang.jin
 * @since 2015/3/30
 * 
 */
public final class LazySavingHelper {
	/** 单例对象 */
	public static final LazySavingHelper OBJ = new LazySavingHelper();

	/** 变化的数据列表 */
	private final Map<Serializable, UpdateEntry> _changeObjMap = new ConcurrentHashMap<>();
	/** 当数据对象空闲超过指定时间后才真正执行更新操作 */
	public long _idelToUpdate = 2L * 60L * 1000L;
	/** IO 服务 */
	public IoOperServ<?> _ioServ = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private LazySavingHelper() {
	}

	/**
	 * 增加要被更新的对象
	 * 
	 * @param lso
	 * @return 
	 * 
	 */
	public boolean addUpdate(ILazySavingObj<?, ?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		} else {
			return this.addUpdate(lso.getLifeCycle());
		}
	}

	/**
	 * 增加要被更新的对象, 注意 : 必须是同一个实例
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	public boolean addUpdate(LifeCycle lc) {
		if (lc == null || 
			lc._currState != LifeCycleStateEnum.active) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 获取业务对象 UId
		final String lsoUId = lc.getUId();
		// 获取旧的进入点
		UpdateEntry oldEntry = this._changeObjMap.get(lsoUId);
		// 获取当前时间
		long nowTime = this.nowTime();

		do {
			if (oldEntry != null) {
				if (oldEntry._operTypeInt == UpdateEntry.OPT_del) {
					// 如果已有的入口是删除操作,
					// 我擦, 那到底是删除还是更新啊...
					// 放弃已有的删除操作, 
					// 改为更新操作...
					LazySavingLog.LOG.error(MessageFormat.format(
						"准备将对象标记为更新操作, 但是已存在一个 key ( = {0} ) 相同的删除操作, 所以放弃删除操作改为更新操作", 
						lsoUId
					));
					break;
				}
	
				if (oldEntry._lifeCycle != lc) {
					// 如果 LifeCycle 不是同一个对象, 
					// 则直接退出!
					LazySavingLog.LOG.error("更新对象 ( 内存地址 ) 不相同, 这是不允许的"); 
					return false;
				} else {
					// 如果是同一对象, 
					// 则直接退出!
					// 但是在更新前修改一下时间
					oldEntry._lastModifiedTime = nowTime;
					return true;
				}
			}
		} while (false);

		// 创建新的进入点
		UpdateEntry newEntry = new UpdateEntry(lc, UpdateEntry.OPT_saveOrUpdate);
		// 设置最后修改时间
		newEntry._lastModifiedTime = nowTime;
		// 添加到字典中
		this._changeObjMap.put(lsoUId, newEntry);

		return true;
	}

	/**
	 * 增加要被删除的对象
	 * 
	 * @param po
	 * @return 
	 * 
	 */
	public boolean addDel(ILazySavingObj<?, ?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		} else {
			return this.addDel(lso.getLifeCycle());
		}
	}

	/**
	 * 增加要被删除的对象
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	public boolean addDel(LifeCycle lc) {
		if (lc == null || 
			lc._currState != LifeCycleStateEnum.active) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 获取业务对象 UId
		final String lsoUId = lc.getUId();
		// 获取旧的进入点
		UpdateEntry oldEntry = this._changeObjMap.get(lsoUId);

		if (oldEntry != null) {
			if (oldEntry._operTypeInt == UpdateEntry.OPT_del) {
				// 如果已有的入口是更新操作,
				// 我擦, 那到底是删除还是更新啊...
				LazySavingLog.LOG.error(MessageFormat.format(
					"准备将对象标记为删除操作, 但是已存在一个 key ( = {0} ) 相同的更新操作, 所以本次删除操作被忽略", 
					lsoUId
				));
				return false;
			}

			if (oldEntry._lifeCycle != lc) {
				// 如果 LifeCycle 不是同一个对象, 
				// 则直接退出!
				LazySavingLog.LOG.error("更新对象 ( 内存地址 ) 不相同, 这是不允许的");
				return false;
			} else {
				// 如果是同一对象, 
				// 则直接退出!
				return true;
			}
		}

		// 
		// 创建新的进入点
		// 注意 : 删除对象时会立即执行! 所以, 
		// 在这里的时间戳会设置为 0
		// 
		// 创建新的进入点
		UpdateEntry newEntry = new UpdateEntry(lc, UpdateEntry.OPT_saveOrUpdate);
		// 设置最后修改时间
		newEntry._lastModifiedTime = 0L;
		// 添加到字典
		this._changeObjMap.put(lsoUId, newEntry);

		return true;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 
	 * 
	 */
	private long nowTime() {
		// 获取系统时间
		return System.currentTimeMillis();
	}

	/**
	 * 执行更新操作
	 * 
	 * @see execUpdate(IPOPredication)
	 * 
	 */
	public final void execUpdate() {
		this.execUpdate(null);
	}

	/**
	 * 执行更新操作, 如果指定了断言参数, 则判断 PO 对象是否符合条件并保存数据库.
	 * 否则, 按照空闲时间来更新.
	 * 空闲时间参数由 {@link #_idelToUpdate} 指定
	 * 
	 * @param pred
	 * 
	 */
	public final void execUpdate(ILazySavingPredication pred) {
		// 获取当前时间
		long nowTime = this.nowTime();
		// 开始时间
		long startTime = nowTime;

		// 获取迭代器
		Iterator<UpdateEntry> it = this._changeObjMap.values().iterator();

		while (it.hasNext()) {
			// 获取入口
			UpdateEntry entry = it.next();

			if (entry == null || 
				entry._lifeCycle == null) {
				// 如果入口对象为空, 
				// 则直接跳过!
				it.remove();
				continue;
			}

			// 获取 LifeCycle
			LifeCycle lc = entry._lifeCycle;

			if (lc._currState != LifeCycleStateEnum.active) {
				// 如果 LifeCycle 尚未激活, 
				// 则直接跳过!
				LazySavingLog.LOG.error(MessageFormat.format(
					"尚未激活 LifeCycle, key = {0}", 
					lc.getUId()
				));
				it.remove();
				continue;
			}

			if (pred != null) {
				if (pred.predicate(lc._lazySavingObj) == false) {
					// 如果有断言对象, 
					// 并且当前 PO 不满足条件, 
					// 则直接退出!
					continue;
				}
			} else {
				if ((nowTime - entry._lastModifiedTime) < this._idelToUpdate) {
					// 如果还没有到时间, 
					// 则直接跳过!
					continue;
				}
			}

			try {
				if (entry._operTypeInt == UpdateEntry.OPT_saveOrUpdate) {
					// 执行保存或更新操作
					CommUpdater.OBJ.saveOrUpdate(lc._lazySavingObj);
				} else {
					// 执行删除操作
					CommUpdater.OBJ.del(lc._lazySavingObj);
					// 在这里销毁业务对象!
					lc.destroy();
				}

				// 从字典中移除对象
				it.remove();
			} catch (Exception ex) {
				// 记录异常日志
				LazySavingLog.LOG.error(ex.getMessage(), ex);
			}
		}

		// 获取结束时间
		long endTime = this.nowTime();
		// 获取花费时间
		long costTime = endTime - startTime;

		// 记录调试信息
		LazySavingLog.LOG.debug(MessageFormat.format(
			"更新消耗时间 = {0}(ms)", 
			String.valueOf(costTime)
		));
	}
}
