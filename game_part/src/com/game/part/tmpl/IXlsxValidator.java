package com.game.part.tmpl;

import java.util.List;

import com.game.part.tmpl.type.AbstractXlsxTmpl;

/**
 * 验证器接口
 * 
 * @author hjj2017
 * @param <T>
 * 
 */
public interface IXlsxValidator<T extends AbstractXlsxTmpl> {
	/**
	 * 验证模板对象列表
	 * 
	 * @param objList
	 * 
	 */
	public void validate(List<T> objList);
}
