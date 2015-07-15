package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定由哪个类进行验证
 * 
 * @author hjj2017
 * @since 2014/6/24
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {
	/** 打包类 */
	Class<?> clazz();
}
