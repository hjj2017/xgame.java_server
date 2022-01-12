package org.xgame.gatewayserver.base;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xgame.bizserver.base.InternalServerMsg;
import org.xgame.bizserver.base.InternalServerMsgCodec;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;

/**
 * 内部消息处理器
 */
public class InternalMsgHandler_GatewayServer extends ChannelDuplexHandler {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(InternalMsgHandler_GatewayServer.class);

    /**
     * Ping 间隔时间
     */
    static private final int PING_INTERVAL_TIME = 5000;

    /**
     * Ping Id
     */
    private final AtomicInteger _pingId = new AtomicInteger(0);

    /**
     * Ping 心跳
     */
    private ScheduledFuture<?> _pingHeartbeat;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        // 添加编解码器
        ctx.pipeline().addBefore(
            ctx.name(),
            InternalServerMsgCodec.class.getName(),
            new InternalServerMsgCodec()
        );
    }

    @Override
    public void userEventTriggered(
        ChannelHandlerContext ctx, Object eventObj) {
        if (null == ctx ||
            !(eventObj instanceof ClientHandshakeStateEvent)) {
            return;
        }

        ClientHandshakeStateEvent
            realEvent = (ClientHandshakeStateEvent) eventObj;

        if (ClientHandshakeStateEvent.HANDSHAKE_COMPLETE != realEvent) {
            return;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        if (null != _pingHeartbeat) {
            LOGGER.debug("停止 Ping");
            _pingHeartbeat.cancel(true);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) {
        if (null == ctx ||
            !(msgObj instanceof InternalServerMsg)) {
            return;
        }
    }

    /**
     * 执行 Ping 命令
     *
     * @param ctx 信道处理器上下文
     */
    private void doPing(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }
    }
}
