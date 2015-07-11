package com.game.bizModule.login;

import com.game.gameServer.framework.Player;

/**
 * 授权信息
 *
 * @author hjj2017
 * @since 2015/7/11
 *
 */
public final class AuthData {
    /** 平台 UId */
    public String _platformUId = null;
    /** 用户名 */
    public String _userName = null;
    /** Pf */
    public String _pf = null;
    /** 创建时间 */
    public long _createTime = 0L;
    /** 最后登录时间 */
    public long _lastLoginTime = 0L;
    /** 最后登录 IP 地址 */
    public String _lastLoginIpAddr = null;

    /**
     * 获取 IO 操作所用的绑定 Id
     *
     * @return
     *
     */
    public long getIoBindUId() {
        if (this._platformUId == null ||
            this._platformUId.isEmpty()) {
            return 0L;
        } else {
            return this._platformUId.charAt(this._platformUId.length() - 1);
        }
    }
}
