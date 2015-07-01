package com.game.gameServer.io;

/**
 * 玩家或场景异步操作
 * 
 * @author haijiang.jin
 * @since 2012/12/31
 * 
 */
public abstract class AbstractPlayerOrSceneIoOper implements IBindUUIdIoOper, IMsgDispatchable {
    @Override
    public final String getKey() {
        return getKey(this.getBindUUId());
    }

    /**
     * 获取关键字
     *
     * @param UUId
     * @return
     *
     */
    public static String getKey(long UUId) {
        return "PLAYER_OR_SCENE."
            + IoConf.OBJ.getPlayerOrSceneThreadIndex(UUId);
    }
}
