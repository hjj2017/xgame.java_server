package org.xgame.gatewayserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.def.WorkModeDef;
import org.xgame.gatewayserver.base.BaseLog;

import java.util.Properties;

/**
 * 配置
 * 日志打印过于频繁, 可以增加:
 * -Dcom.alibaba.nacos.config.log.level=error
 * -Dcom.alibaba.nacos.naming.log.level=error
 */
public final class Configure {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = BaseLog.LOGGER;

    /**
     * DataId = Redis 配置
     */
    static private final String DATA_ID_ORG_XGAME_CONF_REDISXUITE = "org.xgame.conf.redisxuite";

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
     * @param serverAddrOfNacos Nacos 服务器地址
     */
    static void init(String serverAddrOfNacos) {
        if (null == serverAddrOfNacos ||
            serverAddrOfNacos.isEmpty()) {
            return;
        }

        // 获取服务器配置
        ConfigService cs = createConfigService(serverAddrOfNacos);
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

            Properties prop = new Properties();
            prop.put("serverAddr", serverAddrOfNacos);

            return NacosFactory.createConfigService(prop);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
            System.exit(-1);
        }

        return null;
    }
}
