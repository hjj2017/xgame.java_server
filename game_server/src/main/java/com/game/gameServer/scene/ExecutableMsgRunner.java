package com.game.gameServer.scene;

import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.gameServer.msg.netty.IoSessionManager;
import com.game.part.util.Assert;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可执行消息运行器
 *
 * @author hjj2019
 * @since 2016/05/01
 */
class ExecutableMsgRunner implements Runnable {
    /** 可执行消息 */
    private AbstractExecutableMsgObj _execMsgObj;
    /** 场景名称 */
    private String _sceneName;
    /** 计数器 */
    private AtomicInteger _counter;

    /**
     * 类参数构造器
     *
     * @param execMsgObj
     * @param sceneName
     * @param counter
     */
    public ExecutableMsgRunner(
        AbstractExecutableMsgObj execMsgObj, String sceneName, AtomicInteger counter) {
        // 断言参数不为空
        Assert.notNull(execMsgObj, "execMsgObj is null");
        Assert.notNullOrEmpty(sceneName, "sceneName is null");
        Assert.notNull(counter, "counter is null");

        this._execMsgObj = execMsgObj;
        this._sceneName = sceneName;
        this._counter = counter;
    }

    /**
     * 是否可以运行?
     *
     * @return
     */
    boolean canRun() {
        if (this._execMsgObj instanceof AbstractCGMsgObj) {
            if (this.isPlayerAllowedCGMsg((AbstractCGMsgObj)this._execMsgObj) == false) {
                // 如果是 CG 消息,
                // 且如果不允许执行 CG 消息,
                // 则直接退出!
                SceneLog.LOG.error(MessageFormat.format(
                    "不能执行消息 {0}, 玩家不允许执行 {1} 类型的消息! " +
                    "具体请参见 com.game.gameServer.framework.Player 类 _allowMsgTypeMap 属性",
                    this._execMsgObj.getClass().getName(),
                    this._execMsgObj.getMsgType()
                ));
                return false;
            }
        }

        // 令计数器 +1
        this._counter.incrementAndGet();
        return true;
    }

    @Override
    public void run() {
        try {
            if (this._execMsgObj instanceof AbstractCGMsgObj) {
                if (this.isPlayerAllowedCGMsg((AbstractCGMsgObj)this._execMsgObj) == false) {
                    // 如果是 CG 消息,
                    // 且如果不允许执行 CG 消息,
                    // 则直接退出!
                    SceneLog.LOG.error(MessageFormat.format(
                        "二次验证时不能执行消息 {0}, 玩家不允许执行 {1} 类型的消息",
                        this._execMsgObj.getClass().getName(),
                        this._execMsgObj.getMsgType()
                    ));
                    return;
                    // 注意: 在这里是二次验证!
                    // 因为消息对象本身在提交到线程池的时候,
                    // 其 allow = true,
                    // 但是到了执行期,
                    // 由于 Player 对象可能已经发生变化,
                    // 导致 allow = false,
                    // 最常见的情况就是玩家突然断线... 所以在执行消息之前,
                    // 还需要再作一次验证
                    //
                }
            }

            // 执行消息
            this._execMsgObj.exec();
        } catch (Exception ex) {
            // 记录错误日志
            SceneLog.LOG.error(MessageFormat.format(
                "场景 {0} 抛出异常 {1}",
                this._sceneName, ex.getMessage()
            ), ex);
        } finally {
            // 令计数器 -1
            this._counter.decrementAndGet();
        }
    }

    /**
     * 是不是玩家允许的 CG 消息?
     *
     * @param cgMsgObj
     * @return
     * @see MsgTypeEnum
     * @see Player
     *
     */
    private boolean isPlayerAllowedCGMsg(AbstractCGMsgObj cgMsgObj) {
        if (cgMsgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return false;
        }

        if (cgMsgObj.getMsgType() == MsgTypeEnum.logout) {
            // 登出消息任何时候都是允许的!
            return true;
        }

        // 获取会话 UId
        final long sessionUId = cgMsgObj.getSelfHandler()._sessionUId;
        // 获取玩家对象
        Player p = IoSessionManager.OBJ.getPlayerBySessionUId(sessionUId);

        if (p == null) {
            // 如果玩家对象为空,
            // 则看看消息是不是登陆消息?
            // 也就是说:
            // 当玩家对象为空时,
            // 还是允许处理登陆消息的!
            return cgMsgObj.getMsgType() == MsgTypeEnum.login;
        }

        // 看看玩家目前是否允许执行该类型的消息?
        Boolean objBool = p._allowMsgTypeMap.get(cgMsgObj.getMsgType());

        if (objBool == null) {
            // 如果值为空,
            // 则默认为不允许!
            return false;
        } else {
            // 返回玩家身上的值
            return objBool.booleanValue();
        }
    }
}
