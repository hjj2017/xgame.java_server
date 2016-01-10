package com.game.bizModule.login.bizServ;

import com.game.part.util.BizResultObj;

/**
 * 登陆验证结果
 * 
 * @author hjj2017
 * @since 2014/9/10
 * 
 */
public class Result_Auth extends BizResultObj {
    /** 验证成功 */
    public boolean _success = false;

    @Override
    protected void clearContent() {
        this._success = false;
    }
}
