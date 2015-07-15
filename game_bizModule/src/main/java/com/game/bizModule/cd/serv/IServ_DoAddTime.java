package com.game.bizModule.cd.serv;

import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 增加 Cd 时间
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
interface IServ_DoAddTime {
	/**
	 * 增加 Cd 时间
	 * 
	 * @param humanUUID
	 * @param cdType
	 * @param ms
	 * @return 
	 * 
	 */
	default Result_DoAddTime doAddTime(
		long humanUUID, 
		CdTypeEnum cdType, 
		long ms) {
		// 断言参数对象不为空
		Assert.notNull(cdType, "cdType");
		// 借出结果对象
		Result_DoAddTime result = BizResultPool.borrow(Result_DoAddTime.class);
		// 是否可以增加 Cd 时间 ?
		Result_CanAddTime result_1 = CdServ.OBJ.canAddTime(
			humanUUID, cdType
		);

		if (result_1._can == false) {
			// 如果不能增加 Cd 时间, 
			// 则直接退出!
			result._errorCode = result_1._errorCode;
			return result;
		}

		// 获取管理器
		CdManager mngr = CdServ.OBJ._mngrMap.get(humanUUID);
		// 获取计时器
		CdTimer t = mngr._cdMap.get(cdType);
		// 获取当前时间
		long now = CdServ.OBJ.getCurrTime();
		// 获取当前时间与结束时间的时间差
		long diffTime_0 = t.getDiffTime(now);
		// 更新开始时间和结束时间
		t._startTime = now;
		t._endTime = now + diffTime_0 + ms;

		return result;
	}
}
