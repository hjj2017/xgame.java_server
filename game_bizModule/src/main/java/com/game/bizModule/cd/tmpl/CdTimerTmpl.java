package com.game.bizModule.cd.tmpl;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.tmpl.type.*;
import com.game.part.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Cd 计时器模板
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
@FromXlsxFile(fileName = "cd.xlsx", sheetIndex = 0)
public class CdTimerTmpl extends AbstractXlsxTmpl {
	/** Cd 类型 Int */
	@OneToOne(groupName = "_typeInt")
	public XlsxInt _cdTypeInt = new XlsxInt(false);
	/** Cd 名称 */
	public XlsxStr _cdName = null;
	/** Cd 图标 */
	public XlsxStr _cdIcon = null;
	/** 冷却时间阈值 */
	public XlsxLong _threshold = new XlsxLong(false);
	/** 默认开启? */
	public XlsxBool _defaultOpen = null;
	/** 是否可以秒? */
	public XlsxBool _canKilling = null;
	/** 秒 Cd ( 最小 ) 时间单位 */
	public XlsxLong _killingTimeUnit = null;
	/** 秒 Cd 所需金币 */
	public XlsxInt _killingNeedGold = null;

	/** Cd 字典 */
	@OneToOne(groupName = "_typeInt")
	public static final Map<Integer, CdTimerTmpl> _typeIntMap = new HashMap<>();

	/**
	 * 获取计时器模板
	 *
	 * @param cdType
	 * @return
	 *
	 */
	public static CdTimerTmpl getByCdType(CdTypeEnum cdType) {
		// 断言参数不为空
		Assert.notNull(cdType);
		// 获取模板对象
		return _typeIntMap.get(cdType.getIntVal());
	}
}
