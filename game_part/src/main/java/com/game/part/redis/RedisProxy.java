package com.game.part.redis;

import com.game.part.util.ArrayUtil;
import com.game.part.util.MapUtil;
import com.game.part.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Redis 代理服务
 *
 * @author hjj2017
 * @since 2016/6/23
 */
public final class RedisProxy {
    /**
     * 单例对象
     */
    static public final RedisProxy OBJ = new RedisProxy();

    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(RedisProxy.class);

    /**
     * Redis IP 地址
     */
    public String _redisIpAddr = "127.0.0.1";

    /**
     * Redis 端口号
     */
    public int _redisPort = 6379;

    /**
     * Redis 密码
     */
    public String _redisPassword = null;

    /**
     * 订阅频道
     */
    public String[] _subscribeChannelArr = null;

    /**
     * 私有频道
     */
    public String _privateChannel;

    /**
     * 断线重连检测时间, 单位 : 毫秒
     */
    public long _reconnectTestTime = 2000L;

    /**
     * 开启订阅模式
     */
    public boolean _enableSubscribe = false;

    /**
     * 命令接收者
     */
    public ICmdAcceptor _cmdAcceptor = null;

    /**
     * Redis 连接池
     */
    JedisPool _redisPool = null;
    /**
     * 命令编解码器
     */
    final SubscribeCmdCodec _codec = new SubscribeCmdCodec();
    /**
     * 是否正在运行?
     */
    private final AtomicBoolean _running = new AtomicBoolean(false);

    /**
     * 类默认构造器
     */
    private RedisProxy() {
    }

    /**
     * 启动 Redis 代理
     */
    public void startUp() {
        // 创建 Redis 连接池
        this._redisPool = new JedisPool(new JedisPoolConfig(), this._redisIpAddr, this._redisPort);

        if (this._enableSubscribe) {
            // 开始订阅
            new ReconnectAndSubscribeWatcher(this._reconnectTestTime).startUp();
        }

        // 正在运行中...
        this._running.set(true);
    }

