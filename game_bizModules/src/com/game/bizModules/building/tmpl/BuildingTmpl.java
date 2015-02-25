package com.game.bizModules.building.tmpl;

import com.game.part.tmpl.anno.Id;
import com.game.part.tmpl.anno.Packer;
import com.game.part.tmpl.anno.XlsxTmpl;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxStr;

/**
 * 建筑模板
 * 
 * @author hjj2017
 * @since 2014/6/5
 * 
 */
@XlsxTmpl(fileName = "building.xlsx", sheetIndex = 0, startRowIndex = 2)
@Packer(clazz = Pack_BuildingTmpl.class)
public class BuildingTmpl extends AbstractXlsxTmpl {
	/** ID */ @Id
	public XlsxInt _id = new XlsxInt(0);
	/** 建筑名称 */
	public XlsxStr _name = new XlsxStr("");
//	/** 说明 */
//	public XCol<String> _desc;
//	/** 图片 */
//	public XCol<String> _img;
//	/** X 坐标 */
//	public XlsxInt _posX;
//	/** Y 坐标 */
//	public XlsxInt _posY;
//	/** 建筑类型 */
//	@ElementNum(2)
//	public XlsxArrayList<XlsxInt> _typeIdList;
//
//	/** 功能列表 */ 
//	@ElementCount(5)
//	public XCol<List<FuncTmpl>> _funcList;
}
