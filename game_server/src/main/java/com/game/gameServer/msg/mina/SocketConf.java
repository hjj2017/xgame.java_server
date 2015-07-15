package com.game.gameServer.msg.mina;

import net.sf.json.JSONObject;

/**
 * Socket 连接配置
 *
 * @author hjj2017
 * @since 2015/7/4
 *
 */
public final class SocketConf {
    /** 单例对象 */
    public static final SocketConf OBJ = new SocketConf();

    /** 绑定 IP 地址 */
    public String _bindIpAddr = "0.0.0.0";
    /** 绑定端口 */
    public int _bindPort = 4400;
    /** 消息加密 ? */
    public boolean _msgEncrypt = true;

    /**
     * 类默认构造器
     *
     */
    private SocketConf() {
    }

    /**
     * 从 JSON 对象中读取配置
     *
     * @param jsonObj
     *
     */
    public void readFromJson(JSONObject jsonObj) {
        if (jsonObj == null ||
            jsonObj.isEmpty()) {
            return;
        }

        // 服务器绑定 IP 地址
        this._bindIpAddr = jsonObj.optString("bindIpAddr", this._bindIpAddr);
        // 绑定端口号
        this._bindPort = jsonObj.optInt("bindPort", this._bindPort);
        // 消息加密 ?
        this._msgEncrypt = (jsonObj.optInt("msgEncrypt", 1) == 1);
    }
}
