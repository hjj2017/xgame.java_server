package com.game.gameServer.io;

/**
 * 战斗异步操作
 * 
 * @author hjj2017
 * @since 2014/8/14
 * 
 */
public abstract class AbstractBattleIoOper implements IBindUUIdIoOper, IMsgDispatchable {
    @Override
    public final String getKey() {
        return "BATTLE."
            + IoConf.OBJ.getBattleThreadIndex(this.getBindUUId());
    }
}
