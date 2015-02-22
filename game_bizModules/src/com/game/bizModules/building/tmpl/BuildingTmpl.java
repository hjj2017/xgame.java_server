package com.game.bizModules.building.tmpl;

import java.util.List;

import com.game.part.tmpl.XCol;
import com.game.part.tmpl.anno.Id;
import com.game.part.tmpl.anno.ElementCount;
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
	/** ID */ @Id
	public XCol<Integer> _Id;
	/** 城市 ID */
	public XCol<Integer> _cityId;
	/** 建筑名称 */
	public XCol<String> _name;
	/** 说明 */
	public XCol<String> _desc;
	/** 图片 */
	public XCol<String> _img;
	/** X 坐标 */
	public XCol<Integer> _posX;
	/** Y 坐标 */
	public XCol<Integer> _posY;
	/** 建筑类型 */
	@ElementCount(2)
	public XCol<List<Integer>> _typeIdList;
	/** 功能列表 */ 
	@ElementCount(5)
	public XCol<List<FuncTmpl>> _funcList;
}
