package org.xgame.gatewayserver.cluster.finder;

import org.slf4j.Logger;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.network.NettyClient;
import org.xgame.comm.network.NettyClientConf;
import org.xgame.gatewayserver.base.InternalServerMsgHandler_GatewayServer;
import org.xgame.gatewayserver.cluster.ClusterLog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务服务器连接助手
 */
class BizServerConnectHelper {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = ClusterLog.LOGGER;

    /**
     * 客户端字典
     */
    private final Map<ServerJobTypeEnum, Map<String, NettyClient>> _nettyClientMap = new ConcurrentHashMap<>();

    /**
     * 连接到业务服务器
     *
     * @param sjt      服务器工作类型
     * @param serverId 服务器 Id
     * @param host     主机地址
     * @param port     端口号
     */
    void connectToBizServer(String serverId, ServerJobTypeEnum sjt, String host, int port) {
        if (null == serverId ||
            null == sjt ||
            null == host ||
            port <= 0) {
            return;
        }

        Map<String, NettyClient> innerMap = _nettyClientMap.get(sjt);

        if (null == innerMap) {
            innerMap = new ConcurrentHashMap<>();
            _nettyClientMap.putIfAbsent(sjt, innerMap);
            innerMap = _nettyClientMap.get(sjt);
        }

        if (innerMap.containsKey(serverId)) {
            return;
        }

        LOGGER.info(
            "发现新服务器, serverId = {}, serverJobType = {}, serverAddr = {}:{}",
            serverId, sjt.getStrVal(), host, port
        );

        NettyClientConf newConf = new NettyClientConf()
            .setServerId(serverId)
            .setServerHost(host)
            .setServerPort(port)
            .setCustomChannelHandlerFactory(InternalServerMsgHandler_GatewayServer::new)
            .setCloseCallback(innerMap.values()::remove);

        NettyClient newClient = new NettyClient(newConf);
        newClient.connect();

        innerMap.put(serverId, newClient);
    }
}
