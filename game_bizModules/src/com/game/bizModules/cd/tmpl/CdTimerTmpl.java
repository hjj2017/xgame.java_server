package com.game.bizModules.cd.tmpl;

import java.util.Map;

import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.part.tmpl.anno.Id;
import com.game.part.tmpl.anno.IdMap;
import com.game.part.tmpl.anno.ColName;
import com.game.part.tmpl.anno.Validator;
import com.game.part.tmpl.anno.XlsxTmpl;
import com.game.part.utils.Assert;

/**
 * Cd 计时器模板
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
@XlsxTmpl(fileName = "cd.xlsx", sheetIndex = 0)
@Validator(clazz = CdTimerTmpl_Validator.class)
public class CdTimerTmpl {
	/** Cd 类型 Int */ @Id
	@ColName("A")
	public Integer _cdTypeInt = null;

	/** 阈值 */
	@ColName("B")
	public Long _threshold = null;

	/** Cd 字典 */ @IdMap
	public static Map<Integer, CdTimerTmpl> _IDMap = null;

	/**
	 * 获取计时器模板
	 * 
	 * @param cdType
	 * @return 
	 * 
	 */
	public static CdTimerTmpl get(CdTypeEnum cdType) {
		// 断言参数不为空
		Assert.notNull(cdType);

		if (_IDMap != null) {
			return _IDMap.get(cdType.intVal());
		} else {
			return null;
		}
	}
}
