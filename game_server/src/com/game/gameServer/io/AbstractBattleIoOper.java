package com.game.gameServer.io;

/**
 * 战斗异步操作
 * 
 * @author hjj2017
 * @since 2014/8/14
 * 
 */
public abstract class AbstractBattleIoOper implements IBindUIdIoOper, IMsgDispatchable {
    @Override
    public final String getThreadKey() {
        return "BATTLE."
            + IoConf.OBJ.getBattleThreadIndex(this.getBindUId());
    }
}
