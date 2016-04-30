package com.game.gameServer.queued;

import com.game.part.ThreadNamingFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
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
    /** 来自地址 */
    static final String FROM_DESTINATION = "fromDestination";
    /** 消息入口 */
    static final String MSG_ROOT = "msgRoot";
    /** 消息类 */
    static final String MSG_CLAZZ = "msgClazz";
    /** 消息体 */
    static final String MSG_BODY = "msgBody";

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
    /** 线程服务 */
    private ExecutorService _ES;

    public void sendPublicMsg(AbstractQueuedMsg msgObj) {
        this.sendMsg(msgObj, DestinationPool.DEST_PUBLIC);
    }

    /**
     * 发送消息对象
     *
     * @param objMsg
     * @param strDest 地址字符串
     *
     */
    public void sendMsg(AbstractQueuedMsg objMsg, String strDest) {
        this._ES.submit(() -> {
            try {
                Message newMsg = this._sessionObj.createTextMessage(MSG_ROOT);
                newMsg.setStringProperty(FROM_DESTINATION, QueuedConf.OBJ._privateDestination);
                newMsg.setStringProperty(MSG_CLAZZ, objMsg.getClass().getName());
                newMsg.setStringProperty(MSG_BODY, objMsg.toString());

                Destination dest = DestinationPool.OBJ.getDestination(strDest);
                this._msgProducer.send(dest, newMsg);
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

            this._ES = Executors.newSingleThreadExecutor(new ThreadNamingFactory("MsgQueue"));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
