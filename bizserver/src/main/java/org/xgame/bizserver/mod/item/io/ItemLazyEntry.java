package org.xgame.bizserver.mod.item.io;

import org.xgame.bizserver.mod.item.model.ItemModel;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.lazysave.ILazyEntry;

public class ItemLazyEntry implements ILazyEntry {
    /**
     * 当前道具模型
     */
    private final ItemModel _model;

    /**
     * 延迟入口 UId
     */
    private final String _lazyEntryUUId;

    /**
     * 类参数构造器
     *
     * @param model 当前道具模型
     */
    public ItemLazyEntry(ItemModel model) {
        _model = model;
        _lazyEntryUUId = String.valueOf(model.getUUId());
    }

    @Override
    public String getUId() {
        return _lazyEntryUUId;
    }

    @Override
    public void saveOrUpdate() {
        AsyncOperationProcessor.getInstance().process(
            _model.getUUId(),
            () -> {
                _model.unmarkChanged();
            }
        );
    }

    @Override
    public void delete() {
        AsyncOperationProcessor.getInstance().process(
            _model.getUUId(),
            () -> {
                _model.unmarkChanged();
            }
        );
    }
}
