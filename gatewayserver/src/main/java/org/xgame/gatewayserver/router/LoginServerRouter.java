package org.xgame.gatewayserver.router;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.xgame.bizserver.base.InternalServerMsg;
import org.xgame.bizserver.base.MsgRecognizer;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.network.NettyClient;
import org.xgame.gatewayserver.GatewayServer;
import org.xgame.gatewayserver.base.ClientMsgSemiFinished;
import org.xgame.gatewayserver.base.IdSetterGetter;
import org.xgame.gatewayserver.cluster.finder.BizServerFinder;

public class LoginServerRouter {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = RouterLog.LOGGER;

    /**
     * 执行
     *
     * @param ctx    信道处理器上下文
     * @param msgObj 消息对象
     */
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) {
        if (!(msgObj instanceof ClientMsgSemiFinished)) {
            if (null != ctx) {
                ctx.fireChannelRead(msgObj);
            }

            return;
        }

        ClientMsgSemiFinished clientMsg = (ClientMsgSemiFinished) msgObj;
        final int msgCode = clientMsg.getMsgCode();

        // 获取当前服务器工作类型
        ServerJobTypeEnum sjt = MsgRecognizer.getInstance().getServerJobTypeByMsgCode(msgCode);

        if (ServerJobTypeEnum.LOGIN != sjt) {
            LOGGER.error(
                "当前命令不属于账户模块, msgCode = {}",
                msgCode
            );
            return;
        }

        // 获取服务器连接, 也就是本机到目标服务器的连接
        // 登录过程都是随机选择一个服务器
        NettyClient serverConn = BizServerFinder.getInstance().selectOneBizServer(ServerJobTypeEnum.LOGIN);

        if (null == serverConn ||
            !serverConn.isOpen()) {
            LOGGER.error(
                "未找到合适的登陆服务器来接收消息, msgCode = {}",
                msgCode
            );
            return;
        }

        final InternalServerMsg innerMsg = new InternalServerMsg();
        innerMsg.setProxyServerId(GatewayServer.getId());
        innerMsg.setRemoteSessionId(IdSetterGetter.getSessionId(ctx));
        innerMsg.setFromUserId(IdSetterGetter.getUserId(ctx));
        innerMsg.setMsgCode(msgCode);
        innerMsg.setMsgBody(clientMsg.getMsgBody());

        LOGGER.info(
            "转发消息到内部服务器, msgCode = {}, targetServerId = {}",
            msgCode,
            serverConn.getUsingConf().getServerId()
        );

        serverConn.sendMsg(innerMsg);

        // 释放资源
        clientMsg.free();
    }
}
