package com.game.bizModules.cd.serv;

import com.game.bizModules.cd.CdLangDef;
import com.game.bizModules.cd.model.CdTimer;
import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.bizModules.cd.tmpl.CdTimerTmpl;
import com.game.core.utils.BizResultPool;

/**
 * 是否可以增加 Cd 时间
 * 
 * @author hjj2017
 *
 */
interface IServ_CanAddTime {
	/**
	 * 是否可以增加 Cd 时间 ?
	 * 注意 : 在该函数中会检查计时器是否已过期并重置!
	 * 
	 * @param humanUUID
	 * @param cdType
	 * @return 
	 * @see CdServ#checkExpiredAndReset(CdTimer)
	 * 
	 */
	default Result_CanAddTime canAddTime(
		long humanUUID, 
		CdTypeEnum cdType) {
		// 借出结果对象
		Result_CanAddTime result = BizResultPool.borrow(Result_CanAddTime.class);
		// 获取 Cd 管理器
		CdManager mngr = CdServ.OBJ._mngrMap.get(humanUUID);

		if (mngr == null) {
			// 如果管理器对象为空, 
			// 则直接退出!
			result._can = false;
			result._errorCode = CdLangDef.ERROR_NULL_MNGR;
			return result;
		}

		// 获取计时器
		CdTimer t = mngr._cdMap.get(cdType);

		if (t == null || 
			t._opened == false) {
			// 如果定时器不存在或者还没有开启, 
			// 则直接跳过!
			result._can = false;
			result._errorCode = CdLangDef.ERROR_CD_NOT_OPEN;
			return result;
		}

		// 检查计时器是否已过期 ?
		CdServ.OBJ.checkExpiredAndReset(t);
		// 获取开始时间和结束时间的时间差
		long diffTime_0 = t.getDiffTime();

		if (diffTime_0 >= getThreshold(cdType)) {
			// 如果时间间隔大于阈值, 
			// 则直接跳过!
			result._can = false;
			result._errorCode = CdLangDef.ERROR_CD_TOO_HOT;
			return result;
		}

		return result;
	}

	/**
	 * 获取 Cd 阈值
	 * 
	 * @param cdType
	 * @return 
	 * 
	 */
	static long getThreshold(CdTypeEnum cdType) {
		// 获取计时器模板对象
		CdTimerTmpl cdTimer = CdTimerTmpl.get(cdType);

		if (cdTimer == null || 
			cdTimer._threshold == null) {
			return 0L;
		} else {
			return cdTimer._threshold;
		}
	}
}
