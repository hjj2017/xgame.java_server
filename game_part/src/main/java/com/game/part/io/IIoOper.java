package com.game.part.io;

/**
 * 异步操作接口
 * 
 * @author haijiang
 *
 */
public interface IIoOper {
    /**
     * 获取线程关键字,
     * 该关键字决定异步操作在哪一个线程中执行!
     *
     * @return
     *
     */
    String getThreadKey();

    /**
     * 初始化异步操作
     *
     * @return 是否继续向下执行?
     * <ul>
     * <li>true, 继续向下执行 doIo</li>
     * <li>false, 中断执行</li>
     * </ul>
     *
     */
    default boolean doInit() {
        return true;
    }

    /**
     * 执行异步过程, <font color='#990000'>该操作会在异步线程中执行</font>
     *
     * @return 是否执行成功?
     *
     */
    boolean doIo();
}
