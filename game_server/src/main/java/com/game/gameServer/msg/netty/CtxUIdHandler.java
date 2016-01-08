package com.game.gameServer.msg.netty;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import com.game.part.util.NullUtil;

/**
 * 创建 UId
 *
 * @author hjj2019
 * @since 2016/1/8
 *
 */
public class CtxUIdHandler extends ChannelInboundHandlerAdapter {
    /** CTX_UID Key */
    private static final AttributeKey<Long> CTX_UID = AttributeKey.newInstance("CTX_UID");
    /** 计数器, 专门用来生成 UID */
    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if (ctx != null) {
            // 设置 UID, 相当于 SessionUId
            ctx.attr(CTX_UID).setIfAbsent(COUNTER.incrementAndGet());
        }

        super.channelRegistered(ctx);
    }

    /**
     * 获取 UId
     *
     * @param ctx
     * @return
     *
     */
    public static long getUId(ChannelHandlerContext ctx) {
        if (ctx != null) {
            // 获取 CTX_UID
            Long val = ctx.attr(CTX_UID).get();

            if (val == null) {
                return 0;
            } else {
                return val;
            }
        } else {
            return 0;
        }
    }
}
