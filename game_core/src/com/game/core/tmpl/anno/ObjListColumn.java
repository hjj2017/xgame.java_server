package com.game.core.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 列
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjListColumn {
	/** 列名称数组 */
	ObjColumn[] value();
}
