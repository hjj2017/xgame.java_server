package com.game.gameServer.io;

/**
 * 登陆异步操作
 * 
 * @author hjj2017
 * @since 2014/8/14
 * 
 */
public abstract class AbstractLoginIoOper implements IBindUIdIoOper, IMsgDispatchable {
    @Override
    public final String getThreadKey() {
        return "LOGIN."
            + IoConf.OBJ.getLoginThreadIndex(this.getBindUId());
    }
}
