package com.game.gameServer.common.db;

import java.io.Serializable;

import com.game.core.orm.IBaseEntity;
import com.game.core.persistance.IPOUpdater;
import com.game.core.persistance.IPersistanceObject;
import com.game.db.dao.CommDao;

/**
 * 通用的 PO 更新器
 * 
 * @author hjj2017
 * @since 2015/3/31
 * 
 */
public class CommPOUpdater implements IPOUpdater {
	/** 单例对象 */
	public static final CommPOUpdater OBJ = new CommPOUpdater();
	/** 通用的 DAO */
	public CommDao _commDao = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private CommPOUpdater() {
	}

	@Override
	public void save(IPersistanceObject<?, ?> po) {
		if (po == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		@SuppressWarnings("unchecked")
		IBaseEntity<Serializable> entity = (IBaseEntity<Serializable>)po.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			return;
		}

		// 保存到数据库
		this._commDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(IPersistanceObject<?, ?> po) {
		if (po == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		@SuppressWarnings("unchecked")
		IBaseEntity<Serializable> entity = (IBaseEntity<Serializable>)po.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			return;
		}

		// 保存到数据库
		this._commDao.delete(entity);
	}
}
