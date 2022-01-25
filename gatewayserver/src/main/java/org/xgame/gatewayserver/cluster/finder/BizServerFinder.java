package org.xgame.gatewayserver.cluster.finder;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.network.NettyClient;
import org.xgame.gatewayserver.cluster.ClusterLog;

/**
 * 业务服务器发现者
 */
public final class BizServerFinder {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = ClusterLog.LOGGER;

    /**
     * 单例对象
     */
    private static final BizServerFinder INSTANCE = new BizServerFinder();

    /**
     * 命令行参数
     */
    private CommandLine _cmdLn;

    /**
     * 业务服务器发现策略
     */
    private IBizServerFindStrategy _realFinder;

    /**
     * 私有化类默认构造器
     */
    private BizServerFinder() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    static public BizServerFinder getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param cmdLn 命令行参数
     * @return this 指针
     */
    public BizServerFinder init(CommandLine cmdLn) {
        _cmdLn = cmdLn;
        return this;
    }

    /**
     * 开始发现
     */
    public void startFind() {
        if (_cmdLn.hasOption("nacos_server_addr")) {
            _realFinder = new FindByNacos();
        } else if (_cmdLn.hasOption("config_file")) {
            _realFinder = new FindByConfigFile();
        } else {
            LOGGER.warn("命令行参数缺少 --nacos_server_addr 和 --config_file, 跳过业务服务器发现过程");
            return;
        }

        _realFinder.startFind(_cmdLn);
    }

    /**
     * 根据职务类型选择一个游戏服务器
     *
     * @param sjt 职务类型
     * @return Netty 客户端
     */
    public NettyClient selectOneBizServer(ServerJobTypeEnum sjt) {
        if (null != _realFinder) {
            return _realFinder.selectOneBizServer(sjt);
        }

        LOGGER.warn("业务服务器发现者未空");
        return null;
    }
}
