package com.game.bizModules.cd.serv;

import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.core.utils.Assert;
import com.game.core.utils.BizResultPool;

/**
 * 查找并增加 Cd 时间
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
interface IServ_FindAndDoAddTime {
	/**
	 * 查找并增加 Cd 时间
	 * 
	 * @param humanUUID
	 * @param cdTypeArr
	 * @param ms
	 * @return 
	 * 
	 */
	default Result_FindAndDoAddTime findAndDoAddTime(long humanUUID, CdTypeEnum[] cdTypeArr, long ms) {
		// 断言参数对象不为空
		Assert.notNullOrEmpty(cdTypeArr, "cdTypeArr");
		// 借出结果对象
		Result_FindAndDoAddTime result = BizResultPool.borrow(Result_FindAndDoAddTime.class);
		// 首先查找 Cd 类型
		CdTypeEnum cdType = find(humanUUID, cdTypeArr);

		if (cdType == null) {
			return result;
		}

		// 增加 Cd 时间
		CdServ.OBJ.doAddTime(humanUUID, cdType, ms);
		return result;
	}

	/**
	 * 查找可以增加时间的 Cd 类型
	 * 
	 * @param humanUUID
	 * @param cdTypeArr
	 * @return 
	 * 
	 */
	static CdTypeEnum find(long humanUUID, CdTypeEnum[] cdTypeArr) {
		// 断言参数对象不为空
		Assert.notNullOrEmpty(cdTypeArr, "cdTypeArr");

		for (CdTypeEnum cdType : cdTypeArr) {
			if (CdServ.OBJ.canAddTime(humanUUID, cdType)._can) {
				// 如果可以增加 Cd 时间, 
				// 则直接返回!
				return cdType;
			}
		}

		return null;
	}
}
