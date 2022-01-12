package org.xgame.gatewayserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.xgame.bizserver.base.MyCmdHandlerContext;
import org.xgame.bizserver.def.WorkModeDef;
import org.xgame.comm.network.NettyServer;
import org.xgame.comm.network.NettyServerConf;
import org.xgame.comm.util.MyTimer;
import org.xgame.gatewayserver.base.BaseLog;
import org.xgame.gatewayserver.base.ClientMsgHandler;
import org.xgame.gatewayserver.cluster.BizServerFinder;

import java.util.concurrent.TimeUnit;

/**
 * 网关服务器
 */
public final class GatewayServer {
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
    private GatewayServer() {
    }

    /**
     * 获取服务器 Id
     *
     * @return 服务器 Id
     */
    static public String getId() {
        return _Id;
    }

    /**
     * 应用主函数
     *
     * @param argvArray 命令行参数数组
     */
    static public void main(String[] argvArray) {
        // 设置 log4j 属性文件
        PropertyConfigurator.configure(GatewayServer.class.getClassLoader().getResourceAsStream("log4j.properties"));
        //(new GatewayServer()).init(argvArray).startUp();

        MyTimer.getInstance().scheduleWithFixedDelay(
            () ->
                System.out.println(Thread.currentThread().getName()),
            0, 5, TimeUnit.SECONDS
        );
        MyTimer.getInstance().scheduleWithFixedDelay(
            () ->
                System.out.println(Thread.currentThread().getName()),
            0, 5, TimeUnit.SECONDS);
    }

    /**
     * 初始化
     *
     * @param argvArray 命令行参数数组
     * @return this 指针
     */
    private GatewayServer init(String[] argvArray) {
        // 创建参数选项
        Options op = new Options();
        // --server_id --server_job_type_set --bind_host --bind_port --nacos_server_addr 选项
        op.addRequiredOption(null, "server_id", true, "服务器 Id");
        op.addRequiredOption(null, "bind_host", true, "服务器主机地址");
        op.addRequiredOption(null, "bind_port", true, "服务器端口号");
        op.addRequiredOption(null, "nacos_server_addr", true, "Nacos 服务器地址");

        try {
            // 创建默认解析器
            DefaultParser dp = new DefaultParser();
            // 解析命令行参数
            _cmdLn = dp.parse(op, argvArray);

            // 设置服务器 Id
            GatewayServer._Id = _cmdLn.getOptionValue("server_id", null);
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
            "启动网关服务器 serverId = {}, 服务器版本号 = {}, 当前工作模式 = {}",
            _Id,
            Ver.CURR,
            WorkModeDef.currWorkMode()
        );

        // 初始化配置
        Configure.init(_cmdLn);
        // 启动 Netty 服务器
        startUpNettyServer(_cmdLn);
        // 开始发现新业务服务器
        BizServerFinder.getInstance().init(_cmdLn).startFind();
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

        NettyServerConf newConf = new NettyServerConf()
            .setServerId(cmdLn.getOptionValue("server_id"))
            .setServerHost(cmdLn.getOptionValue("bind_host"))
            .setServerPort(Integer.parseInt(cmdLn.getOptionValue("bind_port")))
            .setCustomChannelHandlerFactory(ClientMsgHandler::new);

        // 启动 Netty 服务器
        NettyServer newServer = new NettyServer(newConf);
        newServer.startUp();
    }
}
