package com.game.core;

/**
 * 游戏异常
 * 
 * @author haijiang
 * @since 2012/6/3
 *
 */
public class Error extends RuntimeException {
	/** serialVersionUID */
	private static final long serialVersionUID = -1001L;

	/**
	 * 类默认构造器
	 * 
	 */
	public Error() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * 
	 */
	public Error(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param err
	 * 
	 */
	public Error(Throwable err) {
		super(err);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param err
	 * 
	 */
	public Error(String msg, Throwable err) {
		super(msg, err);
	}
}
