package com.game.part.tmpl.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 元素个数
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementNum {
    /** 元素个数, 默认 = 1 */
    int value() default 1;
}
