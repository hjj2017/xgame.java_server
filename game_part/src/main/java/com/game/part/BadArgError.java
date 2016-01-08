package com.game.part;

/**
 * 参数错误
 * 
 * @author haijiang
 *
 */
public class BadArgError extends GameError {
    /** serialVersionUID */
    private static final long serialVersionUID = 7400420633310393852L;

    /**
     * 类默认构造器
     *
     */
    public BadArgError() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param msg
     *
     */
    public BadArgError(String msg) {
        super(msg);
    }

    /**
     * 类参数构造器
     *
     * @param err
     *
     */
    public BadArgError(Throwable err) {
        super(err);
    }

    /**
     * 类参数构造器
     *
     * @param msg
     * @param err
     *
     */
    public BadArgError(String msg, Throwable err) {
        super(msg, err);
    }
}
