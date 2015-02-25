package com.game.bizModules.building.tmpl;

import java.util.List;

import com.game.part.tmpl.IXlsxValidator;
import com.game.part.utils.Assert;

/**
 * 验证器
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
public class Valid_BuildingTmpl implements IXlsxValidator<BuildingTmpl> {
	/**
	 * 验证数据列表
	 * 
	 * @param tl
	 * 
	 */
	public void validate(List<BuildingTmpl> tl) {
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
