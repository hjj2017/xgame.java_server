package com.game.part.msg.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息序列化 Id
 * 
 * @author hjj2017
 * @since 2015/2/18
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgSerialUId {
	/**
	 * Id 值
	 * 
	 * @return
	 * 
	 */
	short value();
}
