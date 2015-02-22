package com.game.part.tmpl;

/**
 * Excel 列
 * 
 * @author hjj2017
 * @since 2015/2/21
 * 
 */
public class XCol<T> {
	/** 所在 Xlsx 文件名称 */
	String _xlsxFilename = null;
	/** 所在页签索引 */
	String _sheetIndex = null;
	/** 列索引 */
	int _colIndex = -1;
	/** 列值 */
	T _objVal = null;

	/**
	 * 获取对象值
	 * 
	 * @return
	 * 
	 */
	public T objVal() {
		return this._objVal;
	}

	/**
	 * 获取 int 数值
	 * 
	 * @return 
	 * 
	 */
	public int intVal() {
		if (this._objVal == null) {
			return 0;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * 获取 long 数值
	 * 
	 * @return
	 * 
	 */
	public long longVal() {
		if (this._objVal == null) {
			return 0L;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).longValue();
		} else {
			return 0L;
		}
	}

	/**
	 * 获取 short 数值
	 * 
	 * @return
	 * 
	 */
	public short shortVal() {
		if (this._objVal == null) {
			return 0;
		} else if (this._objVal instanceof Number) {
			return ((Number)this._objVal).shortValue();
		} else {
			return 0;
		}
	}

	/**
	 * 获取字符串值
	 * 
	 * @return
	 * 
	 */
	public String strVal() {
		if (this._objVal == null) {
			return null;
		} else {
			return String.valueOf(this._objVal);
		}
	}

	/**
	 * 获取 bool 值
	 * 
	 * @return 
	 * 
	 */
	public boolean boolVal() {
		if (this._objVal == null) {
			return false;
		} else if (this._objVal instanceof Number) {
			return this.intVal() == 1;
		} else {
			String strVal = this.strVal();
			return strVal.equalsIgnoreCase("true") || strVal.equalsIgnoreCase("yes") || strVal.equalsIgnoreCase("y");
		}
	}
}
