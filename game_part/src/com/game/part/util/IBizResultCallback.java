package com.game.part.util;

/**
 * 业务结果回调
 * 
 * @author hjj2017
 * @since 2015/3/27
 * 
 */
@FunctionalInterface
public interface IBizResultCallback<R extends BizResultObj> {
	/**
	 * 执行回调函数
	 * 
	 * @param resultObj
	 * 
	 */
	public void doCallback(R resultObj);
}
