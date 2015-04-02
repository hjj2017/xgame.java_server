package com.game.core.persistance;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.game.core.time.ITimeService;

/**
 * 数据更新器
 * 
 * @author haijiang.jin
 * @since 2015/3/30
 * 
 */
public final class PersistanceHelper {
	/** 单例对象 */
	public static final PersistanceHelper OBJ = new PersistanceHelper();

	/** 变化的数据列表 */
	private final Map<Serializable, UpdateEntry> _changeObjMap = new ConcurrentHashMap<>();
	/** 是否正在执行Update操作 */
	private final AtomicBoolean _isUpdating = new AtomicBoolean(false);
	/** 时间服务 */
	public ITimeService _timeServ;
	/** 当数据对象空闲超过指定时间后才真正执行更新操作 */
	public long _idelToUpdate = 5000L;
	/** 更新操作字典, 主要是 PO 类与 IPOUpdater 之间的对应关系 */
	public final Map<Class<? extends IPersistanceObject<?, ?>>, IPOUpdater> _updaterMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private PersistanceHelper() {
	}

	/**
	 * 增加要被更新的对象
	 * 
	 * @param po
	 * @return 
	 * 
	 */
	public boolean addUpdate(IPersistanceObject<?, ?> po) {
		if (po == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		} else {
			return this.addUpdate(po.getLifeCycle());
		}
	}

	/**
	 * 增加要被更新的对象, 注意 : 必须是同一个实例
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	public boolean addUpdate(ILifeCycle lc) {
		if (lc == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		if (this._isUpdating.get()) {
			// 如果正在更新中, 
			// 则直接退出!
			PersistanceLog.LOG.error("正在执行更新操作, 无法添加新对象");
			return false;
		}

		// 获取关键字
		final Serializable key = lc.getKey();
		// 获取旧的进入点
		UpdateEntry oldEntry = this._changeObjMap.get(key);
		// 获取当前时间
		long nowTime = this.nowTime();

		do {
			if (oldEntry != null) {
				if (UpdateEntry.isInsertOrUpdateOper(oldEntry) == false) {
					// 如果已有的入口是删除操作,
					// 我擦, 那到底是删除还是更新啊...
					// 放弃已有的删除操作, 
					// 改为更新操作...
					PersistanceLog.LOG.error(MessageFormat.format(
						"准备将对象标记为更新操作, 但是已存在一个 key ( = {0} ) 相同的删除操作, 所以放弃删除操作改为更新操作", 
						key
					));
					break;
				}
	
				if (oldEntry._lifeCycle != lc) {
					// 如果 LifeCycle 不是同一个对象, 
					// 则直接退出!
					PersistanceLog.LOG.error("更新对象 ( 内存地址 ) 不相同, 这是不允许的"); 
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
		UpdateEntry newEntry = UpdateEntry.createInsertOrUpdateEntry(lc, nowTime);
		// 添加到字典中
		this._changeObjMap.put(key, newEntry);

		return true;
	}

	/**
	 * 增加要被更新的对象
	 * 
	 * @param po
	 * @return 
	 * 
	 */
	public boolean addDelete(IPersistanceObject<?, ?> po) {
		if (po == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		} else {
			return this.addDelete(po.getLifeCycle());
		}
	}

