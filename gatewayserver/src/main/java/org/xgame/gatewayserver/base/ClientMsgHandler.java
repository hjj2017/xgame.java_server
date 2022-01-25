package org.xgame.gatewayserver.base;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.xgame.gatewayserver.router.ClientMsgRouter;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;

/**
 * 客户端消息处理器
 */
public class ClientMsgHandler extends ChannelDuplexHandler {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = BaseLog.LOGGER;

    /**
     * HTTP 头前缀
     */
    static private final String HTTP_HEADER_PREFIX = "http_header::";

    /**
     * 连接移交被转移
     */
    private boolean _connAlreadyTransfer = false;

    /**
     * 设置连接已经被转移
     *
     * @param val 布尔值
     */
    public void putConnAlreadyTransfer(boolean val) {
        _connAlreadyTransfer = val;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        ChannelHandler[] hArray = {
            new ClientMsgCodec(),
            new ClientMsgRouter()
        };

        // 获取信道管线
        final ChannelPipeline pl = ctx.pipeline();

        for (ChannelHandler h : hArray) {
            // 获取处理器类
            Class<? extends ChannelHandler> hClazz = h.getClass();

            if (null == pl.get(hClazz)) {
                pl.addBefore(ctx.name(), hClazz.getSimpleName(), h);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (null == ctx || null == ctx.channel()) {
            return;
        }

        LOGGER.info("有新的客户端接入");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (null == ctx || null == ctx.channel()) {
            return;
        }

        if (_connAlreadyTransfer) {
            LOGGER.info("客户端连接已转移...");
            return;
        }

        LOGGER.info("有客户端下线");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) {
        // @see ClientMsgRouter
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object objEvent) {
        if (null == ctx ||
            null == ctx.channel() ||
            !(objEvent instanceof HandshakeComplete)) {
            return;
        }

        final HandshakeComplete handshakeComplete = (HandshakeComplete) objEvent;
        final HttpHeaders h = handshakeComplete.requestHeaders();

        final List<Map.Entry<String, String>> entryList = h.entries();

        final Channel ch = ctx.channel();

        for (Map.Entry<String, String> entry : entryList) {
            if (null == entry || null == entry.getKey()) {
                continue;
            }

            ch.attr(AttributeKey.valueOf(HTTP_HEADER_PREFIX + entry.getKey())).set(entry.getValue());
        }
    }

    /**
     * 获取 X-Real-IP 值
     *
     * @param ctx 信道处理器上下文
     * @return X-Real-IP 值
     */
    static public String getXRealIp(ChannelHandlerContext ctx) {
        if (null == ctx || null == ctx.channel()) {
            return null;
        }

        final Channel ch = ctx.channel();
        final AttributeKey<String> attrKey = AttributeKey.valueOf(HTTP_HEADER_PREFIX + "X-Real-IP");

        if (ch.hasAttr(attrKey)) {
            return ch.attr(attrKey).get();
        } else {
            return null;
        }
    }
}
