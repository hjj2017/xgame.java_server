package org.xgame.dbfarmer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xgame.dbfarmer.base.DBFarmerLeader;
import org.xgame.dbfarmer.def.WorkModeDef;

/**
 * 配置
 */
public final class Configure {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(Configure.class);

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

        // 获取服务器配置
        ConfigService cs = createConfigService(cmdLn.getOptionValue("nacos_server_addr"));

        try {
            String strConfig = cs.getConfig(
                DATA_ID_ORG_XGAME_CONF,
                GROUP_XXOO + "." + WorkModeDef.currWorkMode(),
                2000
            );

            JSONObject joConfig = JSONObject.parseObject(strConfig);

            initDBFarmer(joConfig);
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
     * 初始化数据库农民
     *
     * @param joConfig JSON 配置
     */
    static private void initDBFarmer(JSONObject joConfig) {
        if (null == joConfig) {
            return;
        }

        DBFarmerLeader.getInstance().init(joConfig);
    }
}
