package com.game.gameServer.msg.netty;

import java.text.MessageFormat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.SpecialMsgSerialUId;
import com.game.part.msg.MsgLog;
import com.game.part.msg.MsgServ;

/**
 * 自定义消息处理器
 *
 * @author hjj2017
 * @since 2016/1/10
 *
 */
public class MyChannelHandler extends ChannelInboundHandlerAdapter {
    /** 会话 UId */
    private static final AttributeKey<Long> CTX_SESSION_UID = AttributeKey.newInstance("CTX_SESSION_UID");

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 断言参数不为空
        assert ctx != null : "ctx";
        // 调用父类同名函数
        super.channelRegistered(ctx);

        // 新建会话对象
        IoSession newSession = new IoSession(ctx.channel());
        // 将会话 UId 保存到上下文中
        ctx.attr(CTX_SESSION_UID).set(newSession.getUId());
        // 注意 : 这里用的不是 setIfAbsent 函数!
        // 不管原来有没有值,
        // 始终都使用新值代替!!

        // 添加会话对象
        IoSessionManager.OBJ.addSession(newSession);

        // 创建 CG_PLAYER_CONNECTED 消息,
        // 注意 : 有可能在业务模块中并未监听该消息...
        // 如果监听了就处理,
        // 如果没有监听就不处理,
        // 没有什么大碍
        AbstractCGMsgObj cgMsgObj = MsgServ.OBJ.newMsgObj(SpecialMsgSerialUId.PLAYER_CONNECTED);
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
                "接到消息 {0}, sessionUId = {1}",
                obj.toString(),
                String.valueOf(getSessionUId(ctx))
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

        // 创建 CG_PLAYER_DISCONNECTED 消息,
        // 注意 : 有可能在业务模块中并未监听该消息...
        // 但是最好实现这个消息
        AbstractCGMsgObj cgMsgObj = MsgServ.OBJ.newMsgObj(SpecialMsgSerialUId.PLAYER_DISCONNECTED);
        this.postMsg(ctx, cgMsgObj);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        // 记录错误日志
        MsgLog.LOG.error(err.getMessage(), err);
        // 令玩家断开连接
        ctx.close();
    }

    /**
     * 发送消息对象
     *
     * @param ctx Netty 上下文
     * @param cgMsgObj CG 消息对象
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
            handler._sessionUId = getSessionUId(ctx);
        }

        // 分派消息对象
        MsgServ.OBJ.post(cgMsgObj);
    }

    /**
     * 获取会话 UId
     *
     * @param ctx Netty 上下文
     * @return 返回会话 UId
     *
     */
    private static long getSessionUId(ChannelHandlerContext ctx) {
        if (ctx != null) {
            // 获取 CTX_SESSION_UID
            Long val = ctx.attr(CTX_SESSION_UID).get();

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
