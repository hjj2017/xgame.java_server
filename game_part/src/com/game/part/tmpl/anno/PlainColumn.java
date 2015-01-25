package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
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
@Repeatable(PlainListColumn.class)
public @interface PlainColumn {
	/** 类名称 A-Z,AA-ZZ */
	String name() default "";
	/** 是否可以为空值 ? 默认为 false */
	boolean nullable() default false;
}
