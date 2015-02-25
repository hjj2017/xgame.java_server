package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowStream;

/**
 * Excel Bool 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxBool extends BasicTypeCol<Boolean> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxBool() {
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxBool(Boolean defaultVal) {
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
	public static XlsxBool ifNullThenCreate(XlsxBool objVal) {
		if (objVal == null) {
			objVal = new XlsxBool();
		}

		return objVal;
	}

	@Override
	protected void readImpl(XSSFRowStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readBool();
		}
	}
}
