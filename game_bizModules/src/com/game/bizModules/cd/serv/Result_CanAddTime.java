package com.game.bizModules.cd.serv;

import com.game.core.utils.BizResult;

/**
 * 是否可以增加时间 ?
 * 
 * @author hjj2017
 *
 */
public class Result_CanAddTime extends BizResult {
	/** 是否可以增加 ? */
	public boolean _can = false;

	@Override
	protected void clearContent() {
	}
}
