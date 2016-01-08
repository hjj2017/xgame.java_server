package com.game.part.io;

/**
 * IO 工作过程接口
 * 
 * @author haijiang
 *
 */
interface IIoOperProc<TIoOper extends IIoOper> {
    /**
     * 开始工作
     *
     * @param oper
     *
     */
    void execute(TIoOper oper);
}
