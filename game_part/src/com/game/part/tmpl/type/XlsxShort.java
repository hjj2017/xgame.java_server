package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

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

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readShort();
		}
	}
}
