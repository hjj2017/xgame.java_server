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
    /** BOKER URL */
    private static final String BROKER_URL = "tcp://localhost:61616";

    /** 单例对象 */
    public static final MsgQueue OBJ = new MsgQueue();

    private Session _session;
    /** 消息生产者 */
    private MessageProducer _msgProducer;
    /** 消息消费者 */
    private MessageConsumer _msgConsumer;
    /** 线程服务 */
    private ExecutorService _ES;

    /**
     * 发送消息对象
     *
     * @param obj
     *
     */
    public void sendMsg(Object obj) {
        this._ES.submit(() -> {
            try {
                Message newMsg = this._session.createTextMessage("hello");
                this._msgProducer.send(newMsg);
            } catch (Exception ex) {

            }
        });
    }

    /**
     * 启动消息队列
     *
     */
    public void startUp() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("hello");

            this._msgProducer = session.createProducer(destination);
            this._msgConsumer = session.createConsumer(destination);

            this._msgConsumer.setMessageListener((msgObj) -> {
                try {
                    System.out.println("receive : " + msgObj.getIntProperty("key"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            this._ES = Executors.newSingleThreadExecutor(new ThreadNamingFactory("MsgQueue"));
            this._session = session;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
