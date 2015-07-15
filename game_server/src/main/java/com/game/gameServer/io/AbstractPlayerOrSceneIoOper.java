package com.game.gameServer.io;

/**
 * 玩家或场景异步操作
 * 
 * @author haijiang.jin
 * @since 2012/12/31
 * 
 */
public abstract class AbstractPlayerOrSceneIoOper implements IBindUIdIoOper, IMsgDispatchable {
    @Override
    public final String getThreadKey() {
        return getThreadKey(this.getBindUId());
    }

    /**
     * 获取关键字
     *
     * @param UId
     * @return
     *
     */
    public static String getThreadKey(long UId) {
        return "PLAYER_OR_SCENE."
            + IoConf.OBJ.getPlayerOrSceneThreadIndex(UId);
    }
}
