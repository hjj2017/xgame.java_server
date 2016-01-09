package com.game.gameServer.msg.netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.MessageFormat;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import com.game.part.msg.IoBuffUtil;
import com.game.part.msg.MsgLog;
import com.game.part.msg.MsgServ;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 消息解码器
 *
 * @author hjj2019
 * @since 2016/1/8
 *
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(
        ChannelHandlerContext ctx,
        ByteBuf nettyBuf,
        List<Object> out_objList) throws Exception {

        if (nettyBuf == null ||
            out_objList == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取 NioBuff 并按字节序
        ByteBuffer nioBuf = nettyBuf.nioBuffer();
        nioBuf.order(ByteOrder.LITTLE_ENDIAN);

        // 保存原有位置
        final int oldPos = nioBuf.position();
        // 首先, 跳过消息长度
        IoBuffUtil.readShort(nioBuf);
        // 获取消息序列化 Id
        short msgSerialUId = IoBuffUtil.readShort(nioBuf);
        // 还原原有位置
        nioBuf.position(oldPos);

        //
        // 获取消息对象, 注意:
        // CG 消息和 GC 消息,
        // 都是继承自 AbstractMsgObj 类
        AbstractMsgObj msgObj = MsgServ.OBJ.newMsgObj(msgSerialUId);

        if (msgObj == null) {
            // 如果消息对象为空,
            // 则直接退出!
            MsgLog.LOG.error(MessageFormat.format(
                "无法取得消息对象, msgSerialUId = {0}",
                String.valueOf(msgSerialUId)
            ));
            return;
        }

        // 令消息读取数据
        msgObj.readBuff(nioBuf);

        // 修改索引位置
        nettyBuf.setIndex(
            nioBuf.position(), nettyBuf.writerIndex()
        );

        // 向下处理
        out_objList.add(msgObj);
    }
}
