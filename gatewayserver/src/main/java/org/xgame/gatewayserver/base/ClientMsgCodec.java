package org.xgame.gatewayserver.base;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.xgame.bizserver.base.InternalServerMsg;
import org.xgame.bizserver.base.MsgRecognizer;

/**
 * 客户端消息编解码器
 */
public class ClientMsgCodec extends CombinedChannelDuplexHandler<ClientMsgCodec.Decoder, ClientMsgCodec.Encoder> {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = BaseLog.LOGGER;

    /**
     * 客户端消息解码器
     * XXX 注意: 这里只将消息解码成半成品,
     * 不会完全解析成命令对象 XxxCmd!
     */
    static final class Decoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msgObj) {
            try {
                if (null == ctx ||
                    !(msgObj instanceof BinaryWebSocketFrame)) {
                    super.channelRead(ctx, msgObj);
                    return;
                }

                BinaryWebSocketFrame inputFrame = (BinaryWebSocketFrame) msgObj;
                ByteBuf byteBuf = inputFrame.content();

                // 读掉消息长度
                byteBuf.readShort();

                // 读取消息编码
                int msgCode = byteBuf.readShort();
                // 读取消息体
                byte[] msgBody = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(msgBody);

                // 创建客户端消息半成品
                ClientMsgSemiFinished clientMsg = new ClientMsgSemiFinished();
                clientMsg.setMsgCode(msgCode);
                clientMsg.setMsgBody(msgBody);

                // 继续派发消息
                ctx.fireChannelRead(clientMsg);
                // 释放资源
                ReferenceCountUtil.safeRelease(inputFrame);
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 客户端消息编码器
     */
    static final class Encoder extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msgObj, ChannelPromise promise) {
            try {
                if (!(msgObj instanceof GeneratedMessageV3) &&
                    !(msgObj instanceof InternalServerMsg)) {
                    super.write(ctx, msgObj, promise);
                    return;
                }

                // 定义消息编码和消息体
                int msgCode;
                byte[] msgBody;

                if (msgObj instanceof InternalServerMsg) {
                    // 如果是内部服务器消息
                    msgCode = ((InternalServerMsg) msgObj).getMsgCode();
                    msgBody = ((InternalServerMsg) msgObj).getMsgBody();
                    // 释放资源
                    ((InternalServerMsg) msgObj).free();
                } else /*if (msgObj instanceof GeneratedMessageV3)*/ {
                    // 如果是协议消息
                    msgCode = MsgRecognizer.getInstance().getMsgCode((GeneratedMessageV3) msgObj);
                    msgBody = ((GeneratedMessageV3) msgObj).toByteArray();
                }

                ByteBuf byteBuf = ctx.alloc().buffer();

                // 先写出消息长度, 避免粘包情况!
                // XXX 注意: 2 = sizeof(short)
                byteBuf.writeShort(2 + msgBody.length);
                byteBuf.writeShort(msgCode);
                byteBuf.writeBytes(msgBody);

                // 写出消息
                BinaryWebSocketFrame outputFrame = new BinaryWebSocketFrame(byteBuf);
                ctx.write(outputFrame, promise);
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
}
