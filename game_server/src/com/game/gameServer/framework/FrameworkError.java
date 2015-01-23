package com.game.gameServer.framework;

import com.game.core.Error;

/**
 * 内核错误
 * 
 * @author haijiang
 *
 */
public class FrameworkError extends Error {
	/** serialVersionUID */
	private static final long serialVersionUID = -1;

	/**
	 * 类默认构造器
	 * 
	 */
	public FrameworkError() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 */
	public FrameworkError(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param error
	 */
	public FrameworkError(Throwable error) {
		super(error);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param error
	 */
	public FrameworkError(String msg, Throwable error) {
		super(msg, error);
	}
}
