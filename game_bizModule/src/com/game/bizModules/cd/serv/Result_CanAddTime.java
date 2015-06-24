package com.game.bizModules.cd.serv;

import com.game.part.util.BizResultObj;

/**
 * 是否可以增加时间 ?
 * 
 * @author hjj2017
 *
 */
public class Result_CanAddTime extends BizResultObj {
	/** 是否可以增加 ? */
	public boolean _can = false;

	@Override
	protected void clearContent() {
	}
}
