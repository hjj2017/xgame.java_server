package com.game.bizModules.building.tmpl;

import java.util.List;
import java.util.Map;

import com.game.part.tmpl.anno.GroupMap;
import com.game.part.tmpl.anno.Id;
import com.game.part.tmpl.anno.IdMap;
import com.game.part.tmpl.anno.IntRange;
import com.game.part.tmpl.anno.ObjColumn;
import com.game.part.tmpl.anno.PlainColumn;
import com.game.part.tmpl.anno.StrLen;
import com.game.part.tmpl.anno.Validator;
import com.game.part.tmpl.anno.XlsxTmpl;

/**
 * 建筑模板
 * 
 * @author hjj2017
 * @since 2014/6/5
 * 
 */
@XlsxTmpl(fileName = "building.xlsx", sheetIndex = 0, startRow = 2)
@Validator(clazz = BuildingTmpl_Validator.class)
public class BuildingTmpl {
	/** ID */
	@PlainColumn @Id
	public Integer _Id;

	/** 城市 ID */
	@PlainColumn
	public Integer _cityId;

	/** 建筑名称 */
	@PlainColumn
	@StrLen(min = 1, max = 10)
	public String _name;

	/** 说明 */
	@PlainColumn(nullable = true)
	public String _desc;

	/** 图片 */
	@PlainColumn(nullable = true)
	public String _img;

	/** X 坐标 */
	@PlainColumn
	@IntRange(min = 0, max = 2048)
	public Integer _posX;

	/** Y 坐标 */
	@PlainColumn
	@IntRange(min = 0, max = 2048)
	public Integer _posY;

	/** 建筑类型 */
	@PlainColumn
	@PlainColumn
	public List<Integer> _typeIdList;

	/** 功能列表 */
	@ObjColumn
	@ObjColumn
	public List<FuncTmpl> _funcList;

//	/** ID 字典 */ @IdMap
//	public static Map<Integer, BuildingTmpl> _IDMap;
//
//	/** 根据城市 ID 分组 */
//	@GroupMap(groupName = "city_")
//	public static Map<Integer, List<BuildingTmpl>> _groupByCityIDMap;
//
//	/**
//	 * 获取分组名称
//	 * 
//	 * @return 
//	 * 
//	 */
//	@GroupRule(groupName = "city_")
//	public int getCityID() {
//		return this._cityID;
//	}
}
