package com.game.part.lazySaving;

import com.game.part.dao.CommDao;

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
		Object entity = lso.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			return;
		}

		// 保存到数据库
		CommDao.OBJ.save(entity);
	}

	/**
	 * 删除业务对象
	 * 
	 * @param po 
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
			return;
		}

		// 从数据库中删除
		CommDao.OBJ.del(entity.getClass(), "");
	}
}
