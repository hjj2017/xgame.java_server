package org.xgame.bizserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xgame.bizserver.base.InternalServerMsgHandler_BizServer;
import org.xgame.comm.network.NettyServer;
import org.xgame.comm.network.NettyServerConf;

/**
 * 登录服务器
 */
public final class BizServer {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BizServer.class);

    /**
     * 服务器 Id
     */
    static private String _Id;

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
        // --server_id --server_job_type_set --server_host --server_port --nacos_server_addr 选项
        op.addRequiredOption(null, "server_id", true, "服务器 Id");
        op.addRequiredOption(null, "server_job_type_set", true, "服务器工作类型集合");
        op.addRequiredOption(null, "server_host", true, "服务器主机地址");
        op.addRequiredOption(null, "server_port", true, "服务器端口号");
        op.addRequiredOption(null, "nacos_server_addr", true, "Nacos 服务器地址");

        try {
            // 创建默认解析器
            DefaultParser dp = new DefaultParser();
            // 解析命令行参数
            this._cmdLn = dp.parse(op, argvArray);

            // 设置服务器 Id
            BizServer._Id = this._cmdLn.getOptionValue("server_id", null);
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
        if (null == this._cmdLn) {
            LOGGER.error("命令行参数错误");
            return;
        }

        LOGGER.info("serverId = {}", BizServer.getId());

        // 启动 Netty 服务器
        startUpNettyServer(this._cmdLn);
    }

    /**
     * 启动 Netty 服务器
     *
     * @param cmdLn 命令行参数对象
     */
    private static void startUpNettyServer(CommandLine cmdLn) {
        if (null == cmdLn) {
            return;
        }

        NettyServerConf newConf = new NettyServerConf()
            .setServerId(cmdLn.getOptionValue("server_id"))
            .setServerHost(cmdLn.getOptionValue("bind_host"))
            .setServerPort(Integer.parseInt(cmdLn.getOptionValue("bind_port")))
            .setCustomChannelHandlerFactory(InternalServerMsgHandler_BizServer::new);

        // 启动 Netty 服务器
        NettyServer newServer = new NettyServer(newConf);
        newServer.startUp();
    }
}
