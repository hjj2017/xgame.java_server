package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 模板
 * 
 * @author hjj2017
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromXlsxFile {
	/** 文件名称 */
	String fileName();
	/** 页签索引 */
	int sheetIndex();
	/** 起始行 */
	int startFromRowIndex() default 1;
}
