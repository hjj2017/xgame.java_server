package com.game.part.tmpl.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.game.part.tmpl.XSSFRowReadStream;

/**
 * 模板类
 * 
 * @author hjj2017
 * @since 2015/2/23
 *
 */
public abstract class AbstractXlsxTmpl extends AbstractXlsxCol<AbstractXlsxTmpl> {
	@Override
	protected void readImpl(XSSFRowReadStream stream) {
		if (stream == null) {
			// 如果数据流为空, 
			// 则直接退出!
			return;
		}

		// 创建帮助者对象
		IReadHelper helper = ReadHelperFactory.createHelper(this.getClass());

		if (helper != null) {
			// 如果帮助者不为空, 
			// 则读取数据...
			helper.readImpl(this, stream);
		}
	}

	/**
	 * 打包一对一数据到目标字典
	 * 
	 * @param objKey
	 * @param objVal
	 * @param targetMap 
	 * 
	 */
	public static void packOneToOne(Object objKey, AbstractXlsxTmpl objVal, Map<Object, AbstractXlsxTmpl> targetMap) {
		if (objKey == null || 
			objVal == null || 
			targetMap == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		if (objKey instanceof BasicTypeCol<?>) {
			targetMap.put(
				((BasicTypeCol<?>)objKey).objVal(), 
				objVal
			);
		} else {
			targetMap.put(objKey, objVal);
		}
	}

	/**
	 * 打包一对多数据到目标字典
	 * 
	 * @param objKey
	 * @param objVal
	 * @param targetMap 
	 * 
	 */
	public static void packOneToMany(Object objKey, AbstractXlsxTmpl objVal, Map<Object, List<AbstractXlsxTmpl>> targetMap) {
		if (objKey == null || 
			objVal == null || 
			targetMap == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取模板列表
		List<AbstractXlsxTmpl> tmplList = targetMap.get(objKey);

		if (tmplList == null) {
			// 如果模板列表为空, 
			// 则新建列表!
			tmplList = new ArrayList<>();
			
			if (objKey instanceof BasicTypeCol<?>) {
				targetMap.put(
					((BasicTypeCol<?>)objKey).objVal(), 
					tmplList
				);
			} else {
				targetMap.put(objKey, tmplList);
			}
		}

		// 添加数值到列表
		tmplList.add(objVal);
	}
}
