package com.game.gameServer.msg.netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.game.part.msg.type.AbstractMsgObj;

/**
 * 消息编码器
 *
 * @author hjj2019
 * @since 2016/1/8
 *
 */
public class MsgEncoder extends MessageToByteEncoder {
    /** 消息默认容量 */
    private static final int MSG_DEFAULT_CAPCITY = 256;

    @Override
    protected void encode(
        ChannelHandlerContext ctx, Object o, ByteBuf nettyBuf) throws Exception {
        if (!(o instanceof AbstractMsgObj)) {
            // 如果不是消息对象,
            // 则直接退出!
            return;
        }

        // 创建 IoBuff 对象
        ByteBuffer nioBuf = ByteBuffer.allocate(MSG_DEFAULT_CAPCITY);
        nioBuf.order(ByteOrder.LITTLE_ENDIAN);
        nioBuf.position(0);

        // 获取消息对象
        AbstractMsgObj msgObj = (AbstractMsgObj)o;
        // 令消息写出数据
        msgObj.writeBuff(nioBuf);
        nioBuf.flip();

        // 向下处理
        nettyBuf.writeBytes(nioBuf);
    }
}
