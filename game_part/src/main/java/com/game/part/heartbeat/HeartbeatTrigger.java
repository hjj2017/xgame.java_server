package com.game.part.heartbeat;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 心跳触发器
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public final class HeartbeatTrigger {
    /** 单例对象 */
    public static final HeartbeatTrigger OBJ = new HeartbeatTrigger();

    /** 心跳周期, 单位毫秒 */
    private static final int HEARTBEAT_PERIOD = 200;

    /** 心跳类型数组 */
    public IHeartbeatType[] _heartbeatTypeArr = null;

    /** 心跳队列 ( 长期的 ) */
    private final Queue<IHeartbeatPoint> _hbPointQ_L = new ConcurrentLinkedQueue<>();
    /** 心跳队列 ( 临时的 ) */
    private final Queue<IHeartbeatPoint> _hbPointQ_T = new ConcurrentLinkedQueue<>();
    /** 提交服务 */
    private ScheduledExecutorService _postES = null;
    /** 执行线字典 */
    private Map<IHeartbeatType, ExecLine> _execLineMap;

    /**
     * 类默认构造器
     *
     */
    private HeartbeatTrigger() {
    }

    /**
     * 启动心跳
     *
     */
    public void startUp() {
        // 创建执行线字典
        this._execLineMap = new ConcurrentHashMap<>();

        for (IHeartbeatType hbType : this._heartbeatTypeArr) {
            // 添加执行线到字典
            this._execLineMap.put(
                hbType, new ExecLine(hbType)
            );
        }

        // 创建心跳运行器
        final Runnable hbRunner = new HeartbeatRunner(
            this._hbPointQ_L,
            this._hbPointQ_T,
            this._execLineMap
        );

        // 创建线程池并提交线程
        this._postES = Executors.newSingleThreadScheduledExecutor();
        this._postES.scheduleWithFixedDelay(
            hbRunner,
            HEARTBEAT_PERIOD,
            HEARTBEAT_PERIOD,
            TimeUnit.MILLISECONDS
        );
    }

    /**
     * 知晓心跳点
     *
     * @param hbPoint
     */
    public void known(IHeartbeatPoint hbPoint) {
        if (hbPoint == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        if (hbPoint.isForever()) {
            // 添加到长期队列
            this._hbPointQ_L.offer(hbPoint);
        } else {
            // 添加到临时队列
            this._hbPointQ_T.offer(hbPoint);
        }
    }
}
