package com.game.bizModules.utils;

import java.lang.reflect.Array;
import java.util.BitSet;

import com.game.part.utils.Assert;
import com.game.part.utils.KeyValPair;


/**
 * 变化值可以被记录的对象
 * 
 * @author hjj2019
 * @since 2013/3/21
 * 
 */
public class ChangedRecordableObject<T extends Number> {
	/** 值数组 */
	private final T[] _valArr;
	/** 变化值索引 */
	private final BitSet _chSet;
	/** 缓存键值对数组 */
	private final KeyValPair<Integer, T>[] _cachedKVArr;

	/**
	 * 类参数构造器
	 * 
	 * @param clazz
	 * @param capacity 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ChangedRecordableObject(Class<T> clazz, int capacity) {
		// 断言参数不为空
		Assert.notNull(clazz, "clazz");
		Assert.isTrue(capacity > 0, "capacity <= 0");

		// 创建对象数组
		Object obj = Array.newInstance(
			clazz, capacity
		);
		
		// 初始化值数组、变化索引和键值对数组
		this._valArr = (T[])obj;
		this._chSet = new BitSet(capacity);
		this._cachedKVArr = new KeyValPair[capacity];
	}

	/**
	 * 获取数值
	 * 
	 * @param index
	 * @return 
	 * 
	 */
	public T getVal(int index) {
		if (index < 0 || 
			index >= this._valArr.length) {
			return null;
		} else {
			return this._valArr[index];
		}
	}

	/**
	 * 设置数值, 并记录变化的数值
	 * 
	 * @param index
	 * @param val 
	 * 
	 */
	public void setVal(int index, T val) {
		if (index < 0 || 
			index >= this._valArr.length) {
			return;
		}

		// 获取旧数值
		T old = this._valArr[index];
		// 是否有变 ?
		boolean changeFlag = false;

		if (old != null) {
			// 老值与新值不等 ?
			changeFlag = !old.equals(val);
		} else {
			// 新值不为空 ?
			changeFlag = (val != null);
		}

		if (!changeFlag) {
			return;
		}

		// 设置新值和变化索引
		this._valArr[index] = val;
		this._chSet.set(index);
	}

	/**
	 * 是否存在变化值
	 * 
	 * @return 
	 * 
	 */
	public boolean hasChanged() {
		return !this._chSet.isEmpty();
	}

	/**
	 * 清除变化记录
	 * 
	 */
	public void clearChanged() {
		this._chSet.clear();
	}

	/**
	 * 获取容量
	 * 
	 * @return 
	 * 
	 */
	public int capacity() {
		return _valArr.length;
	}

	/**
	 * 取得被修改过的的属性索引及其对应的值
	 * 
	 * @return 
	 * 
	 */
	public KeyValPair<Integer, T>[] getChanged() {
		// 获取变化值数量
		int count = this._chSet.cardinality();

		if (count <= 0) {
			return null;
		}

		// 创建结果数组
		KeyValPair<Integer, T>[] resultArr = KeyValPair.newKeyValPairArray(count);
		// 临时索引
		int i, j = 0;

		for (i = this._chSet.nextSetBit(0); i >= 0; i = _chSet.nextSetBit(i + 1), j++) {
			if (this._cachedKVArr[i] == null) {
				this._cachedKVArr[i] = new KeyValPair<>();
			}

			// 设置键值对引用
			resultArr[j] = this._cachedKVArr[i];

			// 创建键和值
			resultArr[j].setKey(i);
			resultArr[j].setVal(this._valArr[i]);
		}

		return resultArr;
	}
}
