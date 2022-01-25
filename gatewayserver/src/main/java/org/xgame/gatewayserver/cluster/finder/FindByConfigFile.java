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
import java.util.Set;
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
                        _connectHelper.connectToBizServer(
                            sjt, // 服务器工作类型
                            joBizServer.getString("serverId"),
                            joBizServer.getString("host"),
                            joBizServer.getIntValue("port")
                        )
                    )
                );
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public NettyClient selectOneBizServer(ServerJobTypeEnum sjt) {
        return null;
    }
}
