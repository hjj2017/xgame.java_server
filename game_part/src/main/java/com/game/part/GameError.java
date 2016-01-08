package com.game.part;

/**
 * 游戏异常
 * 
 * @author haijiang
 * @since 2012/6/3
 *
 */
public class GameError extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = -1001L;

    /**
     * 类默认构造器
     *
     */
    public GameError() {
        super();
    }

    /**
     * 类参数构造器
     *
     * @param msg
     *
     */
    public GameError(String msg) {
        super(msg);
    }

    /**
     * 类参数构造器
     *
     * @param err
     *
     */
    public GameError(Throwable err) {
        super(err);
    }

    /**
     * 类参数构造器
     *
     * @param msg
     * @param err
     *
     */
    public GameError(String msg, Throwable err) {
        super(msg, err);
    }
}
