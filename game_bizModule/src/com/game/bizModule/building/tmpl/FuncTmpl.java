package com.game.bizModule.building.tmpl;

import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxStr;

/**
 * 功能模板
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
public class FuncTmpl extends AbstractXlsxTmpl {
	/** 功能 ID */
	public XlsxInt _ID;
	/** 名称 */
	public XlsxStr _name;
	/** 说明 */
	public XlsxStr _desc;
}
