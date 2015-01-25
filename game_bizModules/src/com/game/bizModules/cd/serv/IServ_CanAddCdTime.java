package com.game.bizModules.cd.serv;

import com.game.bizModules.cd.model.CdTimer;
import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.bizModules.character.Human;
import com.game.part.utils.Assert;

/**
 * 是否可以增加 Cd 时间 ?
 * 
 * @author hjj2019
 *
 */
interface IServ_CanAddCdTime {
	/**
	 * 判断是否可以增加 Cd 时间 ?
	 * 
	 * @param h 
	 * @param cdType
	 * @return
	 * 
	 */
	public default boolean canAddTime(Human h, CdTypeEnum cdType) {
		// 断言参数不为空
		Assert.notNull(h, "h");
		Assert.notNull(cdType, "typeEnum");

//		// 获取管理器对象
//		CdManager mngrObj = CdServ.OBJ.getCdManager(h);
//		// 断言管理器不为空
//		Assert.notNull(mngrObj, "mngrObj");
//
//		// 获取计时器
//		CdTimer timer = mngrObj.getCdTimer(cdType);
//		// 断言计时器不为空
//		Assert.notNull(timer, "timer");
//
//		// 获取当前时间
//		long now = CdServ.OBJ.getNowTime();
//		// 获取阈值时间
//		long thresholdTime = getThresholdTime(cdType);
//
//		if (timer.getTimeDiff(now) <= 0 || 
//			timer.getTimeDiff() < thresholdTime) {
//			// 如果计时器已经过期, 
//			// 或者还没有达到阈值时间, 
//			// 则可以增加 Cd 时间
//			return true;
//		} else {
//			// 否则不能增加时间
//			return false;
//		}
		return false;
	}

	/**
	 * 是否可以增加 Cd 时间
	 * 
	 * @param h
	 * @param cdTypeArr
	 * @return
	 * 
	 */
	public default CdTypeEnum getFirstCdTypeCanAddTime(Human h, CdTypeEnum[] cdTypeArr) {
		// 断言参数不为空
		Assert.notNull(h, "h");
		Assert.notNull(cdTypeArr, "typeEnumArr");

		for (CdTypeEnum cdType : cdTypeArr) {
			if (this.canAddTime(h, cdType)) {
				return cdType;
			}
		}

		return null;
	}

	/**
	 * 
	 * @param cdType
	 * @return
	 */
	static long getThresholdTime(CdTypeEnum cdType) {
		return 0L;
	}
}
