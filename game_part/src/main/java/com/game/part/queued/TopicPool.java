package com.game.part.queued;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 主题池
 *
 * @author hjj2017
 * @since 2016/04/30
 *
 */
final class TopicPool {
    /** 单例对象 */
    static final TopicPool OBJ = new TopicPool();

    /** 公共主题 */
    private static final String PUBLIC_TOPIC = "public";

    /** 私有地址 */
    String _privateDestination;
    /** JMS 会话对象 */
    Session _sessionObj;

    /** 主题字典 */
    private Map<String, Topic> _topicMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     *
     */
    private TopicPool() {
    }

    /**
     * 获取公共主题
     *
     * @return
     */
    Topic getPublicTopic() {
        return this.getTopic(PUBLIC_TOPIC);
    }

    /**
     * 获取私有主题
     *
     * @return
     */
    Topic getPrivateTopic() {
        if (this._privateDestination == null ||
            this._privateDestination.isEmpty()) {
            return null;
        } else {
            return this.getTopic(this._privateDestination);
        }
    }

    /**
     * 根据主题字符串获取主题对象
     *
     * @param topicName
     * @return
     */
    Topic getTopic(String topicName) {
        if (topicName == null ||
            topicName.isEmpty()) {
            // 默认为公共地址
            topicName = PUBLIC_TOPIC;
        }

        // 获取地址对象
        Topic objTopic = this._topicMap.get(topicName);

        if (objTopic == null) {
            try {
                // 如果地址对象为空, 则新建
                objTopic = this._sessionObj.createTopic(topicName);
                this._topicMap.put(topicName, objTopic);
            } catch (JMSException ex) {
                // 记录错误日志并向外抛出异常
                QueuedLog.LOG.error(ex.getMessage(), ex);
            }
        }

        return objTopic;
    }
}
