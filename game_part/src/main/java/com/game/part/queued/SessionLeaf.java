package com.game.part.queued;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

/**
 * JMS 会话叶子, 实际上就是对 javax.jms.Session 的一层包装!
 *
 * @author hjj2019
 * @since 2016/5/17
 */
class SessionLeaf {
    /** 地址类型 */
    private AddrTypeEnum _addrType;
    /** JMS 会话对象 */
    private Session _realSession;

    /**
     * 类参数构造器
     *
     * @param addrType
     * @param bokerUrl
     *
     */
    SessionLeaf(AddrTypeEnum addrType, String bokerUrl) {
        this._addrType = addrType;
        this._realSession = createRealSession(bokerUrl);
    }

    /**
     * 创建实际的会话对象
     *
     * @param bokerUrl
     * @return
     *
     */
    private static Session createRealSession(String bokerUrl) {
        try {
            ConnectionFactory f = new ActiveMQConnectionFactory(bokerUrl);
            Connection conn = f.createConnection();
            conn.start();

            // 创建 JMS 会话对象
            return conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception ex) {
            // TODO : throw ex;
        }

        return null;
    }

    AddrTypeEnum getAddrType() {
        return this._addrType;
    }
}
