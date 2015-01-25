package com.game.core.tmpl.anno;

/**
 * 整数范围
 * 
 * @author hjj2019
 * @since 2014/10/1
 *
 */
public @interface IntRange {
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
}
