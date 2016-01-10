package com.game.bizModule.human;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.bizModule.util.ChangedRecordableObject;
import com.game.part.util.Assert;
import com.game.part.util.KeyValPair;

/**
 * 角色属性对象, 注意 : 该属性对象与 BattlePropsObj 不同!
 * BattlePropsObj 只会由战斗业务移除特定的某个加数或乘数, 
 * 不会清除所有加数和乘数列表!
 * 而当前类, 会在每一次重新计算时, 
 * 清除所有加数列表和乘数列表, 
 * 并在重新构建列表后进行数值计算!
 * 
 * 另外, 
 * 
 * 当前类会对变化值做增量试的记录
 * 
 * @author hjj2019 
 * @since 2013/3/21
 * 
 */
class CharPropsObj<T extends Number> {
    /** 原始值 */
    private ChangedRecordableObject<T> _origValObj = null;
    /** 当前值 */
    private ChangedRecordableObject<T> _currValObj = null;
    /** 加数字典 */
    private Map<Integer, Map<CharPropsTypeEnum, List<T>>> _addMap = null;
    /** 乘数字典 */
    private Map<Integer, Map<CharPropsTypeEnum, List<T>>> _mulMap = null;

    /**
     * 类参数构造器
     * 
     * @param clazz
     * 
     */
    CharPropsObj(Class<T> clazz) {
        // 断言参数不为空
        Assert.notNull(clazz);

        // 容量等于枚举个数
        int capacity = CharPropsTypeEnum.values().length + 1;
        
        // 初始化原始值和当前值
        this._origValObj = new ChangedRecordableObject<T>(clazz, capacity);
        this._currValObj = new ChangedRecordableObject<T>(clazz, capacity);

        // 初始化加数字典和乘数字典
        this._addMap = new HashMap<>();
        this._mulMap = new HashMap<>();
    }

    /**
     * 获取原始值
     * 
     * @param propsType
     * @return 
     * 
     */
    public T getOriginalVal(CharPropsTypeEnum propsType) {
        if (propsType == null) {
            return null;
        } else {
            return this._origValObj.getVal(
                propsType.getIntVal()
            );
        }
    }

    /**
     * 设置原始值
     * 
     * @param propsType
     * @param value 
     * 
     */
    public void setOriginalVal(CharPropsTypeEnum propsType, T value) {
        if (propsType == null) {
            return;
        } else {
            this._origValObj.setVal(
                propsType.getIntVal(),
                value
            );
        }
    }

    /**
     * 获取当前值
     * 
     * @param propsType
     * @return 
     * 
     */
    public T getCurrVal(CharPropsTypeEnum propsType) {
        if (propsType == null) {
            return null;
        } else {
            return this._currValObj.getVal(
                propsType.getIntVal()
            );
        }
    }

    /**
     * 设置当前值
     * 
     * @param propsType
     * @param value 
     * 
     */
    void setCurrVal(CharPropsTypeEnum propsType, T value) {
        if (propsType == null) {
            return;
        } else {
            this._currValObj.setVal(
                propsType.getIntVal(),
                value
            );
        }
    }

    /**
     * 增加加数, 例如 "攻击力+10", 会记录攻击力和 10
     * 
     * @param sysKey 
     * @param propsType
     * @param value
     * 
     */
    public void addAddition(int sysKey, CharPropsTypeEnum propsType, T value) {
        // 增加加数
        addX(sysKey, propsType, value, this._addMap);
    }

