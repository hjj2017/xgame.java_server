package org.xgame.gatewayserver.router;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.xgame.bizserver.base.MsgRecognizer;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.gatewayserver.base.ClientMsgSemiFinished;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端消息路由器
 */
public class ClientMsgRouter extends ChannelInboundHandlerAdapter {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = RouterLog.LOGGER;

    /**
     * 服务器工作类型和信道处理器字典
     */
    private final Map<ServerJobTypeEnum, IRouter> _hMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     */
    public ClientMsgRouter() {
        // 在此添加新的服务器工作类型和处理器
        _hMap.put(ServerJobTypeEnum.CHAT, null);
        _hMap.put(ServerJobTypeEnum.LOGIN, new LoginServerRouter());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) {
        if (null == ctx ||
            null == msgObj) {
            return;
        }

        if (!(msgObj instanceof ClientMsgSemiFinished)) {
            ctx.fireChannelRead(msgObj);
            return;
        }

        ClientMsgSemiFinished clientMsg = (ClientMsgSemiFinished) msgObj;
        final int msgCode = clientMsg.getMsgCode();

        // 获取当前服务器工作类型
        ServerJobTypeEnum sjt = MsgRecognizer.getInstance().getServerJobTypeByMsgCode(msgCode);

        if (null == sjt) {
            LOGGER.error(
                "未识别出服务器工作类型, msgCode = {}",
                clientMsg.getMsgCode()
            );
            return;
        }

        // 获取处理器
        IRouter r = _hMap.get(sjt);

        if (null == r) {
            LOGGER.error(
                "未找到可以处理 {} 类型消息的处理器, 请修改 {} 类默认构造器增加相关代码",
                sjt,
                ClientMsgRouter.class.getName()
            );
            return;
        }

        r.channelRead(ctx, msgObj);
    }
}
