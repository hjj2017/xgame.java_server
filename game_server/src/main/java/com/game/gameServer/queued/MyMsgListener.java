package com.game.gameServer.queued;

import com.game.part.util.Out;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 自定义消息监听器
 *
 * @author hjj2017
 * @since 2016/04/30
 */
class MyMsgListener implements MessageListener {
    /** 消息编解码器 */
    private QueuedMsgCodec _msgCodec;
    
    @Override
    public void onMessage(Message fromJMSMsg) {
        try {
            Out<AbstractQueuedMsg> out_queuedMsg = new Out<>();
            this._msgCodec.decode(fromJMSMsg, out_queuedMsg);

            AbstractQueuedMsg queuedMsg = out_queuedMsg.getVal();

            if (queuedMsg == null) {
                return;
            }
        } catch (Exception ex) {
            throw new QueuedError(ex);
        }
    }
}
