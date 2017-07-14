package com.game.part.redis;

/**
 * 抽象的订阅命令
 */
public abstract class AbstractSubscribeCmd {
    /**
     * 来自哪个频道
     */
    public String _fromChannel;

    /**
     * 获取处理器
     *
     * @param <CMD> 命令类型
     * @return 命令对象处理器
     */
    abstract public <CMD extends AbstractSubscribeCmd> ICmdHandler<CMD> getHandler();

    /**
     * 命令对象处理接口
     *
     * @param <CMD> 命令类型
     */
    static public interface ICmdHandler<CMD extends AbstractSubscribeCmd> {
        /**
         * 处理命令对象
         *
         * @param cmdObj 命令对象
         */
        void handle(CMD cmdObj);
    }
}
