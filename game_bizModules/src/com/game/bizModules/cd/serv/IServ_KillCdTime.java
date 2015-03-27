package com.game.bizModules.cd.serv;

import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 清除 Cd
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public interface IServ_KillCdTime {
	/**
	 * 清除 Cd 时间
	 * 
	 * @param humanUUID 
	 * @param cdType 
	 * @return 
	 * 
	 */
	default Result_KillCdTime killCdTime(long humanUUID, CdTypeEnum cdType) {
		// 断言参数不为空
		Assert.notNull(cdType, "cdType");
		// 借出结果对象
		Result_KillCdTime result = BizResultPool.borrow(Result_KillCdTime.class);
		return result;
	}
}
