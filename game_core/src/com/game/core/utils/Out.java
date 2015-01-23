package com.game.core.utils;

/**
 * 输出参数
 * 
 * @author hjj2017
 * @param <T> 
 * 
 */
public class Out<T> {
	/** 参数值 */
	private T _val = null;

	/**
	 * 获取参数值
	 * 
	 * @return 
	 * 
	 */
	public T getVal() {
		return this._val;
	}

	/**
	 * 设置参数值
	 * 
	 * @param value 
	 * 
	 */
	public void setVal(T value) {
		this._val = value;
	}

	/**
	 * 如果输出参数不为空则设置数值
	 * 
	 * @param out
	 * @param val
	 * 
	 */
	public static <T> void putVal(Out<T> out, T val) {
		if (out != null) {
			out.setVal(val);
		}
	}
}

