package com.game.bizModules.building.tmpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.part.tmpl.anno.OneToMany;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.tmpl.anno.Validator;
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
@Validator(clazz = Valid_BuildingTmpl.class)
public class BuildingTmpl extends AbstractXlsxTmpl {
	/** ID */ 
	@OneToOne(groupName = "ID")
	public XlsxInt _ID;
	/** 所在城市 Id */
	@OneToMany(groupName = "cityId")
	public XlsxInt _cityId;
	/** 建筑名称 */
	public XlsxStr _name;
	/** X 坐标 */
	public XlsxInt _posX;
	/** Y 坐标 */
	public XlsxInt _posY;

	/** ID 字典 */
	@OneToOne(groupName = "ID")
	public static Map<Integer, BuildingTmpl> _IDMap = new HashMap<>();
	/** 城市建筑字典 */
	@OneToMany(groupName = "cityId")
	public static Map<Integer, List<BuildingTmpl>> _cityBuildingMap = new HashMap<>();
}
