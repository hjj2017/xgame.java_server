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

    /**
     * 延迟入口 UId
     */
    private final String _lazyEntryUUId;

    /**
     * DAO
     */
    private final PlayerDAO _dao;

    /**
     * 类参数构造器
     *
     * @param p 玩家模型
     */
    public PlayerLazyEntry(PlayerModel p) {
        _p = p;
        _lazyEntryUUId = String.valueOf(p.getUUId());
        _dao = new PlayerDAO();
    }

    @Override
    public String getUId() {
        return _lazyEntryUUId;
    }

    @Override
    public void saveOrUpdate() {
        AsyncOperationProcessor.getInstance().process(
            _p.getUUId(),
            () -> _dao.saveOrUpdate(_p)
        );
    }

    @Override
    public void delete() {
        AsyncOperationProcessor.getInstance().process(
            _p.getUUId(),
            () -> _dao.delete(_p)
        );
    }
}
