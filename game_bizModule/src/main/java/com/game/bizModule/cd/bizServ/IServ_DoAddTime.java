package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.time.TimeServ;
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
	 * @param humanUId
	 * @param cdType
	 * @param ms
	 * @return 
	 * 
	 */
	default Result_DoAddTime doAddTime(
		long humanUId,
		CdTypeEnum cdType, 
		long ms) {
		// 断言参数对象不为空
		Assert.notNull(cdType, "cdType");
		// 借出结果对象
		Result_DoAddTime result = BizResultPool.borrow(Result_DoAddTime.class);

		if (ms <= 0) {
			return result;
		}

		// 是否可以增加 Cd 时间 ?
		Result_CanAddTime result_2 = CdServ.OBJ.canAddTime(
			humanUId, cdType
		);

		if (result_2._ok == false) {
			// 如果不能增加 Cd 时间, 
			// 则直接退出!
			result._errorCode = result_2._errorCode;
			return result;
		}

		// 获取管理器
		CdManager mngrObj = CdServ.OBJ._mngrMap.get(humanUId);
		// 获取计时器
		CdTimer t = mngrObj._cdTimerMap.get(cdType);
		// 获取当前时间
		long now = TimeServ.OBJ.now();
		// 获取当前时间与结束时间的时间差
		long diffTime_0 = t.getDiffTime(now);
		// 更新开始时间和结束时间
		t._startTime = now;
		t._endTime = now + diffTime_0 + ms;

		return result;
	}
}
