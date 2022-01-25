package org.xgame.gatewayserver.cluster.finder;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.network.NettyClient;
import org.xgame.gatewayserver.cluster.ClusterLog;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * 通过 Nacos 发现
 */
class FindByNacos implements IBizServerFindStrategy {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = ClusterLog.LOGGER;

    /**
     * 服务名称
     */
    private static final String SERVICE_NAME_ORG_XGAME_BIZSERVER = "org.xgame.bizserver";

    /**
     * 发现服务
     */
    private NamingService _ns;

    /**
     * 业务服务器连接助手
     */
    private final BizServerConnectHelper _connectHelper = new BizServerConnectHelper();

    @Override
    public void startFind(CommandLine cmdLn) {
        String serverAddrOfNacos = cmdLn.getOptionValue("nacos_server_addr");
        _ns = createNamingService(serverAddrOfNacos);

        try {
            for (ServerJobTypeEnum serverJobType : ServerJobTypeEnum.values()) {
                _ns.subscribe(
                    SERVICE_NAME_ORG_XGAME_BIZSERVER,
                    serverJobType.name(),
                    this::namingService_subscribe
                );
            }
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public NettyClient selectOneBizServer(ServerJobTypeEnum sjt) {
        if (null == sjt ||
            null == _ns) {
            return null;
        }

        try {
            List<Instance> instanceList = _ns.selectInstances(
                SERVICE_NAME_ORG_XGAME_BIZSERVER,
                sjt.getStrVal(), true
            );

            Instance instance = instanceList.stream()
                .min(Comparator.comparing(Instance::getWeight))
                .orElse(null);

            if (null == instance) {
                return null;
            }

            return _connectHelper.find(sjt, instance.getInstanceId());
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * 创建发现服务
     *
     * @param serverAddrOfNacos Nacos 服务器地址
     * @return 发现服务
     */
    static private NamingService createNamingService(String serverAddrOfNacos) {
        if (null == serverAddrOfNacos ||
            serverAddrOfNacos.isEmpty()) {
            return null;
        }

        Properties prop = new Properties();
        prop.put("serverAddr", serverAddrOfNacos);

        try {
            return NamingFactory.createNamingService(prop);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * 发现服务订阅
     *
     * @param oEvent 时间对象
     */
    private void namingService_subscribe(Event oEvent) {
        if (!(oEvent instanceof NamingEvent)) {
            return;
        }

        NamingEvent ne = (NamingEvent) oEvent;

        // 获取实例列表
        List<Instance> instanceList = ne.getInstances();

        for (Instance instance : instanceList) {
            if (null == instance) {
                continue;
            }

            connectToBizServer(
                ServerJobTypeEnum.strToVal(ne.getGroupName()),
                instance
            );
        }
    }

    /**
     * 连接到业务服务器
     *
     * @param sjt         服务器工作类型
     * @param regInstance 服务器信息
     */
    private void connectToBizServer(ServerJobTypeEnum sjt, Instance regInstance) {
        if (null == sjt ||
            null == regInstance) {
            return;
        }

        _connectHelper.connectToBizServer(
            sjt, // 服务器工作类型
            regInstance.getInstanceId(),
            regInstance.getIp(),
            regInstance.getPort()
        );
    }
}