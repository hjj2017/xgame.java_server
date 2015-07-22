package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 查找并增加 Cd 时间
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
interface IServ_FindAndDoAddTime {
	/**
	 * 从 cdTypeArr 中查找第一个可以累计时间的 Cd 类型, 并增加 Cd 时间!
	 * <font color="#990000">
	 * 注意: 该函数适用于建筑类型 Cd, 因为建筑有多个 Cd</font>
	 * 
	 * @param humanUId
	 * @param fromCdTypeArr
	 * @param ms
	 * @return 
	 * 
	 */
	default Result_FindAndDoAddTime findAndDoAddTime(long humanUId, CdTypeEnum[] fromCdTypeArr, long ms) {
		// 断言参数对象不为空
		Assert.notNullOrEmpty(fromCdTypeArr, "fromCdTypeArr");
		// 借出结果对象
		Result_FindAndDoAddTime result = BizResultPool.borrow(Result_FindAndDoAddTime.class);
		// 首先查找 Cd 类型
		CdTypeEnum targetCdType = firstCanAdd(humanUId, fromCdTypeArr);

		if (targetCdType == null) {
			return result;
		}

		// 增加 Cd 时间
		CdServ.OBJ.doAddTime(humanUId, targetCdType, ms);
		return result;
	}

	/**
	 * 获取第一个可以增加时间的 Cd 类型
	 * 
	 * @param humanUId
	 * @param fromCdTypeArr
	 * @return 
	 * 
	 */
	static CdTypeEnum firstCanAdd(long humanUId, CdTypeEnum[] fromCdTypeArr) {
		// 断言参数对象不为空
		Assert.notNullOrEmpty(fromCdTypeArr, "fromCdTypeArr");

		for (CdTypeEnum cdType : fromCdTypeArr) {
			if (CdServ.OBJ.canAddTime(humanUId, cdType)._ok) {
				// 如果可以增加 Cd 时间, 
				// 则直接返回!
				return cdType;
			}
		}

		return null;
	}
}
