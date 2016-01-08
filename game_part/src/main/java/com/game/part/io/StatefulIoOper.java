package com.game.part.io;

import java.text.MessageFormat;

import com.game.part.util.Assert;

/**
 * 有状态的异步操作,
 * 该类的目的是将一个有无状态的异步操作包装成一个有状态的操作!
 * 注意: 该类是在包内部使用的!
 * 也就是说将所有状态进行封装避免对外暴露...
 * 而外界操作者,
 * 只知道通过 bool 值来告诉框架是否继续向下执行, 有效避免了状态回溯和混乱
 * 
 * @author haijiang
 * 
 */
class StatefulIoOper implements IIoOper {
    /** 异步操作 */
    private IIoOper _innerOper = null;
    /** 当前状态 */
    private IoOperStateEnum _currState = null;

    /**
     * 类参数构造器
     *
     * @param oper
     *
     */
    public StatefulIoOper(IIoOper oper) {
        Assert.notNull(oper);
        this._innerOper = oper;
    }

    /**
     * 获取内嵌工作
     *
     * @return
     */
    public IIoOper getInnerOper() {
        return this._innerOper;
    }

    /**
     * 获取当前状态
     *
     * @return
     */
    public IoOperStateEnum getCurrState() {
        return this._currState;
    }

    /**
     * 设置当前状态
     *
     * @param value
     */
    private void setCurrState(IoOperStateEnum value) {
        if (value == null) {
            return;
        }

        this._currState = value;
    }

    @Override
    public String getThreadKey() {
        return this._innerOper.getThreadKey();
    }

    @Override
    public boolean doInit() {
        // 记录日志信息
        IoOperLog.LOG.debug(MessageFormat.format(
            "StatefulIoOper[key = {0}].doInit",
            this.getThreadKey()
        ));

        // 获取执行结果
        boolean result = this._innerOper.doInit();

        // 如果继续向下执行,
        // 则设置当前状态为: 初始化成功
        this.setCurrState(
            result
            ? IoOperStateEnum.initOk
            : IoOperStateEnum.exit
        );

        return result;
    }

    @Override
    public boolean doIo() {
        // 记录日志信息
        IoOperLog.LOG.debug(MessageFormat.format(
            "StatefulIoOper[key = {0}].doAsyncProc",
            this.getThreadKey()
        ));

        // 获取执行结果
        boolean result = this._innerOper.doIo();

        if (!result) {
            // 记录日志信息
            IoOperLog.LOG.error(MessageFormat.format(
                "{0} 异步操作执行失败",
                this._innerOper.getClass().getSimpleName()
            ));
        }

        // 如果执行成功,
        // 则设置当前状态为: 异步操作完成
        this.setCurrState(
            result
            ? IoOperStateEnum.ioFinishOk
            : IoOperStateEnum.exit
        );

        return result;
    }
}
