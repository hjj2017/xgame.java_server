package com.game.part.tmpl;

import java.util.List;

/**
 * 打包
 * 
 * @author hjj2017
 * 
 */
interface IServ_PackUp {
	/**
	 * 打包
	 * 
	 * @param clazz
	 * 
	 */
	default void packUp(Class<?> clazz) {
		// 获取对象列表
		List<?> objList = XlsxTmplServ.OBJ.getObjList(clazz);

		if (objList == null || 
			objList.isEmpty()) {
			return;
		}

		// 1. 找到 Id 字段
		// 2. 找到 IdMap
		
		// 3. 找到 Map 函数
		// 4. 找到 MapMap
	}
}
