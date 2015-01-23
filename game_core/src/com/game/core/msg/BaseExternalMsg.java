package com.game.core.msg;


/**
 * 外部消息
 * 
 * @author hjj2017
 *
 */
public abstract class BaseExternalMsg extends BaseMsg implements Cloneable {
	/**
	 * 复制一个新的对象
	 * 
	 * @return 
	 * 
	 */
	public<T extends BaseExternalMsg> T copy() {
		try {
			// 复制一个新的对象
			@SuppressWarnings("unchecked")
			T obj_copy = (T)super.clone();

			return obj_copy;
		} catch (Exception ex) {
			// 抛出运行时异常
			throw new RuntimeException(ex);
		}
	}
}

