package com.game.part.io;

import com.game.part.GameError;

/**
 * IO 错误
 * 
 * @author haijiang
 *
 */
public class IoOperError extends GameError {
	/** serialVersionUID */
	private static final long serialVersionUID = 3844453167745941900L;

	/**
	 * 类默认构造器
	 * 
	 */
	public IoOperError() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 */
	public IoOperError(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param error
	 */
	public IoOperError(Throwable error) {
		super(error);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param error
	 */
	public IoOperError(String msg, Throwable error) {
		super(msg, error);
	}
}
