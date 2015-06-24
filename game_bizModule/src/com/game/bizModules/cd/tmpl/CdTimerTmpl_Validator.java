package com.game.bizModules.cd.tmpl;

import java.util.List;






import com.game.bizModules.cd.model.CdTypeEnum;
import com.game.part.tmpl.XlsxTmplError;
import com.game.part.util.Assert;

/**
 * Cd 验证
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class CdTimerTmpl_Validator {
	/**
	 * Cd 验证
	 * 
	 * @param tl
	 * 
	 */
	public static void validate(List<CdTimerTmpl> tl) {
		// 断言参数不为空
		Assert.notNullOrEmpty(tl, "tl");

		tl.forEach(t -> {
			// 断言参数不为空
			Assert.notNull(t, "t");

			if (t._cdTypeInt == null) {
				// 如果 Cd 类型为空, 
				// 则抛出异常!
				throw new XlsxTmplError("Cd 类型为空");
			}

			// 尝试解析 Cd 类型
			CdTypeEnum.parse(t._cdTypeInt);
		});
	}
}
