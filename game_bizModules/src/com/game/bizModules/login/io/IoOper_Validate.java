package com.game.bizModules.login.io;

import com.game.bizModules.login.handler.Handler_GGLoginAuthResult;
import com.game.bizModules.login.msg.GGLoginAuthResult;
import com.game.bizModules.login.serv.LoginServ;
import com.game.bizModules.login.serv.Result_Auth;
import com.game.gameServer.io.AbstractBindUUIdIoOper;

/**
 * 登陆验证异步操作
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
public class IoOper_Validate extends AbstractBindUUIdIoOper {
	/** 登陆字符串 */
	private String _loginStr = null;

	/**
	 * 内部消息对象 
	 * @see Handler_GGLoginAuthResult
	 */
	private GGLoginAuthResult _iMsg = new GGLoginAuthResult();

	/**
	 * 类参数构造器
	 * 
	 * @param loginStr
	 * 
	 */
	public IoOper_Validate(String loginStr) {
		this._loginStr = loginStr;
	}

	@Override
	public long getBindUUId() {
		return 0L;
	}

	@Override
	public boolean doInit() {
		return true;
	}

	@Override
	public boolean doIo() {
		// 验证登陆字符串
		Result_Auth result = LoginServ.OBJ.auth(this._loginStr);
		// 设置成功标志
		this._iMsg._success = result._success;
		// 验证完成以后, 分派消息
		// 目的是回场景线程继续向下执行逻辑...
		// 具体可以看 Handler_GGLoginValidateResult
		this.msgDispatch(this._iMsg);
		return true;
	}
}
