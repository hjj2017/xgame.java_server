package org.xgame.bizserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.xgame.bizserver.base.BaseLog;
import org.xgame.bizserver.base.InternalServerMsgHandler_BizServer;
import org.xgame.bizserver.cluster.CurrServerReporter;
import org.xgame.bizserver.def.WorkModeDef;
import org.xgame.bizserver.mod.player.PlayerService;
import org.xgame.comm.MainThreadProcessor;
import org.xgame.comm.async.AsyncOperationProcessor;
import org.xgame.comm.cmdhandler.CmdHandlerFactory;
import org.xgame.comm.lazysave.LazySaveService;
import org.xgame.comm.network.NettyServer;
import org.xgame.comm.network.NettyServerConf;
import org.xgame.comm.util.MyTimer;

/**
 * 登录服务器
 */
public final class BizServer {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = BaseLog.LOGGER;

    /**
     * 服务器 Id
     */
    private static String _Id;

    /**
     * 命令行
     */
    private CommandLine _cmdLn;

    /**
     * 私有化类默认构造器
     */
    private BizServer() {
    }

    /**
     * 获取服务器 Id
     *
     * @return 服务器 Id
     */
    public static String getId() {
        return _Id;
    }

    /**
     * 应用主函数
     *
     * @param argvArray 命令行参数数组
     */
    public static void main(String[] argvArray) {
        // 设置 log4j 属性文件
        PropertyConfigurator.configure(BizServer.class.getClassLoader().getResourceAsStream("log4j.properties"));
        (new BizServer()).init(argvArray).startUp();
    }

    /**
     * 初始化
     *
     * @param argvArray 命令行参数数组
     * @return this 指针
     */
    private BizServer init(String[] argvArray) {
        // 创建参数选项
        Options op = new Options();
        // --server_id --server_job_type_set --bind_host --bind_port --nacos_server_addr --config 选项
        op.addRequiredOption(null, "server_id", true, "服务器 Id");
        op.addRequiredOption(null, "server_job_type_set", true, "服务器工作类型集合");
        op.addRequiredOption(null, "bind_host", true, "服务器主机地址");
        op.addRequiredOption(null, "bind_port", true, "服务器端口号");
        op.addOption(null, "config_file", true, "配置文件");
        op.addOption(null, "nacos_server_addr", true, "Nacos 服务器地址");

        try {
            // 创建默认解析器
            DefaultParser dp = new DefaultParser();
            // 解析命令行参数
            _cmdLn = dp.parse(op, argvArray);

            // 设置服务器 Id
            BizServer._Id = _cmdLn.getOptionValue("server_id", null);
            // 初始化配置
            Configure.init(_cmdLn);
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
        }

        return this;
    }

    /**
     * 启动服务器
     */
    private void startUp() {
        if (null == _cmdLn) {
            LOGGER.error("命令行参数错误");
            return;
        }

        LOGGER.info(
            "启动业务服务器 serverId = {}, 服务器版本号 = {}, 当前工作模式 = {}",
            _Id,
            Ver.CURR,
            WorkModeDef.currWorkMode()
        );

        // 延迟保存服务启动心跳
        LazySaveService.getInstance().startHeartbeat();

        // 启动 Netty 服务器
        startUpNettyServer(_cmdLn);
        // 汇报当前服务器
        CurrServerReporter.getInstance().init(_cmdLn).startReport();

        // 添加停机逻辑
        Runtime.getRuntime().addShutdownHook(
            new Thread(this::shutdown)
        );
    }

    /**
     * 启动 Netty 服务器
     *
     * @param cmdLn 命令行参数对象
     */
    private void startUpNettyServer(CommandLine cmdLn) {
        if (null == cmdLn) {
            return;
        }

        CmdHandlerFactory.init(BizServer.class.getPackageName() + ".cmdhandler");

        NettyServerConf newConf = new NettyServerConf()
            .setServerId(cmdLn.getOptionValue("server_id"))
            .setServerHost(cmdLn.getOptionValue("bind_host"))
            .setServerPort(Integer.parseInt(cmdLn.getOptionValue("bind_port")))
            .setCustomChannelHandlerFactory(InternalServerMsgHandler_BizServer::new);

        // 启动 Netty 服务器
        NettyServer newServer = new NettyServer(newConf);
        newServer.startUp();
    }

    /**
     * 停机
     */
    private void shutdown() {
        LOGGER.warn(">>> 服务器准备停机 <<<");

        // 停止线程池
        MainThreadProcessor.getInstance().shutdown();
        MyTimer.getInstance().shutdown();
        AsyncOperationProcessor.getInstance().shutdown();

        // 放弃所有的延迟保存
        LazySaveService.getInstance().forgetALL();

        // 告知每个业务模块执行停服逻辑
        PlayerService.getInstance().onServerShutdown();
    }
}
