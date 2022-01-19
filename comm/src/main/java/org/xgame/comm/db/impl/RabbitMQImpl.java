package org.xgame.comm.db.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.db.IQuerySystem;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * 消息队列实现
 */
public class RabbitMQImpl implements IQuerySystem {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 停机标志
     */
    private final AtomicBoolean _shutdownFlag = new AtomicBoolean(false);

    /**
     * 消息队列频道
     */
    private Channel _rabbitClientCh;

    /**
     * RPC 请求队列数组
     */
    private String[] _rpcRequestQueueArray;

    /**
     * RPC 响应队列
     */
    private String _rpcResponseQueue;

    @Override
    public void init(JSONObject joConfig) {
        if (null == joConfig) {
            return;
        }

        joConfig = joConfig.getJSONObject("dbAgent");
        joConfig = joConfig.getJSONObject("rabbitMQConf");
        String uri = joConfig.getString("uri");

        try {
            ConnectionFactory f = new ConnectionFactory();
            f.setUri(uri);

            Connection conn = f.newConnection();
            _rabbitClientCh = conn.createChannel();

            // 初始化 RPC 请求队列数组
            JSONArray joRpcRequestQueueArray = joConfig.getJSONArray("rpcRequestQueueArray");
            _rpcRequestQueueArray = new String[joRpcRequestQueueArray.size()];

            for (int i = 0; i < _rpcRequestQueueArray.length; i++) {
                _rpcRequestQueueArray[i] = joRpcRequestQueueArray.getString(i);
            }

            // 初始化 RPC 回复队列
            _rpcResponseQueue = "rpcResponseQueue";
            _rabbitClientCh.queueDeclare(_rpcResponseQueue, true, true, false, null);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void execQueryAsync(Class<?> dbFarmerClazz, long bindId, String queryId, JSON joParam, Function<Boolean, Void> callback) {
        if (null == dbFarmerClazz ||
            null == queryId ||
            queryId.isEmpty()) {
            return;
        }

        if (_shutdownFlag.get()) {
            // 如果已经停机,
            return;
        }

        try {
            // 创建关联 Id
            final String corrId = UUID.randomUUID().toString();

            _rabbitClientCh.basicPublish(
                "", getRpcRequestQueue(bindId),
                createAMQPBasicPropz(corrId),
                getMsg(dbFarmerClazz, queryId, joParam)
            );

            _rabbitClientCh.basicConsume(_rpcResponseQueue, true, (consumerTag, delivery) -> {
                if (null == consumerTag ||
                    null == delivery ||
                    null == delivery.getProperties()) {
                    return;
                }

                if (!corrId.equals(delivery.getProperties().getCorrelationId())) {
                    return;
                }

                _rabbitClientCh.basicCancel(consumerTag);

                // 查询完成之后, 再次扔回到异步 IO 线程
                AsyncOperationProcessor.getInstance().process(
                    bindId,
                    () -> callback.apply(true)
                );
            }, (consumerTag) -> {
            });
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private String getRpcRequestQueue(long bindId) {
        if (null == _rpcRequestQueueArray ||
            _rpcRequestQueueArray.length <= 0) {
            return null;
        }

        // 获取 RPC 请求队列索引
        int index = (int) bindId % _rpcRequestQueueArray.length;
        index = Math.abs(index);

        return _rpcRequestQueueArray[index];
    }

    /**
     * 创建属性字典
     *
     * @param corrId 关联 Id
     * @return 属性字典
     */
    private AMQP.BasicProperties createAMQPBasicPropz(String corrId) {
        return new AMQP.BasicProperties.Builder()
            .correlationId(corrId)
            .replyTo(_rpcResponseQueue)
            .build();
    }

    private byte[] getMsg(Class<?> dbFarmerClazz, String queryId, JSON joParam) {
        if (null == dbFarmerClazz ||
            null == queryId ||
            null == joParam) {
            return new byte[0];
        }

        JSONObject joMsg = new JSONObject();
        joMsg.put("dbFarmerClazz", dbFarmerClazz.getName());
        joMsg.put("queryId", queryId);
        joMsg.put("joParam", joParam);

        return joMsg.toJSONString().getBytes(StandardCharsets.UTF_8);
    }
}
