package com.game.part;

/**
 * 参数错误
 * 
 * @author haijiang
 *
 */
public class InvalidArgsError extends Error {
	/** serialVersionUID */
	private static final long serialVersionUID = 7400420633310393852L;

	/**
	 * 类默认构造器
	 * 
	 */
	public InvalidArgsError() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * 
	 */
	public InvalidArgsError(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param err
	 * 
	 */
	public InvalidArgsError(Throwable err) {
		super(err);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param err
	 * 
	 */
	public InvalidArgsError(String msg, Throwable err) {
		super(msg, err);
	}
}
