package com.game.part.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 断线重连并且订阅
 */
class ReconnectAndSubscribeWatcher {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(ReconnectAndSubscribeWatcher.class);

    /**
     * 空频道
     */
    static private final String CHANNEL_NULL = "null";

    /**
     * 线程名称
     */
    static private final String THREAD_NAME = "game.redis.reconnectAndSubscribeWatcher";

    /**
     * 单线程的线程池
     */
    private ExecutorService _es = null;

    /**
     * 断线重连检测时间, 单位 : 毫秒
     */
    private long _reconnectTestTime = -1L;

    /**
     * 类参数构造器
     *
     * @param reconnectTestTime 断线重连检测时间
     */
    ReconnectAndSubscribeWatcher(long reconnectTestTime) {
        this._reconnectTestTime = reconnectTestTime;
    }

    /**
     * 启动
     */
    void startUp() {
        if (RedisProxy.OBJ._redisPool == null) {
            return;
        }

        this._es = Executors.newSingleThreadExecutor(r -> new Thread(r, THREAD_NAME));
        this._es.submit(() -> {
            while (true) {
                this.reconnectAndSubscribe();

                try {
                    // 两秒后断线重连
                    Thread.sleep(this._reconnectTestTime);
                } catch (Exception ex) {
                    // 记录错误日志
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        });
    }

    /**
     * 断线重连且订阅
     */
    private void reconnectAndSubscribe() {
        // 获取订阅频道
        String[] channelArr = RedisProxy.OBJ._subscribeChannelArr;

        if (channelArr == null ||
            channelArr.length <= 0) {
            // 至少要订阅一个频道
            channelArr = new String[] {
                CHANNEL_NULL
            };
        }

        try (Jedis redisObj = RedisProxy.OBJ.getResource()) {
            // 订阅指定频道中的内容
            redisObj.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String strMsg) {
                    // 记录日志信息
                    LOGGER.debug(MessageFormat.format(
                        "channel = {0}, strMsg = {1}",
                        channel,
                        strMsg
                    ));

                    if (RedisProxy.OBJ._cmdAcceptor == null) {
                        // 如果消息执行调用者为空,
                        // 则直接退出!
                        LOGGER.warn("null cmdAcceptor");
                        return;
                    }

                    // 将消息字符串解码为命令对象
                    AbstractSubscribeCmd cmdObj = RedisProxy.OBJ._codec.decode(strMsg);

                    if (cmdObj == null) {
                        // 如果命令对象为空,
                        // 则直接退出!
                        LOGGER.error("null cmdObj");
                        return;
                    }

                    RedisProxy.OBJ._cmdAcceptor.accept(cmdObj);
                }
            }, channelArr);
        } catch (JedisConnectionException ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
