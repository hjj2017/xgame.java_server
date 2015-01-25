package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符串长度
 * 
 * @author hjj2019
 * @since 2014/10/1
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrLen {
	int min() default 0;
	int max() default Integer.MAX_VALUE;
}
