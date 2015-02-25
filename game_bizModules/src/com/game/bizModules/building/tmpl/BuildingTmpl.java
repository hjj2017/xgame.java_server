package com.game.bizModules.building.tmpl;

import java.util.List;
import java.util.Map;

import com.game.part.tmpl.anno.ElementNum;
import com.game.part.tmpl.anno.Uniqued;
import com.game.part.tmpl.anno.UniquedMap;
import com.game.part.tmpl.anno.MapFunc;
import com.game.part.tmpl.anno.MapMap;
import com.game.part.tmpl.anno.Validator;
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
@Validator(clazz = Valid_BuildingTmpl.class)
public class BuildingTmpl extends AbstractXlsxTmpl {
	/** ID */ 
	@Uniqued(groupName = "ID")
	public XlsxInt _ID;
	/** ID 字典 */ 
	@UniquedMap(groupName = "ID")
	public static Map<Integer, BuildingTmpl> _IDMap;

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

	@MapMap(groupName = "0")
	public static Map<Integer, List<BuildingTmpl>> _buildingTypeMap;

	@MapFunc(groupName = "0")
	public Integer getTypeIdKey() {
		return this._typeIdList.get(0).intVal();
	}
}
