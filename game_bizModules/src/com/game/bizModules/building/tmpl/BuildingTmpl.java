package com.game.bizModules.building.tmpl;

import com.game.part.tmpl.anno.Id;
import com.game.part.tmpl.anno.Validator;
import com.game.part.tmpl.anno.XlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxPlainList;
import com.game.part.tmpl.type.XlsxStr;

/**
 * 建筑模板
 * 
 * @author hjj2017
 * @since 2014/6/5
 * 
 */
@XlsxTmpl(fileName = "Building.xlsx", sheetIndex = 0, startRow = 2)
@Validator(clazz = BuildingTmpl_Validator.class)
public class BuildingTmpl {
	/** ID */ @Id
	public XlsxInt _id;
	/** 建筑名称 */
	public XlsxStr _name;
//	/** 说明 */
//	public XCol<String> _desc;
//	/** 图片 */
//	public XCol<String> _img;
	/** X 坐标 */
	public XlsxInt _posX;
	/** Y 坐标 */
	public XlsxInt _posY;
	/** 建筑类型 */
	public XlsxPlainList<XlsxInt> _typeIdList;
//	/** 功能列表 */ 
//	@ElementCount(5)
//	public XCol<List<FuncTmpl>> _funcList;
}
