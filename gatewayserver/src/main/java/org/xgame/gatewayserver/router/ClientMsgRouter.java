package org.xgame.gatewayserver.router;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.xgame.bizserver.def.ServerJobTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端消息路由器
 */
public class ClientMsgRouter extends ChannelInboundHandlerAdapter {
    /**
     * 服务器工作类型和信道处理器字典
     */
    private final Map<ServerJobTypeEnum, ChannelInboundHandler> _hMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     */
    public ClientMsgRouter() {
        // 在此添加新的服务器工作类型和处理器
        _hMap.put(ServerJobTypeEnum.CHAT, null);
        _hMap.put(ServerJobTypeEnum.LOGIN, null);
    }
}