    /**
     * 设置字符串键值
     *
     * @param key 关键字
     * @param val 字符串值
     */
    public void set(String key, String val) {
        if (key == null ||
            key.isEmpty()) {
            // 如果关键字为空,
            // 则直接退出!
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        // 获取 Redis 对象
        try (Jedis redisObj = this.getResource()) {
            if (val == null) {
                // 如果字符串值为空,
                // 则执行删除操作
                redisObj.del(key);
            } else {
                // 设置字符串键值
                redisObj.set(key, val);
            }
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 获取 Redis 实例
     *
     * @return
     */
    Jedis getResource() {
        if (this._redisPool == null) {
            return null;
        }

        // 创建 Redis 实例
        Jedis redisObj = this._redisPool.getResource();

        if (StringUtil.notNullOrEmpty(this._redisPassword)) {
            // 如果 Redis 有密码,
            // 那么先授权!
            redisObj.auth(this._redisPassword);
        }

        return redisObj;
    }

    /**
     * 设置字符串键值
     *
     * @param key 关键字
     * @param val 字符串值
     */
    public void setex(RedisKey key, String val) {
        if (null == key) {
            return;
        } else {
            this.setex(key._redisKey, key._ttl, val);
        }
    }

    /**
     * 设置字符串键值
     *
     * @param key 关键字
     * @param ttl 存活时间, 单位 : 秒
     * @param val 字符串值
     */
    public void setex(String key, int ttl, String val) {
        if (key == null ||
            key.isEmpty()) {
            // 如果关键字为空,
            // 则直接退出!
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        // 获取 Redis 对象
        try (Jedis redisObj = this.getResource()) {
            if (val == null) {
                // 如果字符串值为空,
                // 则执行删除操作
                redisObj.del(key);
            } else {
                // 设置字符串键值
                redisObj.setex(key, ttl, val);
            }
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 批量获取值
     *
     * @param keyArr 关键字数组
     * @return 键值字典
     */
    public Map<String, String> mget(RedisKeyArray keyArr) {
        if (null == keyArr ||
            keyArr.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return this.mget(keyArr._redisKeyArray);
        }
    }

    /**
     * 批量获取值
     *
     * @param keyArr 关键字数组
     * @return 键值字典
     */
    public Map<String, String> mget(String... keyArr) {
        if (null == keyArr ||
            keyArr.length <= 0) {
            return Collections.emptyMap();
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return Collections.emptyMap();
        }

        try (Jedis redisObj = this.getResource()) {
            // 获取值列表
            List<String> valList = redisObj.mget(keyArr);
            // 创建结果字典
            Map<String, String> resultMap = new HashMap<>(valList.size());

            for (int i = 0; i < keyArr.length; i++) {
                if (null == keyArr[i] ||
                    null == valList.get(i)) {
                    continue;
                }

                resultMap.put(keyArr[i], valList.get(i));
            }

            return resultMap;
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
            return Collections.emptyMap();
        }
    }

    /**
     * 批量设置键值对
     *
     * @param keyAndValArr 键值对数组
     */
    public void mset(String... keyAndValArr) {
        if (keyAndValArr == null ||
            keyAndValArr.length <= 0) {
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        try (Jedis redisObj = this.getResource()) {
            redisObj.mset(keyAndValArr);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 批量设置键值对
     *
     * @param keyArr Redis 键数组
     * @param valMap 字符串值字典
     */
    public void msetex(RedisKeyArray keyArr, Map<String, String> valMap) {
        if (null == keyArr ||
            keyArr.isEmpty() ||
            MapUtil.isNullOrEmpty(valMap)) {
            return;
        } else {
            this.msetex(keyArr._redisKeyArray, keyArr._ttl, valMap);
        }
    }

    /**
     * 批量设置键值对
     *
     * @param keyArr 关键字数组
     * @param ttl    存活时间 ( 单位 : 秒 )
     * @param valMap 字符串值字典
     */
    public void msetex(String[] keyArr, int ttl, Map<String, String> valMap) {
        if (ArrayUtil.isNullOrEmpty(keyArr) ||
            MapUtil.isNullOrEmpty(valMap)) {
            return;
        }

        // 创建临时键值对字典
        Map<String, String> tempMap = new HashMap<>(valMap.size());

        for (String key : keyArr) {
            if (valMap.containsKey(key)) {
                tempMap.put(key, valMap.get(key));
            }
        }

        this.msetex(ttl, tempMap);
    }

    /**
     * 批量设置键值对
     *
     * @param ttl    存活时间 ( 单位 : 秒 )
     * @param valMap 字符串值字典
     */
    public void msetex(int ttl, Map<String, String> valMap) {
        if (MapUtil.isNullOrEmpty(valMap)) {
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        try (Jedis redisObj = this.getResource(); Pipeline pl = redisObj.pipelined()) {
            for (String key : valMap.keySet()) {
                pl.setex(key, ttl, valMap.get(key));
            }

            pl.sync();
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 在指定秒数之后过期
     *
     * @param key
     * @param ttl
     */
    public void expire(String key, int ttl) {
        if (key == null ||
            key.isEmpty()) {
            // 如果关键字为空,
            // 则直接退出!
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        try (Jedis redisObj = this.getResource()) {
            redisObj.expire(key, ttl);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 根据关键字获取字符串值
     *
     * @param key 关键字
     * @return 字符串值
     */
    public String get(RedisKey key) {
        if (null == key ||
            key.isEmpty()) {
            return null;
        } else {
            return this.get(key._redisKey);
        }
    }

    /**
     * 根据关键字获取字符串值
     *
     * @param key 关键字
     * @return 字符串值
     */
    public String get(String key) {
        if (StringUtil.isNullOrEmpty(key)) {
            // 如果关键字为空,
            // 则直接退出!
            return null;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return null;
        }

        try (Jedis redisObj = this.getResource()) {
            return redisObj.get(key);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 根据关键字删除字符串值
     *
     * @param key 关键字
     */
    public void del(RedisKey key) {
        if (null == key ||
            key.isEmpty()) {
            return;
        } else {
            this.del(key._redisKey);
        }
    }

    /**
     * 根据关键字删除字符串值
     *
     * @param key 关键字
     */
    public void del(String key) {
        if (StringUtil.isNullOrEmpty(key)) {
            // 如果关键字为空,
            // 则直接退出!
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        try (Jedis redisObj = this.getResource()) {
            redisObj.del(key);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * 发布消息
     *
     * @param cmdObj
     * @param toChannelArr
     */
    public void publish(AbstractSubscribeCmd cmdObj, String... toChannelArr) {
        if (cmdObj == null ||
            toChannelArr == null ||
            toChannelArr.length <= 0) {
            return;
        }

        if (!this._running.get()) {
            // 如果当前没有运行,
            // 则直接退出!
            return;
        }

        // 获取 Redis 对象
        try (Jedis redisObj = this.getResource()) {
            for (String toChannel : toChannelArr) {
                if (StringUtil.isNullOrEmpty(toChannel)) {
                    continue;
                }

                // 将命令对象编码为字符串
                final String strClazzJson = this._codec.encode(cmdObj);
                // 发布消息到指定频道
                redisObj.publish(toChannel, strClazzJson);
            }
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
