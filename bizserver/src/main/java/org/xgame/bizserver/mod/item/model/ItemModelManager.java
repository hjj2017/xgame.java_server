package org.xgame.bizserver.mod.item.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemModelManager {
    private final Map<Long, ItemModel> _itemModelMap = new ConcurrentHashMap<>();

    public Collection<ItemModel> getItemModelALL() {
        return _itemModelMap.values();
    }
}
