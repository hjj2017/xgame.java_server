package com.game.gameServer.msg.netty;

import java.text.MessageFormat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.SpecialMsgSerialUId;
import com.game.part.msg.MsgLog;
import com.game.part.msg.MsgServ;

/**
 * 自定义消息处理器
 *
 */
public class MyChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 断言参数不为空
        assert ctx != null : "ctx";
        // 调用父类同名函数
        super.channelRegistered(ctx);

        // 添加上下文对象
        CtxManager.OBJ.addCtx(ctx);
        // 获取消息对象
        AbstractCGMsgObj cgMsgObj = MsgServ.OBJ.newMsgObj(SpecialMsgSerialUId.PLAYER_CONNECTED);
        // 发送消息
        this.postMsg(ctx, cgMsgObj);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        // 断言参数不为空
        assert ctx != null : "null ctx";
        assert obj != null : "null obj";

        if (obj instanceof AbstractCGMsgObj) {
            // 输出日志记录
            MsgLog.LOG.info(MessageFormat.format(
                "接到消息 {0}, ctxUId = {1}",
                obj.toString(),
                String.valueOf(CtxManager.OBJ.getUId(ctx))
            ));

            // 发送消息
            this.postMsg(
                ctx, (AbstractCGMsgObj)obj
            );
        }
    }

    @Override
    public void channelUnregistered(
        ChannelHandlerContext ctx) throws Exception {
        // 断言参数不为空
        assert ctx != null : "ctx";
        // 调用父类同名函数
        super.channelUnregistered(ctx);

        // 创建并发送短线消息
        AbstractCGMsgObj cgMsgObj = MsgServ.OBJ.newMsgObj(SpecialMsgSerialUId.PLAYER_DISCONNECTED);
        this.postMsg(ctx, cgMsgObj);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        // 记录错误日志
        MsgLog.LOG.error(err.getMessage(), err);
        // 令玩家断开连接
        ctx.disconnect();
        ctx.close();
    }

    /**
     * 发送消息对象
     *
     * @param ctx
     * @param cgMsgObj
     *
     */
    private void postMsg(
        ChannelHandlerContext ctx, AbstractCGMsgObj cgMsgObj) {
        if (ctx == null ||
            cgMsgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 设置消息处理器
        AbstractCGMsgHandler<?> handler = cgMsgObj.getSelfHandler();

        if (handler != null) {
            // 设置会话 UId
            handler._ctxUId = CtxManager.OBJ.getUId(ctx);
        }

        // 分派消息对象
        MsgServ.OBJ.post(cgMsgObj);
    }
}
