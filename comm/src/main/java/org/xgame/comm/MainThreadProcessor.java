package org.xgame.comm;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.xgame.comm.cmdhandler.AbstractCmdHandlerContext;
import org.xgame.comm.cmdhandler.CmdHandlerFactory;
import org.xgame.comm.cmdhandler.ICmdHandler;
import org.xgame.comm.util.SafeRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 住线程处理器
 */
public final class MainThreadProcessor {
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
    private static final int Q_SIZE = 1024;

    /**
     * 单例对象
     */
    private static final MainThreadProcessor INSTANCE = new MainThreadProcessor();

    /**
     * 线程服务
     */
    private final ExecutorService _es;

    /**
     * 私有化类默认构造器
     */
    private MainThreadProcessor() {
        _es = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(Q_SIZE),
            (r) -> {
                // 创建线程并起个名字
                Thread t = new Thread(r);
                t.setName("xgame_MainThreadProcessor");
                return t;
            },

            (r, exec) -> LOGGER.error("主线程处理器拒绝处理")
        );
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static MainThreadProcessor getInstance() {
        return INSTANCE;
    }

    /**
     * 转型命令处理器上下文对象
     *
     * @param ctx    命令处理器上下文对象
     * @param <TCtx> 命令类型
     * @return 转型后的命令处理器上下文对象
     */
    private static <TCtx extends AbstractCmdHandlerContext> TCtx cast_0(Object ctx) {
        @SuppressWarnings("unchecked")
        TCtx tempObj = (TCtx) ctx;
        return tempObj;
    }

    /**
     * 转型消息对象
     *
     * @param msgObj 消息对象
     * @param <TCmd> 命令类型
     * @return 转型后的消息对象
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast_1(Object msgObj) {
        @SuppressWarnings("unchecked")
        TCmd tempObj = (TCmd) msgObj;
        return tempObj;
    }

    /**
     * 处理消息对象
     *
     * @param ctx    命令处理器上下文
     * @param cmdObj 命令对象
     */
    public void process(AbstractCmdHandlerContext ctx, GeneratedMessageV3 cmdObj) {
        if (null == ctx ||
            null == cmdObj) {
            return;
        }

        this.process(() -> {
            // 获取命令类
            final Class<?> cmdClazz = cmdObj.getClass();
            // 创建命令处理器
            final ICmdHandler<?, ?> cmdHandler = CmdHandlerFactory.create(cmdClazz);

            if (null == cmdHandler) {
                LOGGER.error(
                    "未找到命令处理器, cmdClazz = {}",
                    cmdClazz.getName()
                );
                return;
            }

            LOGGER.debug(
                "处理命令, cmdClazz = {}",
                cmdClazz.getName()
            );

            cmdHandler.handle(cast_0(ctx), cast_1(cmdObj));
        });
    }

    /**
     * 处理任务
     *
     * @param task 任务对象
     */
    public void process(Runnable task) {
        if (null != task) {
            _es.submit(new SafeRunner(task));
        }
    }

    /**
     * 停机
     */
    public void shutdown() {
        if (null == _es ||
            _es.isShutdown()) {
            return;
        }

        _es.shutdown();

        try {
            if (!_es.awaitTermination(120, TimeUnit.SECONDS)) {
                LOGGER.error("线程池未停机");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
