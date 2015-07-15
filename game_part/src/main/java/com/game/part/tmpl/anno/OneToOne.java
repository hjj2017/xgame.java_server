package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个关键字对应一个模板对象
 * 
 * @author hjj2017
 * @since 2015/2/27
 * 
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {
	/** 分组名称 */
	String groupName();
}
