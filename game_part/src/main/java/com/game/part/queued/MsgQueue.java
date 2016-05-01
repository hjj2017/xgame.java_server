package com.game.part.queued;

import com.game.part.ThreadNamingFactory;
import com.game.part.util.Assert;
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

    /** 线程名称 */
    private static final String THREAD_NAME = "com.game::MsgQueue";

    /** BokerUrl */
    public String _bokerUrl = null;
    /** 私有地址 */
    public String _privateDestination = null;
    /** 消息执行调用者 */
    public IMsgExeCaller _msgExeCaller = null;

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
        this.sendMsg(msgObj, TopicPool.OBJ.getPublicTopic());
    }

    /**
     * 发送消息对象
     *
     * @param queuedMsg
     * @param strToDestination 发送到目标地址
     *
     */
    public void sendMsg(AbstractQueuedMsg queuedMsg, final String strToDestination) {
        Topic objTopic = TopicPool.OBJ.getTopic(strToDestination);
        this.sendMsg(queuedMsg, objTopic);
    }

    /**
     * 发送消息对象
     *
     * @param queuedMsg
     * @param objTopic
     */
    private void sendMsg(AbstractQueuedMsg queuedMsg, Topic objTopic) {
        // 断言参数不为空
        Assert.notNull(queuedMsg, "queuedMsg is null");
        Assert.notNull(objTopic, "objTopic is null");

        this._senderES.submit(() -> {
            try {
                // 创建 JMS 消息对象并序列化
                Message toJMSMsg = this._sessionObj.createMessage();
                this._msgCodec.encode(queuedMsg, toJMSMsg);
                // 发送 JMS 消息
                this._msgProducer.send(objTopic, toJMSMsg);
            } catch (Exception ex) {
                // 记录错误日志
                QueuedLog.LOG.error(ex.getMessage(), ex);
            }
        });
    }

    /**
     * 启动消息队列
     *
     */
    public void startUp() {
        try {
            ConnectionFactory f = new ActiveMQConnectionFactory(this._bokerUrl);
            Connection conn = f.createConnection();
            conn.start();

            // 创建 JMS 会话对象
            this._sessionObj = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 初始化主题池
            TopicPool.OBJ._sessionObj = this._sessionObj;
            TopicPool.OBJ._privateDestination = this._privateDestination;

            // 创建消息生产者和消费者
            this._msgProducer = this._sessionObj.createProducer(null);
            this._msgConsumerMap.putAll(this.createConsumerMap());

            // 创建解码器
            this._msgCodec = new QueuedMsgCodec();
            // 创建发送线程池
            ThreadNamingFactory tnf = new ThreadNamingFactory(THREAD_NAME);
            this._senderES = Executors.newSingleThreadExecutor(tnf);
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
        MsgDecodeListener listener = new MsgDecodeListener(
            this._msgCodec,
            this._msgExeCaller
        );

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
