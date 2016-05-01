package com.game.part.queued;

import com.game.part.util.Assert;
import com.game.part.util.Out;
import com.google.gson.Gson;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * 消息编码解码器
 *
 * @author hjj2019
 * @since 2016/4/30
 *
 */
final class QueuedMsgCodec {
    /** 来自地址 */
    static final String FROM_DESTINATION = "_fromDestination";
    /** 消息类 */
    static final String MSG_CLAZZ = "msgClazz";
    /** 消息体 */
    static final String MSG_BODY = "msgBody";

    /**
     * 将 QueuedMsg 编码成 JMS 消息
     *
     * @param fromQueuedMsg
     * @param toJMSMsg
     * @throws JMSException
     */
    void encode(AbstractQueuedMsg fromQueuedMsg, Message toJMSMsg)
        throws JMSException {
        Assert.notNull(fromQueuedMsg, "fromQueuedMsg is null");
        Assert.notNull(toJMSMsg, "toJMSMsg is null");

        // 将私有地址设置为来信地址
        final String privateDestination = "";
        fromQueuedMsg._fromDestination = privateDestination;

        // 将消息序列化为 JSON 对象
        Gson gson = new Gson();
        String strJson = gson.toJson(fromQueuedMsg);

        // 编码到 JMS 消息对象
        toJMSMsg.setStringProperty(FROM_DESTINATION, privateDestination);
        toJMSMsg.setStringProperty(MSG_CLAZZ, fromQueuedMsg.getClass().getName());
        toJMSMsg.setStringProperty(MSG_BODY, strJson);
    }

    /**
     * 将 JMS 消息解码成 QueuedMsg
     *
     * @param fromJMSMsg
     * @param out_toQueuedMsg
     * @throws JMSException
     * @throws ClassNotFoundException
     */
    void decode(Message fromJMSMsg, Out<AbstractQueuedMsg> out_toQueuedMsg)
        throws JMSException, ClassNotFoundException {
        // 断言参数不为空
        Assert.notNull(fromJMSMsg, "fromJMSMsg is null");
        Assert.notNull(out_toQueuedMsg, "out_toQueuedMsg is null");

        // 获取消息的来信地址
        final String fromDestination = fromJMSMsg.getStringProperty(FROM_DESTINATION);
        // 获取消息类和消息体
        String strClazz = fromJMSMsg.getStringProperty(MSG_CLAZZ);
        String strJson = fromJMSMsg.getStringProperty(MSG_BODY);

        // 通过 Gson 反序列化
        Class<?> msgClazz = Class.forName(strClazz);
        Object msgObj = (new Gson()).fromJson(strJson, msgClazz);

        // 添加到输出参数
        AbstractQueuedMsg queuedMsg = (AbstractQueuedMsg)msgObj;
        queuedMsg._fromDestination = fromDestination;
        out_toQueuedMsg.setVal(queuedMsg);
    }
}
