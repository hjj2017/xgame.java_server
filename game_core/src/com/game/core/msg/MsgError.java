package com.game.core.msg;

import com.game.core.Error;

/**
 * 消息错误
 * 
 * @author haijiang
 * @since 2012/6/3
 * 
 */
public class MsgError extends Error {
	/** serialVersionUID */
	private static final long serialVersionUID = 8136836021493508981L;

	/**
	 * 类默认构造器
	 * 
	 */
	public MsgError() {
		super();
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * 
	 */
	public MsgError(String msg) {
		super(msg);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param err
	 * 
	 */
	public MsgError(Throwable err) {
		super(err);
	}

	/**
	 * 类参数构造器
	 * 
	 * @param msg
	 * @param err
	 * 
	 */
	public MsgError(String msg, Throwable err) {
		super(msg, err);
	}
}
