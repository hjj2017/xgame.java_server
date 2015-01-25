package com.game.bizModules.login.serv;

import com.game.part.utils.BizResult;

/**
 * 登陆验证结果
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
public class Result_Auth extends BizResult {
	/** 验证成功 */
	public boolean _success = false;

	@Override
	protected void clearContent() {
		this._success = false;
	}
}
