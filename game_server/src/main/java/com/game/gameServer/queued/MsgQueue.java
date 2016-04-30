package com.game.gameServer.queued;

import com.game.part.ThreadNamingFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息队列
 *
 * @author hjj2017
 * @since 2016/4/27
 *
 */
public final class MsgQueue {
    /** 单例对象 */
    public static final MsgQueue OBJ = new MsgQueue();

    /** JMS 会话对象 */
    private Session _sessionObj;
    /** 消息生产者 */
    private MessageProducer _msgProducer;
    /** 公共的消息消费者 */
    private MessageConsumer _msgConsumer_PUB;
    /** 私有的消息消费者 */
    private MessageConsumer _msgConsumer_PRI;
    /** 消息编解码器 */
    private QueuedMsgCodec _msgCodec;
    /** 消息发送线程池 */
    private ExecutorService _senderES;

    /**
     * 发送公共消息
     *
     * @param msgObj
     *
     */
    public void sendPublicMsg(AbstractQueuedMsg msgObj) {
        this.sendMsg(msgObj, DestinationPool.DEST_PUBLIC);
    }

    /**
     * 发送消息对象
     *
     * @param queuedMsg
     * @param strToDestination 发送到目标地址
     *
     */
    public void sendMsg(AbstractQueuedMsg queuedMsg, String strToDestination) {
        this._senderES.submit(() -> {
            try {
                // 获取发送到目标地址对象
                Destination toDestination = DestinationPool.OBJ.getDestination(strToDestination);
                // 创建 JMS 消息对象并序列化
                Message toJMSMsg = this._sessionObj.createMessage();
                this._msgCodec.encode(queuedMsg, toJMSMsg);
                // 发送 JMS 消息
                this._msgProducer.send(toDestination, toJMSMsg);
            } catch (Exception ex) {
                throw new QueuedError(ex);
            }
        });
    }

    /**
     * 启动消息队列
     *
     */
    public void startUp() {
        try {
            ConnectionFactory f = new ActiveMQConnectionFactory(QueuedConf.OBJ._bokerUrl);
            Connection conn = f.createConnection();
            conn.start();

            this._sessionObj = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            DestinationPool.OBJ.putSessionObj(this._sessionObj);

            // 创建消息生产者
            this._msgProducer = this._sessionObj.createProducer(null);

            MyMsgListener l = new MyMsgListener();

            // 创建公共消息消费者
            Destination publicDest = DestinationPool.OBJ.getPublicDestination();
            this._msgConsumer_PUB = this._sessionObj.createConsumer(publicDest);
            this._msgConsumer_PUB.setMessageListener(l);

            // 创建私有消息消费者
            Destination privateDest = DestinationPool.OBJ.getPrivateDestination();
            this._msgConsumer_PRI = this._sessionObj.createConsumer(privateDest);
            this._msgConsumer_PRI.setMessageListener(l);

            this._msgCodec = new QueuedMsgCodec();

            this._senderES = Executors.newSingleThreadExecutor(new ThreadNamingFactory("MsgQueue"));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
