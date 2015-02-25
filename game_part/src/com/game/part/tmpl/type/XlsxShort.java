package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowStream;

/**
 * Excel Short 字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxShort extends BasicTypeCol<Short> {
	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxShort() {
	}

	/**
	 * 类参数构造器
	 * 
	 * @param defaultVal
	 * 
	 */
	public XlsxShort(Short defaultVal) {
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
	public static XlsxShort ifNullThenCreate(XlsxShort objVal) {
		if (objVal == null) {
			objVal = new XlsxShort();
		}

		return objVal;
	}

	@Override
	protected void readImpl(XSSFRowStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readShort();
		}
	}
}
