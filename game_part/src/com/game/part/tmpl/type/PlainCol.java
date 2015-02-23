package com.game.part.tmpl.type;

import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * 普通数值列
 * 
 * @author hjj2019
 * @param <T>
 * 
 */
public abstract class PlainCol<T> extends AbstractXlsxCol<T> {
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

	/**
	 * 验证字段的正确性
	 * 
	 * @return
	 * 
	 */
	public void validate() {
	}

	/**
	 * 更新数值
	 * 
	 * @param cell
	 * @param xlsxFileName
	 * 
	 */
	abstract void update(XSSFCell cell, String xlsxFileName);

	@Override
	public String toString() {
		// 创建字符串缓冲区
		StringBuffer sb = new StringBuffer();
		// 添加属性
		sb.append(this.getClass().getSimpleName());
		sb.append(" { ");
		sb.append("_xlsxFileName = ");
		sb.append(this._xlsxFileName);
		sb.append(", _sheetName = ");
		sb.append(this._sheetName);
		sb.append(", _rowIndex = ");
		sb.append(this._rowIndex);
		sb.append(", _colIndex = ");
		sb.append(this._colIndex);
		sb.append(", _objVal = ");
		sb.append(this._objVal);
		sb.append(" }");
		// 返回字符串
		return sb.toString();
	}
}
