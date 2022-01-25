package org.xgame.gatewayserver.base;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.xgame.bizserver.base.InternalServerMsg;
import org.xgame.bizserver.base.InternalServerMsgCodec;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;

/**
 * 内部消息处理器
 */
public class InternalServerMsgHandler_GatewayServer extends ChannelInboundHandlerAdapter {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = BaseLog.LOGGER;

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

        ChannelHandler[] hArray = {
            new LengthFieldBasedFrameDecoder(4096, 0, 2, 0, 2),
            new LengthFieldPrepender(2),
            new InternalServerMsgCodec(),
        };

        // 获取信道管线
        final ChannelPipeline pl = ctx.pipeline();

        for (ChannelHandler h : hArray) {
            // 获取处理器类
            Class<? extends ChannelHandler>
                hClazz = h.getClass();

            if (null == pl.get(hClazz)) {
                pl.addBefore(ctx.name(), hClazz.getSimpleName(), h);
            }
        }
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
