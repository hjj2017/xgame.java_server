package com.game.part.tmpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.game.part.tmpl.codeGen.Coder_R;

/**
 * Excel 解析器工厂类
 * 
 * @author hjj2017
 * @since 2014/9/27
 *
 */
final class XlsxParserFactory {
	/** 解析器字典 */
	private static final Map<Class<?>, IXlsxParser> _parserMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private XlsxParserFactory() {
	}

	/**
	 * 创建解析器
	 * 
	 * @param <TObj>
	 * @param byClazz
	 * @return
	 * 
	 */
	static IXlsxParser create(Class<?> byClazz) {
		if (byClazz == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取解析器对象
		IXlsxParser parserObj = (IXlsxParser)_parserMap.get(byClazz);

		if (parserObj != null) {
			// 如果解析器不为空, 
			// 则直接返回!
			return parserObj;
		}

		// 动态生成解析器
		parserObj = Coder_R.OBJ.genPerser(byClazz);

		if (parserObj == null) {
			// 如果解析器对象还是为空, 
			// 则抛出异常!
			throw new XlsxTmplError("解析器为空");
		}

		// 添加解析器到字典
		_parserMap.put(byClazz, parserObj);
		// 返回解析器
		return parserObj;
	}
}
