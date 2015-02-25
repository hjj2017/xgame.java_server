package com.game.part.tmpl.type;

import com.game.part.tmpl.XSSFRowReadStream;

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

	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			return;
		} else {
			this._objVal = stream.readLong();
		}
	}
}
