package com.game.bizModule.login.bizServ.auth;

import com.game.gameServer.framework.Player;

/**
 * 登陆验证器
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
public interface IAuthorize {
    /**
     * 根据登陆串进行授权验证
     *
     * @param p
     * @param loginStr
     * @return 
     * 
     */
    boolean auth(Player p, String loginStr);
}
