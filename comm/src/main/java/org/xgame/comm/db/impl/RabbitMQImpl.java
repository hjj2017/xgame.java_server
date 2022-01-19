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
        if (null == joConfig ||
            !joConfig.containsKey("rabbitMQConf")) {
            return;
        }

        final JSONObject joRabbitMQConf = joConfig.getJSONObject("rabbitMQConf");
        String uri = joRabbitMQConf.getString("uri");

        try {
            ConnectionFactory f = new ConnectionFactory();
            f.setUri(uri);

            Connection conn = f.newConnection();
            _rabbitClientCh = conn.createChannel();

            // 初始化 RPC 请求队列数组
            JSONArray joRpcRequestQueueArray = joRabbitMQConf.getJSONArray("rpcRequestQueueArray");
            _rpcRequestQueueArray = new String[joRpcRequestQueueArray.size()];

            for (int i = 0; i < _rpcRequestQueueArray.length; i++) {
                _rpcRequestQueueArray[i] = joRpcRequestQueueArray.getString(i);
            }

            // 初始化 RPC 回复队列
            _rpcResponseQueue = joConfig.getString("rpcResponseQueue");
            _rabbitClientCh.queueDeclare(
                _rpcResponseQueue, true, true, false, null
            );

            LOGGER.info(
                "连接到 RabbitMQ, serverAddr ={}:{}",
                f.getHost(),
                f.getPort()
            );
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void execQueryAsync(
        Class<?> dbFarmerClazz, long bindId, String queryId, JSONObject joParam, Function<Boolean, Void> callback) {
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
                encodeMsg(dbFarmerClazz, queryId, joParam)
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

    /**
     * 获取 RPC 请求队列
     *
     * @param bindId 绑定 Id
     * @return RPC 请求队列
     */
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

    /**
     * 编码消息
     *
     * @param dbFarmerClazz 数据库农民类
     * @param queryId       查询 Id
     * @param joParam       JSON 参数
     * @return 编码后的消息字节数组
     */
    private byte[] encodeMsg(Class<?> dbFarmerClazz, String queryId, JSON joParam) {
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
