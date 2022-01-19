package org.xgame.comm.util;

import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.async.IAsyncOperation;
import org.xgame.comm.async.IContinueWith;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * 下一步
 */
public final class AsyncNextStep {
    /**
     * 绑定 Id
     */
    private final long _bindId;

    /**
     * 异步操作队列
     */
    private final Queue<IAsyncOperation> _asyncOpQueue = new ArrayDeque<>();

    /**
     * 在结束时继续执行
     */
    private IContinueWith _onOver_con;

    /**
     * 结束过程在哪个线程执行
     */
    private Executor _onOver_exec;

    /**
     * 类参数构造器
     *
     * @param bindId 绑定 Id
     */
    public AsyncNextStep(long bindId) {
        _bindId = bindId;
    }

    /**
     * 添加下一步
     *
     * @param op 异步操作
     * @return this 指针
     */
    public AsyncNextStep addNext(IAsyncOperation op) {
        if (null == op) {
            return this;
        }

        _asyncOpQueue.offer(op);
        return this;
    }

    /**
     * 当结束时执行
     *
     * @param con  结束时继续执行
     * @param exec 结束过程在哪个线程执行
     * @return this 指针
     */
    public AsyncNextStep onOver(IContinueWith con, Executor exec) {
        _onOver_con = con;
        _onOver_exec = exec;
        return this;
    }

    /**
     * 执行下一步
     */
    public void doNext() {
        IAsyncOperation asyncOp = _asyncOpQueue.poll();
        IContinueWith con = null;
        Executor exec = null;

        if (null == asyncOp) {
            con = _onOver_con;
            exec = _onOver_exec;
        }

        AsyncOperationProcessor.getInstance().process(
            _bindId, asyncOp, con, exec
        );
    }
}
