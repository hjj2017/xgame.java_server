package com.game.robot.kernal;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.game.robot.RobotLog;

/**
 * 被聚焦的功能模块
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public class FocusModule {
    /** 模块准备工作 */
    AbstractModuleReady _moduleReady = null;
    /** GC 消息处理器字典 */
    final Map<Class<?>, AbstractGCMsgHandler<?>> _gcHandlerMap = new HashMap<>();
    /** 下一个功能模块 */
    private FocusModule _next = null;

    /**
     * 类默认构造器
     * 
     */
    FocusModule() {
    }

    /**
     * 获取下一个功能模块
     * 
     * @return
     * 
     */
    public FocusModule getNext() {
        return this._next;
    }

    /**
     * 设置下一个功能模块
     * 
     * @param value
     * 
     */
    public void setNext(FocusModule value) {
        this._next = value;
    }

    /**
     * 添加 GC 消息处理器
     * 
     * @param value
     * 
     */
    public void addGCMsgHandler(
        AbstractGCMsgHandler<?> value) {
        if (value != null) {
            this._gcHandlerMap.put(value.getClass(), value);
        }
    }

    /**
     * 添加所有的 GC 消息处理器
     * 
     * @param value
     * 
     */
    public void addAllGCMsgHandler(Collection<AbstractGCMsgHandler<?>> value) {
        if (value != null && 
            value.isEmpty() == false) {
            for (AbstractGCMsgHandler<?> elem : value) {
                this.addGCMsgHandler(elem);
            }
        }
    }

    /**
     * 开始测试当前聚焦的模块
     * 
     * @param robotObj
     * @param msgObj
     * 
     */
    public void test(Robot robotObj, Object msgObj) {
        if (robotObj == null) {
            // 如果参数对象为空, 
            // 则直接退出!
            return;
        }

        if (msgObj.equals(ModuleReadyCmd.OBJ)) {
            // 如果是模块准备指令,
            RobotLog.LOG.info(MessageFormat.format(
                "机器人 {0} 准备测试 {1} 模块",
                robotObj._userName,
                this._moduleReady.getClass().getSimpleName()
            ));

            // 则令当前关注的模块做好准备!
            this._moduleReady.ready(robotObj);
            return;
        }

        // 记录消息处理日志
        RobotLog.LOG.info(MessageFormat.format(
            "机器人 {0} 处理 {1} 消息",
            robotObj._userName,
            msgObj.getClass().getSimpleName()
        ));

        // 获取迭代器
        Iterator<AbstractGCMsgHandler<?>> it = this._gcHandlerMap.values().iterator();

        for (; it.hasNext(); ) {
            // 获取 GC 消息处理器
            AbstractGCMsgHandler<?> handler = it.next();

            if (handler != null) {
                // 如果处理器不为空, 
                // 则尝试处理消息...
                handler.handleObj(robotObj, msgObj);
            }
        }
    }
}
