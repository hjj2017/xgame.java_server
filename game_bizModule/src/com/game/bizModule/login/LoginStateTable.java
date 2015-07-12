package com.game.bizModule.login;

/**
 * 登陆状态表
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class LoginStateTable {
    /** 是否已经有了平台 UId? */
    public boolean _platformUIdOk = false;
    /** 授权中... */
    public boolean _execAuth = false;
    /** 验证完成? */
    public boolean _authSuccess = false;
}
