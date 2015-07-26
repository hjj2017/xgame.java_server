package com.game.bizModule.cd.bizServ;

import com.game.part.util.BizResultObj;

/**
 * 是否可以增加时间?
 * 
 * @author hjj2017
 * @since 2015/7/21
 *
 */
public class Result_CanAddTime extends BizResultObj {
	/** 是否可以累计 Cd 时间? */
	public boolean _canAddTime = false;

	@Override
	protected void clearContent() {
		this._canAddTime = false;
	}
}
