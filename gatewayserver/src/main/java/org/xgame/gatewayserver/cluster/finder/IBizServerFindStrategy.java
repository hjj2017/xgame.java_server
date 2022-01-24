package org.xgame.gatewayserver.cluster.finder;

import org.apache.commons.cli.CommandLine;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.network.NettyClient;

/**
 * 业务服务器发现策略
 */
interface IBizServerFindStrategy {
    /**
     * 开始发现
     *
     * @param cmdLn 命令行参数
     */
    void startFind(CommandLine cmdLn);

    /**
     * 根据服务器工作类型选择一个客户端
     *
     * @param sjt 服务器工作类型
     * @return 客户端
     */
    NettyClient selectOneBizServer(ServerJobTypeEnum sjt);
}
