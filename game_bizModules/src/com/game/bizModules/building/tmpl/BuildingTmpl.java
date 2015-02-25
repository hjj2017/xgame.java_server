package com.game.bizModules.building.tmpl;

import com.game.part.tmpl.anno.ElementNum;
import com.game.part.tmpl.anno.Id;
import com.game.part.tmpl.anno.Packer;
import com.game.part.tmpl.anno.XlsxTmpl;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxArrayList;
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
	public XlsxInt _ID;
	/** 建筑名称 */
	public XlsxStr _name;
//	/** 说明 */
//	public XCol<String> _desc;
//	/** 图片 */
//	public XCol<String> _img;
//	/** X 坐标 */
//	public XlsxInt _posX;
//	/** Y 坐标 */
//	public XlsxInt _posY;
	/** 建筑类型 */
	@ElementNum(2)
	public XlsxArrayList<XlsxInt> _typeIdList;
	/** 功能 */
	@ElementNum(2)
	public XlsxArrayList<FuncTmpl> _mainFunc;
}
