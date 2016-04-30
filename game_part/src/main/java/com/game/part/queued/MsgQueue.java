package com.game.part.queued;

import com.game.part.ThreadNamingFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    /** 消息消费者字典 */
    private Map<String, MessageConsumer> _msgConsumerMap = new ConcurrentHashMap<>();
    /** 消息编解码器 */
    private QueuedMsgCodec _msgCodec;
    /** 消息发送线程池 */
    private ExecutorService _senderES;

    /**
     * 类默认构造器
     *
     */
    private MsgQueue() {
    }

    /**
     * 发送公共消息
     *
     * @param msgObj
     *
     */
    public void sendPublicMsg(AbstractQueuedMsg msgObj) {
        this.sendMsg(msgObj, TopicPool.PUBLIC_TOPIC);
    }

    /**
     * 发送消息对象
     *
     * @param queuedMsg
     * @param strToDestination 发送到目标地址
     *
     */
    public void sendMsg(AbstractQueuedMsg queuedMsg, final String strToDestination) {
        this._senderES.submit(() -> {
            try {
                // 获取主题对象
                Topic objTopic = TopicPool.OBJ.getTopic(strToDestination);
                // 创建 JMS 消息对象并序列化
                Message toJMSMsg = this._sessionObj.createMessage();
                this._msgCodec.encode(queuedMsg, toJMSMsg);
                // 发送 JMS 消息
                this._msgProducer.send(objTopic, toJMSMsg);
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
            final String bokerUrl = "";
            ConnectionFactory f = new ActiveMQConnectionFactory(bokerUrl);
            Connection conn = f.createConnection();
            conn.start();

            this._sessionObj = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicPool.OBJ._sessionObj = this._sessionObj;

            // 创建消息生产者和消费者
            this._msgProducer = this._sessionObj.createProducer(null);
            this._msgConsumerMap.putAll(this.createConsumerMap());

            this._msgCodec = new QueuedMsgCodec();

            this._senderES = Executors.newSingleThreadExecutor(new ThreadNamingFactory("com.game::MsgQueue"));
        } catch (Exception ex) {
            // 记录错误日志并退出!
            QueuedLog.LOG.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    /**
     * 创建消息消费者字典
     *
     * @return
     * @throws JMSException
     */
    private Map<String, MessageConsumer> createConsumerMap() throws JMSException {
        // 创建主题数组
        final Topic[] listenTopicArr = {
            TopicPool.OBJ.getPrivateTopic(),
            TopicPool.OBJ.getPublicTopic(),
        };

        // 结果字典
        Map<String, MessageConsumer> resultMap = new HashMap<>();
        // 创建监听器
        MyMsgListener listener = new MyMsgListener();

        for (Topic listenTopic : listenTopicArr) {
            // 根据监听主题创建消息消费者
            MessageConsumer msgConsumer = this._sessionObj.createConsumer(listenTopic);
            msgConsumer.setMessageListener(listener);

            // 添加到字典对象
            resultMap.put(
                listenTopic.getTopicName(),
                msgConsumer
            );
        }

        return resultMap;
    }
}
