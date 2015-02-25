package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowStream;

/**
 * Excel Lang 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxLong extends BasicTypeCol<Long> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxLong() {
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxLong(Long defaultVal) {
		this._objVal = defaultVal;
	}

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static XlsxLong ifNullThenCreate(XlsxLong objVal) {
		if (objVal == null) {
			objVal = new XlsxLong();
		}

		return objVal;
	}

	@Override
	protected void readImpl(XSSFRowStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readLong();
		}
	}
}
