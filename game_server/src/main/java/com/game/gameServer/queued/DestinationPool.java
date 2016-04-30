package com.game.gameServer.queued;

import javax.jms.Destination;
import javax.jms.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地址池
 *
 * @author hjj2017
 * @since 2016/04/30
 *
 */
final class DestinationPool {
    /** 公共地址 */
    public static final String DEST_PUBLIC = "public";
    /** 单例对象 */
    static final DestinationPool OBJ = new DestinationPool();

    /** 地址字典 */
    private Map<String, Destination> _destMap = new ConcurrentHashMap<>();
    /** JMS 会话对象 */
    private Session _sessionObj;

    /**
     * 类默认构造器
     *
     */
    private DestinationPool() {
    }

    /**
     * 设置 JMS 会话对象
     *
     * @param sessionObj
     */
    public void putSessionObj(Session sessionObj) {
        this._sessionObj = sessionObj;
    }

    /**
     * 获取公共地址
     *
     * @return
     */
    Destination getPublicDestination() {
        return this.getDestination(DEST_PUBLIC);
    }

    /**
     * 获取私有地址
     *
     * @return
     */
    Destination getPrivateDestination() {
        return this.getDestination(QueuedConf.OBJ._privateDestination);
    }

    /**
     * 根据地址字符串获取地址
     *
     * @param strDestination
     * @return
     */
    Destination getDestination(String strDestination) {
        if (strDestination == null ||
            strDestination.isEmpty()) {
            strDestination = DEST_PUBLIC;
        }

        Destination destObj = this._destMap.get(strDestination);

        if (destObj == null) {
            try {
                destObj = this._sessionObj.createTopic(strDestination);
                this._destMap.put(strDestination, destObj);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return destObj;
    }
}
