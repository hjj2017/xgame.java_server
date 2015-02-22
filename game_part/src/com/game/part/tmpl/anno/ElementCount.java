package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列表元素个数
 * 
 * @author hjj2017
 * @since 2014/6/6
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementCount {
	/** 长度数值 */
	int value() default 0;
}
