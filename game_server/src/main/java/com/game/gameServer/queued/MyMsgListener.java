package com.game.gameServer.queued;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 自定义消息监听器
 *
 * @author hjj2017
 * @since 2016/04/30
 */
public class MyMsgListener implements MessageListener {
    @Override
    public void onMessage(Message msgObj) {
        try {
            msgObj.getStringProperty("");
        } catch (Exception ex) {
            throw new QueuedError(ex);
        }
    }
}
