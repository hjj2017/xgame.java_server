package com.game.bizModule.login.serv.auth;

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
    /** 是不是管理员? */
    public boolean _isAdmin = false;

    /**
     * 获取 IO 操作所用的绑定 Id
     *
     * @return
     *
     */
    public long getIoBindUId() {
        if (this._platformUId == null ||
            this._platformUId.isEmpty()) {
            // 如果平台 UId 为空,
            // 则直接退出!
            return 0L;
        }

        if (this._platformUId.length() == 1) {
            // 如果平台 UId 就一个字符,
            // 则直接返回!
            return this._platformUId.charAt(0);
        }

        // 获取字符串长度
        int strLen = this._platformUId.length();
        // 获取最后两位字符
        long n1 = this._platformUId.charAt(strLen - 2);
        long n0 = this._platformUId.charAt(strLen - 1);
        // 换算为 10 进制数
        return n1 * 10 + n0;
    }
}
