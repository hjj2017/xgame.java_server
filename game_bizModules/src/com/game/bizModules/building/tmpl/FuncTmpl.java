package com.game.bizModules.building.tmpl;

import com.game.part.tmpl.anno.PlainColumn;

/**
 * 功能模板
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
public class FuncTmpl {
	/** 功能 ID */
    @PlainColumn
	public Integer _ID;
	/** 名称 */
    @PlainColumn
	public String _name;
	/** 说明 */
    @PlainColumn
	public String _desc;
}
