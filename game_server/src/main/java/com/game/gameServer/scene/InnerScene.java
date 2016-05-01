package com.game.gameServer.scene;

import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.gameServer.msg.MsgOrigTypeEnum;
import com.game.part.ThreadNamingFactory;
import com.game.part.msg.type.AbstractMsgObj;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认场景
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
class InnerScene {
    /** 执行线程 */
    private static final String THREAD_NAME_EXEC_SERV = InnerScene.class.getSimpleName() + "#execServ";

    /** 执行服务 */
    private ExecutorService _execServ = null;
    /** 计数器 */
    private AtomicInteger _counter = new AtomicInteger();
    /** 场景名称 */
    public final String _name;
    /** 允许的、能处理的消息源类型 */
    public final MsgOrigTypeEnum _allowMsgOrigType;

    /**
     * 类默认构造器
     * 
     * @param name 场景名称
     * @param allowMsgOrigType 允许的消息源类型
     * 
     */
    InnerScene(String name, MsgOrigTypeEnum allowMsgOrigType) {
        // 设置场景名称和消息源类型
        this._name = name;
        this._allowMsgOrigType = allowMsgOrigType;
        // 初始化默认场景
        this.init(name);
    }

    /**
     * 初始化消息字典
     * 
     * @param name
     * 
     */
    private void init(String name) {
        // 创建线程命名工厂
        ThreadNamingFactory nf = new ThreadNamingFactory();
        // 创建执行线程
        nf.putThreadName(MessageFormat.format(
            "{0}::{1}", 
            THREAD_NAME_EXEC_SERV, 
            name
        ));
        this._execServ = Executors.newSingleThreadExecutor(nf);
    }

    /**
     * 执行消息,
     * 注意: 这里不是立即执行消息, 而是将消息提交到线程池中
     *
     * @param msgObj
     *
     */
    final void execMsg(AbstractMsgObj msgObj) {
         if (msgObj == null ||
            (msgObj instanceof AbstractExecutableMsgObj) == false) {
            // 如果不是可执行消息,
            // 则直接退出!
            SceneLog.LOG.error("不能处理非可执行消息");
            return;
        }

        // 强制转型为可执行消息对象
        final AbstractExecutableMsgObj execMsgObj = (AbstractExecutableMsgObj)msgObj;

        // 创建 Runner 提交到线程池
        ExecutableMsgRunner runner = new ExecutableMsgRunner(
            execMsgObj, this._name, this._counter
        );

        if (runner.canRun()) {
            // 提交到线程池
            this._execServ.submit(runner);
        }
    }

    /**
     * 调用心跳过程
     *
     * @param hb
     */
    void callHeartbeat(IHeartbeat hb) {
        if (hb == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 执行心跳过程
        hb.doHeartbeat();
    }
}
