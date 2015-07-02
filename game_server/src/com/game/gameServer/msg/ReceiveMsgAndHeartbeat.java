package com.game.gameServer.msg;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.game.gameServer.scene.SceneFacade;
import com.game.part.BadArgError;
import com.game.part.msg.IMsgReceiver;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 接收消息并执行心跳
 *
 * @author 2019
 * @since 2015/7/2
 *
 */
public class ReceiveMsgAndHeartbeat implements IMsgReceiver {
    /** 单例对象 */
    public static final ReceiveMsgAndHeartbeat OBJ = new ReceiveMsgAndHeartbeat();
    /** 心跳周期, 单位毫秒 */
    private static final int HEARTBEAT_PERIOD = 200;

    /** 消息队列 */
    private final ConcurrentLinkedQueue<AbstractMsgObj> _msgQ = new ConcurrentLinkedQueue<>();
    /** 提交服务 */
    private ScheduledExecutorService _postES = null;

    /**
     * 类默认构造器
     *
     */
    private ReceiveMsgAndHeartbeat() {
    }

    /**
     * 启动心跳
     *
     */
    public void startUp() {
        // 创建提交线程
        this._postES = Executors.newSingleThreadScheduledExecutor();
        this._postES.scheduleWithFixedDelay(() -> {
            while (this._msgQ.size() > 0) {
                // 令场景处理消息
                SceneFacade.OBJ.handleMsg(this._msgQ.poll());
            }
        }, HEARTBEAT_PERIOD, HEARTBEAT_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    public void receive(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 添加消息对象到队列
        this._msgQ.add(msgObj);
    }
}
