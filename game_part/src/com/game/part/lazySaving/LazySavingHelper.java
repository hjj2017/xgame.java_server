package com.game.part.lazySaving;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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

	/** 
	 * 变化的数据字典 0, 主字典!
	 * 当处于更新过程中的时候 ( 执行 {@link #execUpdate()} 时 ), 
	 * 主字典会与外界隔绝...
	 * 
	 */
	private final Map<String, UpdateEntry> _changeObjMap0 = new ConcurrentHashMap<>();

	/** 
	 * 变化的数据字典 1, 辅助字典!
	 * 当处于更新过程中的时候 ( 执行 {@link #execUpdate()} 时 ), 
	 * 新数据会塞到辅助字典里...
	 * 
	 */
	private final Map<String, UpdateEntry> _changeObjMap1 = new ConcurrentHashMap<>();

	/** 更新中...? */
	private AtomicBoolean _updatingFlag = new AtomicBoolean(false);

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
	public void addUpdate(ILazySavingObj<?, ?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		} else {
			this.addUpdate(lso.getLifeCycle());
		}
	}

	/**
	 * 增加要被更新的对象, 注意 : 必须是同一个实例
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	public void addUpdate(LifeCycle lc) {
		// 字典变量
		Map<String, UpdateEntry> mapX = this._updatingFlag.get() ? this._changeObjMap1 : this._changeObjMap0;
		// 添加更新操作
		addUpdate(lc, this.nowTime(), mapX);
	}
	
	/**
	 * 增加要被更新的对象, 注意 : 必须是同一个实例
	 * 
	 * @param lc
	 * @param nowTime 
	 * @param mapX 
	 * @return
	 * 
	 */
	private static void addUpdate(LifeCycle lc, long nowTime, Map<String, UpdateEntry> mapX) {
		if (lc == null || 
			lc._currState != LifeCycleStateEnum.active || 
			mapX == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取业务对象 UId
		final String lsoUId = lc.getUId();
		// 获取旧的进入点
		UpdateEntry oldEntry = mapX.get(lsoUId);

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
					return;
				} else {
					// 如果是同一对象, 
					// 则直接退出!
					// 但是在更新前修改一下时间
					oldEntry._lastModifiedTime = nowTime;
					return;
				}
			}
		} while (false);

		// 创建新的进入点
		UpdateEntry newEntry = new UpdateEntry(lc, UpdateEntry.OPT_saveOrUpdate);
		// 设置最后修改时间
		newEntry._lastModifiedTime = nowTime;
		// 添加到字典中
		mapX.put(lsoUId, newEntry);

		return;
	}

	/**
	 * 增加要被删除的对象
	 * 
	 * @param lso
	 * @return 
	 * 
	 */
	public void addDel(ILazySavingObj<?, ?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		} else {
			this.addDel(lso.getLifeCycle());
		}
	}

	/**
	 * 增加要被删除的对象
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	public void addDel(LifeCycle lc) {
		if (lc == null || 
			lc._currState != LifeCycleStateEnum.active) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 字典变量
		Map<String, UpdateEntry> mapX = this._updatingFlag.get() ? this._changeObjMap1 : this._changeObjMap0;
		// 添加删除操作
		addDel(lc, this.nowTime(), mapX);
	}

	/**
	 * 增加要被删除的对象
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	private static void addDel(LifeCycle lc, long nowTime, Map<String, UpdateEntry> mapX) {
		if (lc == null || 
			lc._currState != LifeCycleStateEnum.active || 
			mapX == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取业务对象 UId
		final String lsoUId = lc.getUId();
		// 获取旧的进入点
		UpdateEntry oldEntry = mapX.get(lsoUId);

		if (oldEntry != null) {
			if (oldEntry._operTypeInt == UpdateEntry.OPT_del) {
				// 如果已有的入口是更新操作,
				// 我擦, 那到底是删除还是更新啊...
				LazySavingLog.LOG.error(MessageFormat.format(
					"准备将对象标记为删除操作, 但是已存在一个 key ( = {0} ) 相同的更新操作, 所以本次删除操作被忽略", 
					lsoUId
				));
				return;
			}

			if (oldEntry._lifeCycle != lc) {
				// 如果 LifeCycle 不是同一个对象, 
				// 则直接退出!
				LazySavingLog.LOG.error("更新对象 ( 内存地址 ) 不相同, 这是不允许的");
				return;
			} else {
				// 如果是同一对象, 
				// 则直接退出!
				return;
			}
		}

		// 创建新的进入点
		UpdateEntry newEntry = new UpdateEntry(lc, UpdateEntry.OPT_del);
		// 设置最后修改时间
		newEntry._lastModifiedTime = nowTime;
		// 添加到字典
		mapX.put(lsoUId, newEntry);
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
	 * @see execUpdate(ILazySavingPredication)
	 * 
	 */
	public final void execUpdate() {
		this.execUpdate(null);
	}

	/**
	 * 执行更新操作, 如果指定了断言参数, 则判断 LSO 对象是否符合条件并保存数据库.
	 * 否则, 按照空闲时间来更新.
	 * 空闲时间参数由 {@link #_idelToUpdate} 指定
	 * 
	 * @param pred
	 * 
	 */
	public final void execUpdate(ILazySavingPredication pred) {
		if (this._updatingFlag.compareAndSet(
			false, true)) {
			// 事先检查是否未在更新过程中,
			// 如果没在更新, 则把标志位设置为 true...
			// 但如果正在更新中, 
			// 则直接退出!
			LazySavingLog.LOG.error("正在更新中...");
			return;
		}

		// 获取当前时间
		long nowTime = this.nowTime();
		// 开始时间
		long startTime = nowTime;

		// 将字典 1 中的数据移到字典 0
		mv(this._changeObjMap1, this._changeObjMap0);
		// 获取迭代器
		Iterator<UpdateEntry> it = this._changeObjMap0.values().iterator();

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
					// 并且当前 LSO 不满足条件, 
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
		
		// 再次将字典 1 中的数据移到字典 0, 
		// 因为在更新过程中, 
		// map1 中可能又有数据了...
		mv(this._changeObjMap1, this._changeObjMap0);

		// 获取结束时间
		long endTime = this.nowTime();
		// 获取花费时间
		long costTime = endTime - startTime;

		// 记录调试信息
		LazySavingLog.LOG.debug(MessageFormat.format(
			"更新消耗时间 = {0}(ms)", 
			String.valueOf(costTime)
		));

		// 修改更新标识
		this._updatingFlag.set(false);
	}

	/**
	 * 将 "来源字典" 中的键值移到 "目标字典"
	 * 
	 * @param fromMap
	 * @param toMap
	 * 
	 */
	private static void mv(Map<String, UpdateEntry> fromMap, Map<String, UpdateEntry> toMap) {
		if (fromMap == null || 
			fromMap.isEmpty() || 
			toMap == null || 
			toMap.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取键值迭代器
		Iterator<Entry<String, UpdateEntry>> it = fromMap.entrySet().iterator();

		while (it.hasNext()) {
			// 获取字典入口
			Entry<String, UpdateEntry> mapEntry = it.next();

			if (mapEntry == null || 
				mapEntry.getValue() == null) {
				// 如果字典入口为空, 
				// 则直接跳过!
				continue;
			}

			// 获取更新入口
			UpdateEntry upEntry = mapEntry.getValue();

			if (upEntry._operTypeInt == UpdateEntry.OPT_saveOrUpdate) {
				// 添加 LC 到目标字典 ( 保存或者更新 )
				addUpdate(
					upEntry._lifeCycle, 
					upEntry._lastModifiedTime, 
					toMap
				);
			} else {
				// 添加 LC 到目标字典 ( 删除 )
				addDel(
					upEntry._lifeCycle, 
					upEntry._lastModifiedTime, 
					toMap
				);
			}

			// 从来源字典中删除当前键值
			it.remove();
		}
	}
}
