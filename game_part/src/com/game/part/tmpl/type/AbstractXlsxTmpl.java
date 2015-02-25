package com.game.part.tmpl.type;

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
}
