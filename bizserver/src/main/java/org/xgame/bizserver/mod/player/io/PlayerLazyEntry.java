package org.xgame.bizserver.mod.player.io;

import org.xgame.bizserver.mod.player.model.PlayerModel;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.lazysave.ILazyEntry;

/**
 * 玩家延迟入口
 */
public class PlayerLazyEntry implements ILazyEntry {
    /**
     * 玩家模型
     */
    private final PlayerModel _p;

    private final String _lazyEntryUUId;

    /**
     * 类参数构造器
     *
     * @param p 玩家模型
     */
    public PlayerLazyEntry(PlayerModel p) {
        _p = p;
        _lazyEntryUUId = String.valueOf(p.getUUId());
    }

    @Override
    public String getUId() {
        return _lazyEntryUUId;
    }

    @Override
    public void saveOrUpdate() {
        AsyncOperationProcessor.getInstance().process(
            _p.getUUId(),
            () -> {
            }
        );
    }

    @Override
    public void delete() {
        AsyncOperationProcessor.getInstance().process(
            _p.getUUId(),
            () -> {
            }
        );
    }
}
