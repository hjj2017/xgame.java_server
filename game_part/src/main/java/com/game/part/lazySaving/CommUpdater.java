package com.game.part.lazySaving;

import java.text.MessageFormat;

import com.game.part.io.IoOperServ;

/**
 * 通用的 LSO 更新器
 * 
 * @author hjj2017
 * @since 2015/3/31
 * 
 */
class CommUpdater {
	/** 通用的更新器 */
	static final CommUpdater OBJ = new CommUpdater();

	/**
	 * 类默认构造器
	 * 
	 */
	private CommUpdater() {
	}

	/**
	 * 保存或者更新业务对象
	 * 
	 * @param lso 
	 * 
	 */
	void saveOrUpdate(ILazySavingObj<?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取数据库实体
		final Object entity = lso.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			LazySavingLog.LOG.error(MessageFormat.format(
				"无法保存实体数据, 实体对象为空! storeKey = {0}",
				lso.getStoreKey()
			));
			return;
		}

		// 通过 IO 服务执行保存操作
		IoOperServ.OBJ.execute(new IoOper_SaveOrUpdate(
			lso.getThreadKey(),
			entity
		));
	}

	/**
	 * 删除业务对象
	 * 
	 * @param lso 
	 * 
	 */
	void del(ILazySavingObj<?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取数据库实体
		Object entity = lso.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			LazySavingLog.LOG.error(MessageFormat.format(
				"无法删除实体数据, 实体对象为空! storeKey = {0}",
				lso.getStoreKey()
			));
			return;
		}


		// 通过 IO 服务执行保存操作...
		IoOperServ.OBJ.execute(new IoOper_Del(
			lso.getThreadKey(),
			entity
		));
	}
}
