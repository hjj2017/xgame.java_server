package org.xgame.bizserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.base.BaseLog;
import org.xgame.bizserver.def.WorkModeDef;
import org.xgame.comm.db.DBAgent;

/**
 * 配置
 */
public final class Configure {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = BaseLog.LOGGER;

    /**
     * DataId = MySql 配置
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

        // 获取服务器配置
        ConfigService cs = createConfigService(cmdLn.getOptionValue("nacos_server_addr"));

        try {
            String strConfig = cs.getConfig(
                DATA_ID_ORG_XGAME_CONF,
                GROUP_XXOO + "." + WorkModeDef.currWorkMode(),
                2000
            );

            JSONObject joConfig = JSONObject.parseObject(strConfig);

            initDBAgent(joConfig);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }
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
        joConfig = joConfig.getJSONObject("dbAgent");
        joConfig.put("rpcResponseQueue", rpcResponseQueue);

        DBAgent.getInstance().init(joConfig);
    }
}
