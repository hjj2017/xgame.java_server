package com.game.bizModule.login.io;

import com.game.bizModule.login.msg.GGAuthFinish;
import com.game.bizModule.login.bizServ.auth.IAuthorize;
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
        return AbstractLoginIoOper.getBindUIdByPlayer(this._p);
    }

    @Override
    public boolean doIo() {
        // 验证登录字符串
        boolean authSuccess = this._authImpl.auth(
            this._p,
            this._loginStr
        );


        // 登陆验证完成消息
        GGAuthFinish ggMSG = new GGAuthFinish();
        ggMSG._success = authSuccess;
        ggMSG._p = this._p;
        // 分派 GG 消息
        this.msgDispatch(ggMSG);

        return true;
    }
}
