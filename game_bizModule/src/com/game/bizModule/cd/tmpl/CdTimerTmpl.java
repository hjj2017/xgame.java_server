package com.game.bizModule.cd.tmpl;

import java.util.Map;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.part.tmpl.anno.OneToMany;
import com.game.part.tmpl.anno.Validator;
import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.util.Assert;

/**
 * Cd 计时器模板
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
@FromXlsxFile(fileName = "cd.xlsx", sheetIndex = 0)
@Validator(clazz = CdTimerTmpl_Validator.class)
public class CdTimerTmpl {
	/** Cd 类型 Int */
	@OneToMany(groupName = "ID")
	public Integer _cdTypeInt = null;

	/** 阈值 */
	public Long _threshold = null;

	/** Cd 字典 */
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
