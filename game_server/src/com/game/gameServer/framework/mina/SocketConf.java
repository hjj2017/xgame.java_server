package com.game.gameServer.framework.mina;

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

    /**
     * 类默认构造器
     *
     */
    private SocketConf() {
    }

    /** 绑定 IP 地址 */
    public String _bindIpAddr = "0.0.0.0";
    /** 绑定端口 */
    public int _bindPort = 4400;
    /** 消息加密 ? */
    public boolean _msgEncrypt = true;
}
