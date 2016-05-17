package com.game.part.queued;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 * 公共的消息发送箱
 *
 * @author hjj2019
 * @since 2016/5/17
 *
 */
class PublicOutBox {
    /** 消息队列会话对象 */
    private Session _sessionObj;
    /** 消息生产者 */
    private MessageProducer _msgProducer;

    /**
     *
     *
     * @param leaf
     */
    static void createByLeaf(SessionLeaf leaf) {
    }

    void sendMsg(AbstractQueuedMsg queuedMsg, String strAddr) {
        try {
            Message jmsMSG = this._sessionObj.createTextMessage();
            this._msgProducer.send(null, jmsMSG);
        } catch (Exception ex) {
        }
    }
}
