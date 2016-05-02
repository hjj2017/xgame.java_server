package com.game.part.heartbeat;

import com.game.part.util.Assert;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Queue;

/**
 * 心跳运行者
 *
 * @author hjj2019
 * @since 2016/05/02
 */
class HeartbeatRunner implements Runnable {
    /** 最大处理数量 */
    private static final int MAX_PROCESS = 128;

    /** 心跳队列 ( longTime ) */
    private final Queue<IHeartbeatPoint> _hbPointQ_L;
    /** 心跳队列 ( temp ) */
    private final Queue<IHeartbeatPoint> _hbPointQ_T;
    /** 执行线字典 */
    private final Map<IHeartbeatType, ExecLine> _execLineMap;

    /**
     * 类参数构造器
     *
     * @param hbPointQ_L
     * @param hbPointQ_T
     * @param execLineMap
     */
    HeartbeatRunner(
        Queue<IHeartbeatPoint> hbPointQ_L,
        Queue<IHeartbeatPoint> hbPointQ_T,
        Map<IHeartbeatType, ExecLine> execLineMap) {
        // 断言参数不为空
        Assert.notNull(hbPointQ_L, "hbPointQ_L is null");
        Assert.notNull(hbPointQ_T, "hbPointQ_T is null");
        Assert.notNull(execLineMap, "execLineMap is null");

        this._hbPointQ_L = hbPointQ_L;
        this._hbPointQ_T = hbPointQ_T;
        this._execLineMap = execLineMap;
    }

    @Override
    public void run() {
        // 处理心跳队列 ( 长期的 )
        this._hbPointQ_L.forEach(hbPoint -> this.postToExecLine(hbPoint));

        // 处理心跳队列 ( 短期的 )
        for (int i = 0; i < MAX_PROCESS && !this._hbPointQ_T.isEmpty(); i++) {
            // 获取心跳点
            IHeartbeatPoint hbPoint = this._hbPointQ_T.poll();
            // 令执行线处理心跳点
            this.postToExecLine(hbPoint);
        }
    }

    /**
     * 将 '心跳点' 提交到 '执行线'
     *
     * @param hbPoint
     *
     */
    private void postToExecLine(IHeartbeatPoint hbPoint) {
        if (hbPoint == null) {
            // 如果参数对象为空,
            // 则直接退出!
            HeartbeatLog.LOG.error("参数对象为空");
            return;
        }

        // 获取消息类型
        IHeartbeatType hbType = hbPoint.getHeartbeatType();
        Assert.notNull(hbType, "心跳类型为空");

        // 获取执行线对象
        ExecLine execLine = this._execLineMap.get(hbType);

        if (execLine == null) {
            // 如果执行线对象为空,
            // 则直接退出!
            HeartbeatLog.LOG.error(MessageFormat.format(
                "执行线为空, 心跳类型 = {0}",
                hbType.getStrVal()
            ));
            return;
        }

        // 令 '执行线' 调用 '心跳点' 中的心跳过程
        execLine.callHeartbeat(hbPoint);
    }
}
