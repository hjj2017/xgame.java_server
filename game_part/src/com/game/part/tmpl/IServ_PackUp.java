package com.game.part.tmpl;

import java.text.MessageFormat;
import java.util.List;

import com.game.part.tmpl.type.AbstractXlsxTmpl;

/**
 * 打包
 * 
 * @author hjj2017
 * @since 2015/2/27
 * 
 */
interface IServ_PackUp {
	/**
	 * 打包
	 * 
	 * @param clazz
	 * 
	 */
	default void packUp(Class<? extends AbstractXlsxTmpl> clazz) {
		// 获取对象列表
		List<? extends AbstractXlsxTmpl> objList = XlsxTmplServ.OBJ.getObjList(clazz);

		if (objList == null || 
			objList.isEmpty()) {
			return;
		}

		// 构建打包器
		IXlsxPacker packer = XlsxPackerMaker.make(clazz);

		if (packer == null) {
			// 如果打包器为空, 
			// 则抛出异常!
			throw new XlsxTmplError(MessageFormat.format(
				"未能构建类 {0} 的打包器", 
				clazz.getName()
			));
		}

		// 给每个对象打包
		objList.forEach(o -> {
			if (o != null) {
				packer.packUp(o);
			}
		});
	}
}
