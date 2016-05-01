package com.game.part.queued;

import com.game.part.util.Assert;
import com.game.part.util.Out;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 消息解码监听
 *
 * @author hjj2017
 * @since 2016/04/30
 */
class MsgDecodeListener implements MessageListener {
    /** 消息编解码器 */
    private QueuedMsgCodec _msgCodec;
    /** 消息执行调用者 */
    private IMsgExeCaller _msgExeCaller;

    /**
     * 类参数构造器
     *
     * @param msgCodec
     */
    MsgDecodeListener(QueuedMsgCodec msgCodec, IMsgExeCaller msgExeCaller) {
        // 断言参数不为空
        Assert.notNull(msgCodec, "msgCodec is null");
        Assert.notNull(msgCodec, "msgExeCaller is null");

        this._msgCodec = msgCodec;
        this._msgExeCaller = msgExeCaller;
    }

    @Override
    public void onMessage(Message fromJMSMsg) {
        if (fromJMSMsg == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        try {
            // 将 JMS 消息解码成 QueuedMsg
            Out<AbstractQueuedMsg> out_queuedMsg = new Out<>();
            this._msgCodec.decode(
                fromJMSMsg, out_queuedMsg
            );

            // 获取队列消息
            AbstractQueuedMsg queuedMsg = out_queuedMsg.getVal();

            if (queuedMsg != null) {
                this._msgExeCaller.callExec(queuedMsg);
            }
        } catch (Exception ex) {
            // 记录错误日志
            QueuedLog.LOG.error(ex.getMessage(), ex);
        }
    }
}
