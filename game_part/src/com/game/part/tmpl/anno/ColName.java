package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 列名称
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColName {
	/** Excel 列名称 A-Z, AA-ZZ */
	String value() default "";
}
