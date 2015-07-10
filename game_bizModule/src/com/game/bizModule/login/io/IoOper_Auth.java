package com.game.bizModule.login.io;

import com.game.bizModule.login.msg.GGAuthFinished;
import com.game.bizModule.login.serv.auth.IAuthorize;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractLoginIoOper;

/**
 * 异步登陆验证
 *
 * @author hjj2017
 * @since 2015/7/9
 *
 */
public class IoOper_Auth extends AbstractLoginIoOper {
    /** 玩家对象 */
    public Player _p = null;
    /** 登陆串 */
    public String _loginStr = null;
    /** 登陆验证实现 */
    public IAuthorize _authImpl = null;

    @Override
    public long getBindUId() {
        return 0L;
    }

    @Override
    public boolean doIo() {
        // 验证登录字符串
        boolean success = this._authImpl.auth(this._loginStr);

        this.msgDispatch(new GGAuthFinished());
        return true;
    }
}
