package com.game.bizModule.cd.serv;

import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.character.Human;
import com.game.part.util.Assert;

/**
 * 增加 Cd 时间
 * 
 * @author hjj2019
 * @since 2014/5/18
 * 
 */
interface IServ_AddCdTime {
	/**
	 * 增加 CD 时间
	 * 
	 * @param h
	 * @param cdType
	 * @param ms
	 * 
	 */
	public default Result_AddCdTime addCdTime(Human h, CdTypeEnum cdType, long ms) {
		// 断言参数不为空
		Assert.notNull(h, "h");
		Assert.notNull(cdType, "cdType");
		Assert.isTrue(ms > 0, "ms <= 0");

		// 查找并增加 Cd 时间
		Result_AddCdTime result = this.findAndAddCdTime(
			h, new CdTypeEnum[] { cdType }, ms
		);
		return result;
	}

	/**
	 * 增加 CD 时间
	 * 
	 * @param h
	 * @param cdType
	 * @param ms
	 * 
	 */
	public default Result_AddCdTime findAndAddCdTime(Human h, CdTypeEnum[] cdTypeArr, long ms) {
		// 断言参数不为空
		Assert.notNull(h, "h");
		Assert.notNull(cdTypeArr, "cdTypeArr");
		Assert.isTrue(ms > 0, "ms <= 0");

//		// 获取第一个可以增加时间的 Cd 类型
//		CdTypeEnum cdType = CdServ.OBJ.getFirstCdTypeCanAddTime(h, cdTypeArr);
//		Assert.notNull(cdType);
//
//		// 获取 Cd 管理器
//		CdManager mngrObj = CdServ.OBJ.getCdManager(h);
//		Assert.notNull(mngrObj);
//
//		// 获取冷却队列列表
//		CdTimer cdTimer = mngrObj.getCdTimer(cdType);
//		Assert.notNull(cdTimer);
//
//		// 获取当前时间
//		long now = CdServ.OBJ.getNowTime();
//		// 原有的
//		long oldTimeDiff = cdTimer.getTimeDiff(now);
//
//		// 设置计时器的开始时间和结束时间
//		cdTimer._startTime = now;
//		cdTimer._endTime = now + oldTimeDiff + ms;

		return null;
	}
}
