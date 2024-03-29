package org.xgame.bizserver.base;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.xgame.bizserver.msg.CommProtocol;
import org.xgame.comm.MainThreadProcessor;
import org.xgame.comm.cmdhandler.AbstractCmdHandlerContext;

/**
 * 内部服务器消息处理器
 */
public class InternalServerMsgHandler_BizServer extends ChannelInboundHandlerAdapter {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = BaseLog.LOGGER;

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
    public void channelActive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        LOGGER.info("网关服务器已连入");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        LOGGER.warn("网关服务器已断开");
    }

    @Override
    public void channelRead(ChannelHandlerContext nettyCtx, Object msgObj) {
        if (null == nettyCtx ||
            !(msgObj instanceof InternalServerMsg)) {
            return;
        }

        // 获取内部服务器消息
        InternalServerMsg realMsg = (InternalServerMsg) msgObj;
        // 获取协议消息
        GeneratedMessageV3 protoMsg = realMsg.getProtoMsg();

        if (CommProtocol.CommMsgCodeDef._PingCmd_VALUE != realMsg.getMsgCode()) {
            LOGGER.info(
                "收到内部服务器消息, proxyServerId = {}, remoteSessionId = {}, fromUserId = {}, msgCode = {}, msgClazz = {}",
                realMsg.getProxyServerId(),
                realMsg.getRemoteSessionId(),
                realMsg.getFromUserId(),
                realMsg.getMsgCode(),
                null == protoMsg ? "NULL" : protoMsg.getClass().getSimpleName()
            );
        }

        AbstractCmdHandlerContext myCtx = new MyCmdHandlerContext(nettyCtx.channel())
            .setProxyServerId(realMsg.getProxyServerId())
            .setRemoteSessionId(realMsg.getRemoteSessionId())
            .setFromUserId(realMsg.getFromUserId())
            .setClientIP(realMsg.getClientIP());

        // 处理命令对象
        // XXX 注意: 命令对象具体是由哪个 CmdHandler 处理器的,
        // 可以参考 CmdHandlerFactory 类!
        // CmdHandlerFactory 类是 MainThreadProcessor 构造器中的一个参数,
        // 它会自动扫描指定包中所有实现了 ICmdHandler 接口的类...
        MainThreadProcessor.getInstance().process(
            myCtx, protoMsg
        );

        realMsg.free();
    }
}
