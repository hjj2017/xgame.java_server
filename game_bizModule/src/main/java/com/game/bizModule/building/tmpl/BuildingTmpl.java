package com.game.bizModule.building.tmpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.gameServer.framework.GameServerConf;
import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.anno.OneToMany;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.tmpl.anno.Validator;
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
@FromXlsxFile(fileName = "building.xlsx", sheetIndex = 0)
public class BuildingTmpl extends AbstractXlsxTmpl {
	/** Id */
	@OneToOne(groupName = "_typeInt")
	public XlsxInt _typeInt = new XlsxInt(false);
	/** 建筑名称 */
	public XlsxStr _buildingName;
	/** 建筑说明 */
	public XlsxStr _buildingDesc;
	/** 开启所需角色等级 */
	public XlsxInt _openingNeedHumanLevel;

	/** Id 字典 */
	@OneToOne(groupName = "_typeInt")
	public static Map<Integer, BuildingTmpl> _typeIntMap = new HashMap<>();

	/**
	 * 根据建筑类型获取模版
	 *
	 * @param bt
	 * @return
	 *
	 */
	public static BuildingTmpl getByBuildingType(BuildingTypeEnum bt) {
		return _typeIntMap.get(bt.getIntVal());
	}
}
