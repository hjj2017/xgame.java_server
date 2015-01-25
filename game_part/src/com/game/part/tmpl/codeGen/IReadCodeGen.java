package com.game.part.tmpl.codeGen;

import java.lang.reflect.Field;

/**
 * 代码生成接口
 * 
 * @author hjj2019
 * @since 2014/10/2
 * 
 */
public interface IReadCodeGen {
	/**
	 * 构建读取代码
	 *
	 * @param f 字段
	 * @param codeCtx 代码上下文
	 * 
	 */
	void genReadCode(Field f, CodeContext codeCtx);
}
