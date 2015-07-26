package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.CdLog;
import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.cd.tmpl.CdTimerTmpl;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.part.util.BizResultPool;

import java.text.MessageFormat;

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
	 * @param humanUId
	 * @param cdType
	 * @return 
	 * @see CdServ#checkExpiredAndReset(CdTimer)
	 * 
	 */
	default Result_CanAddTime canAddTime(
		long humanUId,
		CdTypeEnum cdType) {
		// 借出结果对象
		Result_CanAddTime result = BizResultPool.borrow(Result_CanAddTime.class);
		// 获取 Cd 管理器
		CdManager mngrObj = CdServ.OBJ._mngrMap.get(humanUId);

		if (mngrObj == null) {
			// 如果管理器对象为空, 
			// 则直接退出!
			CdLog.LOG.error(MessageFormat.format(
				"管理器为空, 角色 = {0}",
				String.valueOf(humanUId)
			));
			result._errorCode = MultiLangDef.LANG_COMM_nullMngrObj;
			result._canAddTime = false;
			return result;
		}

		// 获取计时器
		CdTimer t = mngrObj._cdTimerMap.get(cdType);

		if (t == null || 
			t._opened == false) {
			// 如果定时器不存在或者还没有开启, 
			// 则直接跳过!
			CdLog.LOG.error(MessageFormat.format(
				"计时器尚未开启, 角色 = {0}, Cd 类型 = {1}",
				String.valueOf(humanUId),
				cdType.getStrVal()
			));
			result._errorCode = MultiLangDef.LANG_CD_cdTypeNotOpened;
			result._canAddTime = false;
			return result;
		}

		// 检查计时器是否已过期 ?
		CdServ.OBJ.checkExpiredAndReset(t);
		// 获取开始时间和结束时间的时间差
		long diffTime = t.getDiffTime();

		if (diffTime >= getThreshold(cdType)) {
			// 如果时间间隔大于阈值, 
			// 则直接跳过!
			result._errorCode = MultiLangDef.LANG_CD_diffTimeGeqThreshold;
			result._canAddTime = false;
			return result;
		}

		result._canAddTime = true;
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
		CdTimerTmpl tmplObj = CdTimerTmpl.getByCdType(cdType);

		if (tmplObj == null ||
			tmplObj._threshold == null) {
			return 0L;
		} else {
			return tmplObj._threshold.getLongVal();
		}
	}
}
