package org.xgame.bizserver.cluster;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.util.MyTimer;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 当前服务器汇报人
 */
public final class CurrServerReporter {
    /**
     * 日志对象
     */
    public static final Logger LOGGER = ClusterLog.LOGGER;

    /**
     * 服务名称
     */
    static private final String SERVICE_NAME_ORG_XGAME_BIZSERVER = "org.xgame.bizserver";

    /**
     * 单例对象
     */
    private static final CurrServerReporter INSTANCE = new CurrServerReporter();

    /**
     * 命令行参数
     */
    private CommandLine _cmdLn;

    /**
     * 私有化类默认构造器
     */
    private CurrServerReporter() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static CurrServerReporter getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param cmdLn 命令行参数
     * @return this 指针
     */
    public CurrServerReporter init(CommandLine cmdLn) {
        _cmdLn = cmdLn;
        return this;
    }

    /**
     * 开始报告
     */
    public void startReport() {
        try {
            String serverAddrOfNacos = _cmdLn.getOptionValue("nacos_server_addr");
            NamingService ns = createNamingService(serverAddrOfNacos);

            final Instance newInstance = new Instance();
            newInstance.setInstanceId(_cmdLn.getOptionValue("server_id"));
            newInstance.setIp(_cmdLn.getOptionValue("bind_host"));
            newInstance.setPort(Integer.parseInt(_cmdLn.getOptionValue("bind_port")));

            String strServerJobTypeSet = _cmdLn.getOptionValue("server_job_type_set");
            Set<ServerJobTypeEnum> serverJobTypeSet = ServerJobTypeEnum.strToValSet(strServerJobTypeSet);

            // 执行定时任务
            MyTimer.getInstance().scheduleWithFixedDelay(() -> {
                for (ServerJobTypeEnum sjt : serverJobTypeSet) {
                    // 注册服务器实例时, 使用异步操作方式
                    AsyncOperationProcessor.getInstance().process(() -> {
                        try {
                            newInstance.setWeight(0);
                            ns.registerInstance(
                                SERVICE_NAME_ORG_XGAME_BIZSERVER,
                                sjt.getStrVal(),
                                newInstance
                            );
                        } catch (Exception ex) {
                            // 记录错误日志
                            LOGGER.error(ex.getMessage(), ex);
                        }
                    });
                }
            }, 0, 10, TimeUnit.SECONDS);
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
}
