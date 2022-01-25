package org.xgame.gatewayserver.router;

import io.netty.channel.ChannelHandlerContext;

/**
 * 路由器接口
 */
interface IRouter {
    /**
     * 读取消息
     *
     * @param ctx    信道处理器上下文
     * @param msgObj 消息对象
     */
    void channelRead(ChannelHandlerContext ctx, Object msgObj);
}
