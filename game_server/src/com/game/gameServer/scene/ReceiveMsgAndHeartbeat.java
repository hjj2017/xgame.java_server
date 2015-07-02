package com.game.gameServer.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.game.part.lazySaving.LazySavingHelper;
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
    /** 心跳接口列表 */
    public final List<IHeartbeat> _heartbeatList = new ArrayList<>();

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
        //
        // 创建线程逻辑,
        // 在这里定义每次心跳时需要执行哪些操作
        //
        final Runnable r = () -> {
            // 令每个心跳接口执行一次心跳!
            this._heartbeatList.forEach(hb -> {
                if (hb != null) {
                    hb.doHeartbeat();
                }
            });

            // 处理消息队列
            while (this._msgQ.size() > 0) {
                // 令场景处理消息
                SceneFacade.OBJ.handleMsg(this._msgQ.poll());
            }

            // 执行延迟保存
            LazySavingHelper.OBJ.execUpdateWithInterval();
        };

        // 创建线程池并提交线程
        this._postES = Executors.newSingleThreadScheduledExecutor();
        this._postES.scheduleWithFixedDelay(r,
            HEARTBEAT_PERIOD,
            HEARTBEAT_PERIOD,
            TimeUnit.MILLISECONDS
        );
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
