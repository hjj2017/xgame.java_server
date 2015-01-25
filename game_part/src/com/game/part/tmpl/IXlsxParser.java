package com.game.part.tmpl;

import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * Excel 数据行解析器
 * 
 * @author hjj2017
 * @since 2014/9/27 
 * 
 */
public interface IXlsxParser {
	/**
	 * 将 Excel 文件中的一行数据解析为业务对象, 
	 * 注意 : 其实这里可以使用泛型, 
	 * 但是为了让代码足够简单, 所以放弃了泛型定义...
	 * 
	 * @param row
	 * @return
	 */
	Object parse(XSSFRow row);
}
