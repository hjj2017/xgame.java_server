package org.xgame.gatewayserver.cluster;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.network.NettyClient;
import org.xgame.comm.network.NettyClientConf;
import org.xgame.comm.util.MyTimer;
import org.xgame.gatewayserver.base.InternalServerMsgHandler_GatewayServer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 业务服务器发现者
 */
public final class BizServerFinder {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = ClusterLog.LOGGER;

    /**
     * 服务名称
     */
    static private final String SERVICE_NAME_ORG_XGAME_BIZSERVER = "org.xgame.bizserver";

    /**
     * 单例对象
     */
    static private final BizServerFinder INSTANCE = new BizServerFinder();

    /**
     * 命令行参数
     */
    private CommandLine _cmdLn;

    /**
     * 发现服务
     */
    private NamingService _ns = null;

    /**
     * 客户端字典
     */
    private final Map<ServerJobTypeEnum, Map<String, NettyClient>> _nettyClientMap = new ConcurrentHashMap<>();

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
            startFind_byNacos();
        } else {
            startFind_byConfigFile();
        }
    }

    /**
     * 开始发现 - 通过 Nacos
     */
    private void startFind_byNacos() {
        String serverAddrOfNacos = _cmdLn.getOptionValue("nacos_server_addr");
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
                instance,
                ServerJobTypeEnum.strToVal(ne.getGroupName())
            );
        }
    }

    /**
     * 连接到业务服务器
     *
     * @param regInstance 服务器信息
     * @param sjt         服务器工作类型
     */
    private void connectToBizServer(Instance regInstance, ServerJobTypeEnum sjt) {
        if (null == sjt ||
            null == regInstance) {
            return;
        }

        connectToBizServer(
            regInstance.getInstanceId(),
            sjt, // 服务器工作类型
            regInstance.getIp(),
            regInstance.getPort()
        );
    }

    /**
     * 开始发现 - 通过配置文件
     */
    private void startFind_byConfigFile() {
        String strConfig = null;

        try {
            strConfig = Files.readString(Paths.get(_cmdLn.getOptionValue("config_file")));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        if (null == strConfig ||
            strConfig.isEmpty()) {
            LOGGER.error("配置文本为空, 请检查 Nacos 或配置文件");
            System.exit(-1);
            return;
        }

        JSONObject joConfig = JSONObject.parseObject(strConfig);
        JSONArray jaPossibleBizServerList = joConfig.getJSONArray("possibleBizServerList");

        MyTimer.getInstance().scheduleWithFixedDelay(() -> {
            for (int i = 0; i < jaPossibleBizServerList.size(); i++) {
                // 获取业务服务器配置
                JSONObject joBizServer = jaPossibleBizServerList.getJSONObject(i);

                if (null == joBizServer) {
                    continue;
                }

                final Set<ServerJobTypeEnum> sjtSet = ServerJobTypeEnum.strToValSet(
                    joBizServer.getString("serverJobTypeSet")
                );

                AsyncOperationProcessor.getInstance().process(i, () ->
                    sjtSet.forEach((sjt) ->
                        connectToBizServer(
                            joBizServer.getString("serverId"),
                            sjt, // 服务器工作类型
                            joBizServer.getString("host"),
                            joBizServer.getIntValue("port")
                        )
                    )
                );
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 连接到业务服务器
     *
     * @param sjt      服务器工作类型
     * @param serverId 服务器 Id
     * @param host     主机地址
     * @param port     端口号
     */
    private void connectToBizServer(String serverId, ServerJobTypeEnum sjt, String host, int port) {
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

    /**
     * 根据职务类型选择一个游戏服务器
     *
     * @param sjt 职务类型
     * @return Netty 客户端
     */
    public NettyClient selectOneBizServer(ServerJobTypeEnum sjt) {
        if (null == sjt ||
            !_nettyClientMap.containsKey(sjt) ||
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

            return _nettyClientMap.get(sjt).get(instance.getInstanceId());
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }
}
