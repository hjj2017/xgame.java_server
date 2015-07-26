package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.part.util.BizResultObj;

/**
 * 查找并增加 Cd 时间
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
public class Result_FindAndDoAddTime extends BizResultObj {
	/** 已使用的 Cd 类型 */
	public CdTypeEnum _usedCdType = null;

	@Override
	protected void clearContent() {
		this._usedCdType = null;
	}
}
