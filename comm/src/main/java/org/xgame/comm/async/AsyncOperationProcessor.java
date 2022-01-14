package org.xgame.comm.async;

import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.comm.MainThreadProcessor;
import org.xgame.comm.util.SafeRunner;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步操作处理器
 */
public final class AsyncOperationProcessor {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 线程池大小
     */
    private static final int THREAD_POOL_SIZE = 1;

    /**
     * 队列长度
     */
    private static final int Q_SIZE = 4096;

    /**
     * 随机对象
     */
    private static final Random RAND = new Random();

    /**
     * 单例对象
     */
    private static final AsyncOperationProcessor INSTANCE = new AsyncOperationProcessor();

    /**
     * 线程服务数组
     */
    private final ExecutorService[] _esArray;

    /**
     * 主线程执行器,
     * 主线程处理器得代理对象
     */
    private final Executor _mainThreadExecutor = (cmd) -> MainThreadProcessor.getInstance().process(cmd);

    /**
     * 私有化类默认构造器
     */
    private AsyncOperationProcessor() {
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;
        _esArray = new ExecutorService[threadNum];

        for (int i = 0; i < threadNum; i++) {
            // 线程名称
            final String threadName = "xgame_AsyncOperationProcessor[" + i + "]";
            // 创建单线程服务
            _esArray[i] = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(Q_SIZE),
                (r) -> {
                    // 创建线程并起个名字
                    Thread t = new Thread(r);
                    t.setName(threadName);
                    return t;
                },

                (r, exec) -> LOGGER.error("IO 过程被拒绝")
            );
        }
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static AsyncOperationProcessor getInstance() {
        return INSTANCE;
    }

    /**
     * 处理异步操作
     *
     * @param bindId 绑定 ( 线程 ) Id
     * @param op     异步操作对象
     * @param con    同步继续执行
     * @param exec   通过谁来执行 IContinueWith 接口
     */
    public void process(long bindId, IAsyncOperation op, IContinueWith con, Executor exec) {
        if (null == op) {
            return;
        }

        // 根据绑定 Id 获取线程索引
        bindId = Math.abs(bindId);
        int esIndex = (int) bindId % _esArray.length;

        _esArray[esIndex].submit(new SafeRunner(
            () -> {
                // 执行异步操作
                op.doAsync();

                if (null != con) {
                    Objects.requireNonNullElse(exec, _mainThreadExecutor).execute(con::doContinue);
                }
            }
        ));
    }

    /**
     * 处理异步操作
     *
     * @param bindId 绑定 ( 线程 ) Id
     * @param op     异步操作对象
     * @param con    同步继续执行
     */
    public void process(long bindId, IAsyncOperation op, IContinueWith con) {
        if (null == op) {
            return;
        }

        process(bindId, op, con, _mainThreadExecutor);
    }

    /**
     * 处理异步操作
     *
     * @param bindId 绑定 ( 线程 ) Id
     * @param op     异步操作
     */
    public void process(long bindId, IAsyncOperation op) {
        process(bindId, op, null, null);
    }

    /**
     * 处理异步操作
     *
     * @param op  异步操作
     * @param con 同步继续执行
     */
    public void process(IAsyncOperation op, IContinueWith con) {
        if (null == op) {
            return;
        }

        int bindId = RAND.nextInt(_esArray.length);

        process(
            bindId, op, con, _mainThreadExecutor
        );
    }

    /**
     * 处理异步操作
     *
     * @param op 异步操作
     */
    public void process(IAsyncOperation op) {
        process(op, null);
    }
}