	/**
	 * 增加要被删除的对象
	 * 
	 * @param lc
	 * @return
	 * 
	 */
	public boolean addDelete(ILifeCycle lc) {
		if (lc == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 获取关键字
		final Serializable key = lc.getKey();
		// 获取旧的进入点
		UpdateEntry oldEntry = this._changeObjMap.get(key);

		if (oldEntry != null) {
			if (UpdateEntry.isInsertOrUpdateOper(oldEntry)) {
				// 如果已有的入口是更新操作,
				// 我擦, 那到底是删除还是更新啊...
				PersistanceLog.LOG.error(MessageFormat.format(
					"准备将对象标记为删除操作, 但是已存在一个 key ( = {0} ) 相同的更新操作, 所以本次删除操作被忽略", 
					key
				));
				return false;
			}

			if (oldEntry._lifeCycle != lc) {
				// 如果 LifeCycle 不是同一个对象, 
				// 则直接退出!
				PersistanceLog.LOG.error("更新对象 ( 内存地址 ) 不相同, 这是不允许的");
				return false;
			} else {
				// 如果是同一对象, 
				// 则直接退出!
				return true;
			}
		}

		// 创建新的进入点
		// 注意 : 删除对象时会立即执行! 所以, 
		// 在这里的时间戳会设置为 0
		UpdateEntry newEntry = UpdateEntry.createDeleteEntry(lc, 0L);
		// 添加到字典
		this._changeObjMap.put(key, newEntry);

		return true;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 
	 * 
	 */
	private long nowTime() {
		if (this._timeServ != null) {
			// 获取当前时间戳
			return this._timeServ.now();
		} else {
			// 获取系统时间
			return System.currentTimeMillis();
		}
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
	 * @see _idelToUpdate
	 * 
	 */
	public final void execUpdate(IPOPredication pred) {
		if (this._isUpdating.get()) {
			// 如果正在更新中, 
			// 则直接退出!
			PersistanceLog.LOG.error("正在更新中, 所以忽略此次操作");
			return;
		}

		// 将更新标识设置为 true
		this._isUpdating.set(true);
		// 开始时间
		long startTime = System.currentTimeMillis();
		// 获取当前时间
		long nowTime = this.nowTime();

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
			ILifeCycle lc = entry._lifeCycle;

			if (lc.isActive() == false) {
				// 如果 LifeCycle 尚未激活, 
				// 则直接跳过!
				PersistanceLog.LOG.error(MessageFormat.format(
					"尚未激活 LifeCycle, key = {0}", 
					lc.getKey()
				));
				it.remove();
				continue;
			}

			if (pred != null) {
				if (pred.predicate(lc.getPO()) == false) {
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
				if (UpdateEntry.isInsertOrUpdateOper(entry)) {
					// 执行更新操作
					this.doInsertOrUpdate(lc);
				} else {
					// 执行删除操作
					this.doDelete(lc);
				}
				
				// 从字典中移除对象
				it.remove();
			} catch (Exception ex) {
				// 记录异常日志
				PersistanceLog.LOG.error(ex.getMessage(), ex);
			}
		}

		// 获取结束时间
		long endTime = System.currentTimeMillis();
		// 获取花费时间
		long costTime = endTime - startTime;

		// 记录调试信息
		PersistanceLog.LOG.debug(MessageFormat.format(
			"更新消耗时间 = {0}(ms)", 
			String.valueOf(costTime)
		));

		// 更新已完成
		this._isUpdating.set(false);
	}

	/**
	 * 执行插入或更新操作
	 * 
	 * @param lc 
	 * 
	 */
	private void doInsertOrUpdate(ILifeCycle lc) {
		if (lc == null || 
			lc.getPO() == null) {
			// 如果对象参数为空,
			// 则直接退出!
			return;
		}

		// 获取要被保存的对象
		IPersistanceObject<?, ?> po = lc.getPO();
		// 获取更新器
		IPOUpdater updater = this._updaterMap.get(po.getClass());

		if (updater == null) {
			// 如果更新器为空, 
			// 则直接抛出运行时异常!
			throw new RuntimeException(MessageFormat.format(
				"未找到与类 {0} 相对应的更新器", 
				po.getClass().getName()
			));
		}

		// 记录调试日志
		PersistanceLog.LOG.debug(MessageFormat.format(
			"准备保存数据 poClazz = {0}, key = {1}", 
			po.getClass().getName(), 
			lc.getKey()
		));

		// 执行插入或更新操作
		updater.save(po);
	}

	/**
	 * 执行删除操作
	 * 
	 * @param lc 
	 * 
	 */
	private void doDelete(ILifeCycle lc) {
		if (lc == null) {
			// 如果对象参数为空,
			// 则直接退出!
			return;
		}

		// 获取要被保存的对象
		IPersistanceObject<?, ?> po = lc.getPO();
		// 获取更新器
		IPOUpdater updater = this._updaterMap.get(po.getClass());

		if (updater == null) {
			// 如果更新器为空, 
			// 则直接抛出运行时异常!
			throw new RuntimeException(MessageFormat.format(
				"未找到与类 {0} 相对应的更新器", 
				po.getClass().getName()
			));
		}

		// 记录调试日志
		PersistanceLog.LOG.debug(MessageFormat.format(
			"准备删除数据 poClazz = {0}, key = {1}", 
			po.getClass().getName(), 
			lc.getKey()
		));

		// 执行删除操作
		updater.delete(po);
	}

	/**
	 * 返回改变的对象
	 * 
	 * @return
	 * 
	 */
	public Map<Serializable, UpdateEntry> getChangedObjMap() {
		// 获取对象字典
		return Collections.unmodifiableMap(
			this._changeObjMap
		);
	}
}
