package com.game.part.io;

/**
 * 同步的 IO 工作过程
 * 
 * @author haijiang
 *
 */
class SyncIoOperProc implements IIoOperProc<IIoOper> {
    /** 单例对象 */
    static final SyncIoOperProc OBJ = new SyncIoOperProc();

    /**
     * 类默认构造器
     *
     */
    private SyncIoOperProc() {
    }

    @Override
    public void execute(IIoOper oper) {
        if (oper == null) {
            return;
        }

        // 将异步操作包装成一个有状态的对象,
        // 然后带入 invokeDoInit, invokeDoIo 函数中!
        this.nextStep(new StatefulIoOper(oper));
    }

    /**
     * 调用异步操作对象的 doInit 函数
     *
     * @param oper
     */
    private void invokeDoInit(StatefulIoOper oper) {
        if (oper == null) {
            return;
        }

        try {
            // 执行初始化操作并进入下一步!
            oper.doInit();
            this.nextStep(oper);
        } catch (Exception ex) {
            // 记录错误日志
            IoOperLog.LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * 调用异步操作对象的 doIo 函数
     *
     * @param oper
     */
    private void invokeDoIo(StatefulIoOper oper) {
        if (oper == null) {
            return;
        }

        try {
            // 执行 IO 操作
            oper.doIo();
        } catch (Exception ex) {
            // 记录错误日志
            IoOperLog.LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * 执行下异步操作
     *
     * @param oper
     */
    private void nextStep(StatefulIoOper oper) {
        if (oper == null) {
            return;
        }

        // 获取当前工作状态
        IoOperStateEnum currState = oper.getCurrState();

        if (currState == null) {
            this.invokeDoInit(oper);
            return;
        }

        switch (oper.getCurrState()) {
            case exit:
            case ioFinishOk:
                return;

            case initOk:
                // 执行异步过程
                this.invokeDoIo(oper);
                return;

            default:
                return;
        }
    }
}
