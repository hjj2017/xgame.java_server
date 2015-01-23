package com.game.bizModules.building.tmpl;

import java.text.MessageFormat;
import java.util.List;




import com.game.core.tmpl.XlsxTmplError;
import com.game.core.utils.Assert;

/**
 * 验证器
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class BuildingTmpl_Validator {
	/**
	 * 验证数据列表
	 * 
	 * @param tl
	 * 
	 */
	public static void validate(List<BuildingTmpl> tl) {
		// 断言参数不为空
		Assert.notNullOrEmpty(tl, "tl");
//
//		tl.forEach(t -> {
//			// 断言参数不为空
//			Assert.notNull(t, "t");
//
//			if (t._posX == null || 
//				t._posX < -1024 || 
//				t._posX > +4096) {
//				String errMsg = MessageFormat.format(
//					"建筑坐标错误! ID = {0}", 
//					t._posX
//				);
//				throw new XlsxTmplError(errMsg);
//			}
//		});
	}
}
