package org.xgame.bizserver.base;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import org.slf4j.Logger;

/**
 * 内部服务器通信消息
 */
public final class InternalServerMsg {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = BaseLog.LOGGER;

    /**
     * 代理服务器 Id
     */
    private String _proxyServerId;

    /**
     * 远程会话 Id, 也就是客户端连接代理服务器的标识 Id
     */
    private int _remoteSessionId = -1;

    /**
     * 来自用户 Id
     */
    private int _fromUserId = -1;

    /**
     * 客户端 IP 地址
     */
    private String _clientIP = null;

    /**
     * 消息编码
     */
    private int _msgCode;

    /**
     * 消息体
     */
    private byte[] _msgBody;

    /**
     * 获取代理服务器 Id
     *
     * @return 代理服务器 Id
     */
    public String getProxyServerId() {
        return _proxyServerId;
    }

    /**
     * 设置代理服务器 Id
     *
     * @param val 代理服务器 Id
     * @return this 指针
     */
    public InternalServerMsg setProxyServerId(String val) {
        _proxyServerId = val;
        return this;
    }

    /**
     * 获取远程会话 Id
     *
     * @return 远程会话 Id
     */
    public int getRemoteSessionId() {
        return _remoteSessionId;
    }

    /**
     * 设置远程会话 Id
     *
     * @param val 远程会话 Id
     * @return this 指针
     */
    public InternalServerMsg setRemoteSessionId(int val) {
        _remoteSessionId = val;
        return this;
    }

    /**
     * 获取来自用户 Id
     *
     * @return 来自用户 Id
     */
    public int getFromUserId() {
        return _fromUserId;
    }

    /**
     * 设置来自用户 Id
     *
     * @param val 来自用户 Id
     * @return this 指针
     */
    public InternalServerMsg setFromUserId(int val) {
        _fromUserId = val;
        return this;
    }

    /**
     * 获取客户端 IP 地址
     *
     * @return 客户端 IP 地址
     */
    public String getClientIP() {
        return _clientIP;
    }

    /**
     * 设置客户端 IP 地址
     *
     * @param val 客户端 IP 地址
     * @return this 指针
     */
    public InternalServerMsg setClientIP(String val) {
        _clientIP = val;
        return this;
    }

    /**
     * 获取消息编码
     *
     * @return 消息编码
     */
    public int getMsgCode() {
        return _msgCode;
    }

    /**
     * 设置消息编码
     *
     * @param val 消息编码
     * @return this 指针
     */
    public InternalServerMsg setMsgCode(int val) {
        _msgCode = val;
        return this;
    }

    /**
     * 获取消息体字节数组
     *
     * @return 消息体字节数组
     */
    public byte[] getMsgBody() {
        return _msgBody;
    }

    /**
     * 设置消息体字节数组
     *
     * @param val 消息体字节数组
     * @return this 指针
     */
    public InternalServerMsg setMsgBody(byte[] val) {
        _msgBody = val;
        return this;
    }

    /**
     * 设置协议消息
     *
     * @param msgObj 消息对象
     */
    public void putProtoMsg(GeneratedMessageV3 msgObj) {
        if (null == msgObj) {
            return;
        }

        _msgCode = MsgRecognizer.getInstance().getMsgCode(msgObj);
        _msgBody = msgObj.toByteArray();
    }

    /**
     * 获取协议消息
     *
     * @return 消息对象
     */
    public GeneratedMessageV3 getProtoMsg() {
        // 获取消息构建器
        Parser<? extends GeneratedMessageV3> msgParser = MsgRecognizer.getInstance().getMsgParserByMsgCode(_msgCode);

        if (null == msgParser) {
            LOGGER.error(
                "未找到消息构建器, msgCode = {}",
                _msgCode
            );
            return null;
        }

        try {
            return msgParser.parseFrom(_msgBody);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * 释放资源
     */
    public void free() {
        _msgBody = null;
    }
}
