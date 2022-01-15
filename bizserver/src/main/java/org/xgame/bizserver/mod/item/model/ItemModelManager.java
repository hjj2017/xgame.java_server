package org.xgame.bizserver.mod.item.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 道具管理器
 */
public class ItemModelManager {
    /**
     * 道具模型字典
     */
    private final Map<Long, ItemModel> _itemModelMap = new ConcurrentHashMap<>();

    /**
     * 获取道具模型列表
     *
     * @return 道具模型列表
     */
    public Collection<ItemModel> getItemModelALL() {
        return _itemModelMap.values();
    }

    /**
     * 释放所有资源
     */
    public void free() {
        for (ItemModel currItem : _itemModelMap.values()) {
            if (null != currItem) {
                currItem.free();
            }
        }

        _itemModelMap.clear();
    }
}