    /**
     * 添加数值到属性字典
     * 
     * @param sysKey
     * @param propsType
     * @param value
     * @param map
     * 
     */
    private static <T> void addX(
        int sysKey, 
        CharPropsTypeEnum propsType, 
        T value, 
        Map<Integer, Map<CharPropsTypeEnum, List<T>>> map) {

        if (propsType == null || 
            value == null) {
            // 如果属性类型为空, 或者
            // 数值为空, 
            // 则直接退出!
            return;
        }

        if (map == null) {
            // 如果属性字典为空, 
            // 则直接退出!
            return;
        }

        // 获取内置字典
        Map<CharPropsTypeEnum, List<T>> innerMap = map.get(sysKey);

        if (innerMap == null) {
            // 如果内置字典为空, 
            // 则创建内置字典并加入根字典
            innerMap = new HashMap<>();
            map.put(sysKey, innerMap);
        }
        
        // 获取加数数值列表
        List<T> vl = innerMap.get(propsType);

        if (vl == null) {
            // 创建并添加属性增加值列表
            vl = new ArrayList<>(32);
            innerMap.put(propsType, vl);
        }

        vl.add(value);
    }

    /**
     * 清除所有加数
     * 
     */
    public void clearAdd() {
        if (this._addMap != null) {
            this._addMap.clear();
        }
    }

    /**
     * 清除指定系统的所有加数
     * 
     * @param sysKey
     * 
     */
    public void clearAdd(int sysKey) {
        // 获取内置字典
        Map<?, ?> innerMap = this._addMap.get(sysKey);

        if (innerMap == null || 
            innerMap.isEmpty()) {
            // 如果内置字典为空, 
            // 则直接退出!
            return;
        }

        // 清除内置字典
        innerMap.clear();
        this._addMap.remove(sysKey);
    }

    /**
     * 获取加数列表
     * 
     * @param propsType
     * @return 
     * 
     */
    public List<T> getAddList(CharPropsTypeEnum propsType) {
        // 获取加数列表
        return getXList(propsType, this._addMap);
    }

    /**
     * 获取 X 列表
     * 
     * @param propsType
     * @param map 
     * @return 
     * 
     */
    private static <T> List<T> getXList(
        CharPropsTypeEnum propsType, 
        Map<Integer, Map<CharPropsTypeEnum, List<T>>> map) {

        if (propsType == null) {
            // 如果属性类型为空, 
            // 则直接退出!
            return null;
        }

        if (map == null || 
            map.isEmpty()) {
            // 如果属性字典为空, 
            // 则直接退出!
            return null;
        }

        // 创建结果列表
        List<T> resultList = new ArrayList<>();

        for (Map<CharPropsTypeEnum, List<T>> innerMap : map.values()) {
            // 获取加数列表
            List<T> addList = innerMap.get(propsType);

            if (addList == null || 
                addList.isEmpty()) {
                continue;
            }

            // 添加加数列表
            resultList.addAll(addList);
        }

        return resultList;
    }

    /**
     * 添加乘数, 例如 "攻击力+10%", 会记录攻击力和 10%
     * 
     * @param sysKey 系统关键字
     * @param propsType
     * @param value
     * 
     */
    public void addMul(int sysKey, CharPropsTypeEnum propsType, T value) {
        // 添加乘数
        addX(sysKey, propsType, value, this._mulMap);
    }

    /**
     * 清除所有乘数
     * 
     */
    public void clearMul() {
        if (this._mulMap != null) {
            this._mulMap.clear();
        }
    }

    /**
     * 清除指定系统的所有乘数
     * 
     * @param sysKey
     * 
     */
    public void clearMul(int sysKey) {
        // 获取内置字典
        Map<?, ?> innerMap = this._mulMap.get(sysKey);

        if (innerMap == null || 
            innerMap.isEmpty()) {
            // 如果内置字典为空, 
            // 则直接退出!
            return;
        }

        // 清除内置字典
        innerMap.clear();
        this._mulMap.remove(sysKey);
    }

    /**
     * 获取乘数列表
     * 
     * @param propsType
     * @return 
     * 
     */
    public List<T> getMulList(CharPropsTypeEnum propsType) {
        // 获取乘数列表
        return getXList(propsType, this._mulMap);
    }

    /**
     * 获取已修改的键值对数组
     * 
     * @return 
     * 
     */
    public KeyValPair<Integer, T>[] getChangedVal() {
        return this._currValObj.getChanged();
    }

    /**
     * 清除变化值
     * 
     */
    public void clearChanged() {
        this._currValObj.clearChanged();
    }
}
