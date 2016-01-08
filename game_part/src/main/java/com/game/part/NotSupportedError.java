package com.game.part;

/**
 * 不支持错误
 * 
 * @author Haijiang
 * @since 2012/6/3
 *
 */
public class NotSupportedError extends GameError {
    /** serialVersionUID */
    private static final long serialVersionUID = 3917831565297901055L;

    /**
     * 类默认构造器
     *
     */
    public NotSupportedError() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param msg
     *
     */
    public NotSupportedError(String msg) {
        super(msg);
    }

    /**
     * 类参数构造器
     *
     * @param err
     *
     */
    public NotSupportedError(Throwable err) {
        super(err);
    }

    /**
     * 类参数构造器
     *
     * @param msg
     * @param err
     *
     */
    public NotSupportedError(String msg, Throwable err) {
        super(msg, err);
    }
}
