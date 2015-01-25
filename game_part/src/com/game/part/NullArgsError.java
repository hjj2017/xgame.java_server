package com.game.part;

/**
 * 参数错误
 * 
 * @author haijiang
 * @since 2012/6/3
 *
 */
public class NullArgsError extends Error {
	/** serialVersionUID */
	private static final long serialVersionUID = 9070716904017022631L;

	/**
	 * 类默认构造器
	 * 
	 */
	public NullArgsError() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * 
	 */
	public NullArgsError(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param err
	 * 
	 */
	public NullArgsError(Throwable err) {
		super(err);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param err
	 * 
	 */
	public NullArgsError(String msg, Throwable err) {
		super(msg, err);
	}
}
