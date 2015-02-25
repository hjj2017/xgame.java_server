package com.game.part.tmpl.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.game.part.tmpl.XSSFRowStream;
import com.game.part.utils.ClazzUtil;

/**
 * 模板类
 * 
 * @author hjj2017
 * @since 2015/2/23
 *
 */
public abstract class AbstractXlsxTmpl extends AbstractXlsxCol<AbstractXlsxTmpl> {
	@Override
	protected void readImpl(XSSFRowStream stream) {
		if (stream == null) {
			return;
		}

		Field[] fArr = this.getClass().getDeclaredFields();

		for (Field f : fArr) {
			try {
				if (AbstractXlsxCol.class.isAssignableFrom(f.getType()) == false) {
					continue;
				}
		
				// 获取 readXSSFRow 函数
				Method m = ClazzUtil.getMethod(f.getType(), "readXSSFRow");
				// 获取字段值
				AbstractXlsxCol<?> fObj = (AbstractXlsxCol<?>)f.get(this);
	
				m.invoke(fObj, stream);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
