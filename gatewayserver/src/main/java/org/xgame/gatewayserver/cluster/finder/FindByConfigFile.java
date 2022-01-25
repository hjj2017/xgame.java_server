package org.xgame.gatewayserver.cluster.finder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.network.NettyClient;
import org.xgame.comm.util.MyTimer;
import org.xgame.gatewayserver.cluster.ClusterLog;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 通过配置文件发现,
 * 其本质是通过定时任务不断的尝试连接目标服务器!
 * 目标服务器有哪些?
 * 这个已经在配置文件中明确定义了...
 * 所以无法像 Nacos 那样, 支持运行期间动态增加业务服务器...
 */
class FindByConfigFile implements IBizServerFindStrategy {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = ClusterLog.LOGGER;

    /**
     * 业务服务器连接助手
     */
    private final BizServerConnectHelper _connectHelper = new BizServerConnectHelper();

    /**
     * 业务服务器 Id 字典
     */
    private final Map<ServerJobTypeEnum, Set<String>> _bizServerIdMap = new ConcurrentHashMap<>();

    @Override
    public void startFind(CommandLine cmdLn) {
        if (null == cmdLn ||
            !cmdLn.hasOption("config_file")) {
            return;
        }

        String strConfig = null;

        try {
            strConfig = Files.readString(Paths.get(cmdLn.getOptionValue("config_file")));
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

        MyTimer.getInstance().scheduleWithFixedDelay(
            () -> connectToBizServer(jaPossibleBizServerList),
            0,
            5000, // 每隔 5 秒连接一次
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public NettyClient selectOneBizServer(ServerJobTypeEnum sjt) {
        if (null == sjt) {
            return null;
        }

        Set<String> innerSet = _bizServerIdMap.get(sjt);

        if (null == innerSet ||
            innerSet.isEmpty()) {
            return null;
        }

        return _connectHelper.find(
            sjt,
            innerSet.stream().findAny().get()
        );
    }

    /**
     * 连接到业务服务器
     *
     * @param jaPossibleBizServerList 业务服务器列表
     * @see #connectToBizServer(JSONObject)
     * @see #connectToBizServer(ServerJobTypeEnum, JSONObject)
     */
    private void connectToBizServer(JSONArray jaPossibleBizServerList) {
        if (null == jaPossibleBizServerList ||
            jaPossibleBizServerList.isEmpty()) {
            return;
        }

        for (int i = 0; i < jaPossibleBizServerList.size(); i++) {
            // 获取业务服务器配置
            JSONObject joBizServer = jaPossibleBizServerList.getJSONObject(i);
            connectToBizServer(joBizServer);
        }
    }

    /**
     * 连接到业务服务器
     *
     * @param joBizServer 业务服务器配置
     * @see #connectToBizServer(ServerJobTypeEnum, JSONObject)
     */
    private void connectToBizServer(JSONObject joBizServer) {
        if (null == joBizServer ||
            joBizServer.isEmpty()) {
            return;
        }

        final Set<ServerJobTypeEnum> sjtSet = ServerJobTypeEnum.strToValSet(
            joBizServer.getString("serverJobTypeSet")
        );

        sjtSet.forEach((sjt) ->
            connectToBizServer(sjt, joBizServer)
        );
    }

    /**
     * 连接到业务服务器
     *
     * @param sjt         服务器工作类型
     * @param joBizServer 业务服务器配置
     */
    private void connectToBizServer(ServerJobTypeEnum sjt, JSONObject joBizServer) {
        if (null == sjt ||
            null == joBizServer ||
            joBizServer.isEmpty()) {
            return;
        }

        Set<String> innerSet = _bizServerIdMap.get(sjt);

        if (null == innerSet) {
            _bizServerIdMap.putIfAbsent(
                sjt, new HashSet<>()
            );
        }

        innerSet = _bizServerIdMap.get(sjt);
        innerSet.add(joBizServer.getString("serverId"));

        AsyncOperationProcessor.getInstance().process(() ->
            _connectHelper.connectToBizServer(
                sjt, // 服务器工作类型
                joBizServer.getString("serverId"),
                joBizServer.getString("host"),
                joBizServer.getIntValue("port")
            )
        );
    }
}
