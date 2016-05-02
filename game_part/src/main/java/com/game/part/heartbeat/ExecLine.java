package com.game.part.heartbeat;

import com.game.part.ThreadNamingFactory;
import com.game.part.util.Assert;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 执行线
 * 
 * @author hjj2017
 * @since 2014/5/2
 * 
 */
class ExecLine {
    /** 线程名称前缀 */
    private static final String THREAD_NAME_EXEC_SERV = "com.game::Heartbeat.ExecLine";

    /** 执行服务 */
    private ExecutorService _execServ = null;
    /** 计数器 */
    private AtomicInteger _counter = new AtomicInteger();
    /** 心跳类型 */
    public final IHeartbeatType _hbType;

    /**
     * 类默认构造器
     * 
     * @param hbType 心跳类型
     * 
     */
    ExecLine(IHeartbeatType hbType) {
        // 参数对象为空
        Assert.notNull(hbType, "hbType is null");

        this._hbType = hbType;
        // 初始化执行线
        this.init(hbType.getStrVal());
    }

    /**
     * 初始化消息字典
     * 
     * @param hbTypeName
     * 
     */
    private void init(String hbTypeName) {
        // 创建线程命名工厂
        ThreadNamingFactory tnf = new ThreadNamingFactory();
        // 创建执行线程
        tnf.putThreadName(MessageFormat.format(
            "{0}#{1}",
            THREAD_NAME_EXEC_SERV,
            hbTypeName
        ));
        this._execServ = Executors.newSingleThreadExecutor(tnf);
    }

    /**
     * 调用心跳过程,
     * 注意: 这里不是立即执行消息, 而是将消息提交到线程池中
     *
     * @param hbPoint
     *
     */
    final void callHeartbeat(IHeartbeatPoint hbPoint) {
         if (hbPoint == null) {
             // 如果参数对象为空,
             // 则直接退出!
             return;
        }

        // 令计数器 +1
        this._counter.incrementAndGet();

        // 提交到线程池
        this._execServ.submit(() -> {
            try {
                // 执行心跳过程
                hbPoint.doHeartbeat();
            } catch (Exception ex) {
                // 记录错误日志
                HeartbeatLog.LOG.error(ex.getMessage(), ex);
            } finally {
                // 令计数器 -1
                this._counter.decrementAndGet();
            }
        });
    }

    /**
     * 统计当前 '执行线' 正在处理的 '心跳点' 的数量
     *
     * @return
     */
    public int countPoint() {
        return this._counter.get();
    }
}
