package com.game.bizModule.guid.bizServ;

import com.game.part.GameError;

/**
 * GUId 异常
 *
 * @author hjj2017
 * @since 2015/7/19
 *
 */
public class Guid64Error extends GameError {
    // serialVersionUID
	private static final long serialVersionUID = 3924650360910776442L;

	/**
     * 类默认构造器
     *
     */
    public Guid64Error() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param msg
     *
     */
    public Guid64Error(String msg) {
        super(msg);
    }

    /**
     * 类参数构造器
     *
     * @param err
     *
     */
    public Guid64Error(Throwable err) {
        super(err);
    }

    /**
     * 类参数构造器
     *
     * @param msg
     * @param err
     *
     */
    public Guid64Error(String msg, Throwable err) {
        super(msg, err);
    }
}
