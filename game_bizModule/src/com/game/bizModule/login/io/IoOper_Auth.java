package com.game.bizModule.login.io;

import com.game.bizModule.login.serv.auth.AuthData;
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
        // 获取授权信息
        AuthData authData = this._p.getPropValOrCreate(AuthData.class);
        // 获取绑定 Id
        return authData.getIoBindUId();
    }

    @Override
    public boolean doIo() {
        // 获取授权信息
        AuthData authData = this._p.getPropValOrCreate(AuthData.class);
        // 验证登录字符串
        boolean ok = this._authImpl.auth(
            this._loginStr,
            this._p._loginIpAddr,
            authData
        );

        // 登陆验证完成消息
        GGAuthFinished ggMSG = new GGAuthFinished();
        ggMSG._ok = ok;
        ggMSG._p = this._p;
        // 分派 GG 消息
        this.msgDispatch(ggMSG);

        return true;
    }
}
