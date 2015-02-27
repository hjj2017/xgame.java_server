package com.game.part.tmpl;

import java.text.MessageFormat;

import com.game.part.tmpl.type.AbstractXlsxCol;

/**
 * 模板错误
 * 
 * @author hjj2017
 * @since 2014/6/13
 * 
 */
public class XlsxTmplError extends RuntimeException {
	// serialVersionUID
	private static final long serialVersionUID = -8461262413672475675L;

	/**
	 * 类默认构造器
	 * 
	 */
	public XlsxTmplError() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * 
	 */
	public XlsxTmplError(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param err
	 * 
	 */
	public XlsxTmplError(Throwable err) {
		super(err);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param err
	 * 
	 */
	public XlsxTmplError(String msg, Throwable err) {
		super(msg, err);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param col
	 * @param msg
	 * 
	 */
	public XlsxTmplError(AbstractXlsxCol<?> col, String msg) {
		super(MessageFormat.format(
			"文件 {0} 页签 {1} 第 {2} 行, 第 {3} ( {4} ) 列错误! 具体原因为 : {5}",
			col.getXlsxFileName(), 
			col.getSheetName(), 
			col.getRowIndex(), 
			col.getColIndex(), 
			XSSFUtil.getColName(col.getColIndex()), 
			msg
		));
	}
}
