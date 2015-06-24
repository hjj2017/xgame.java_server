package com.game.bizModule.cd.dao;

import java.util.List;

import com.game.bizModule.cd.entity.CdTimerEntity;

/**
 * Cd 计时器 Dao
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CdTimerDao {
	/** 单例对象 */
	public static final CdTimerDao OBJ = new CdTimerDao();

	/**
	 * 获取 Cd 计时器实体
	 * 
	 * @param humanUUID
	 * @return
	 * 
	 */
	public List<CdTimerEntity> listByHumanUUID(long humanUUID) {
		return null;
	}
}
