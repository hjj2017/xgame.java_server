package org.xgame.bizserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.base.BaseLog;
import org.xgame.bizserver.def.WorkModeDef;
import org.xgame.dbfarmer.agent.DBAgent;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 配置
 */
public final class Configure {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = BaseLog.LOGGER;

    /**
     * DataId
     */
    static private final String DATA_ID_ORG_XGAME_CONF = "org.xgame.conf";

    /**
     * 分组名称
     */
    static private final String GROUP_XXOO = "xxoo";

    /**
     * 私有化类默认构造器
     */
    private Configure() {
    }

    /**
     * 初始化
     *
     * @param cmdLn 命令行对象
     */
    static void init(CommandLine cmdLn) {
        if (null == cmdLn) {
            return;
        }

        String strConfig = null;

        if (cmdLn.hasOption("nacos_server_addr")) {
            // 获取服务器配置
            ConfigService cs = createConfigService(cmdLn.getOptionValue("nacos_server_addr"));

            try {
                strConfig = cs.getConfig(
                    DATA_ID_ORG_XGAME_CONF,
                    GROUP_XXOO + "." + WorkModeDef.currWorkMode(),
                    2000
                );
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        } else if (cmdLn.hasOption("config_file")) {
            try {
                strConfig = Files.readString(Paths.get(cmdLn.getOptionValue("config_file")));
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        if (null == strConfig ||
            strConfig.isEmpty()) {
            LOGGER.error("配置文本为空, 请检查 Nacos 或配置文件");
            System.exit(-1);
            return;
        }

        JSONObject joConfig = JSONObject.parseObject(strConfig);

        initDBAgent(joConfig);
    }

    /**
     * 创建配置服务
     *
     * @param serverAddrOfNacos Nacos 服务器地址
     * @return 配置服务
     */
    static private ConfigService createConfigService(String serverAddrOfNacos) {
        if (null == serverAddrOfNacos ||
            serverAddrOfNacos.isEmpty()) {
            throw new IllegalArgumentException("serverAddrOfNacos is null or empty");
        }

        try {
            LOGGER.info(
                "通过 Nacos 服务器 ( serverAddr = {} ) 加载配置",
                serverAddrOfNacos
            );
            return NacosFactory.createConfigService(serverAddrOfNacos);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
            System.exit(-1);
        }

        return null;
    }

    /**
     * 初始化数据库代理
     *
     * @param joConfig JSON 配置
     */
    static private void initDBAgent(JSONObject joConfig) {
        if (null == joConfig) {
            return;
        }

        String rpcResponseQueue = "rpc_response_queue_" + BizServer.getId();
        joConfig.put("rpcResponseQueue", rpcResponseQueue);
        DBAgent.getInstance().init(joConfig);
    }
}
