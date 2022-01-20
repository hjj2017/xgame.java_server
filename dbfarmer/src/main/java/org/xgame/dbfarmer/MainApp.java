package org.xgame.dbfarmer;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xgame.dbfarmer.base.DBFarmerLeader;
import org.xgame.dbfarmer.def.WorkModeDef;
import org.xgame.dbfarmer.mod.player.PlayerDBFarmer;

import java.util.concurrent.CountDownLatch;

/**
 * 应用程序类
 */
public final class MainApp {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    /**
     * 服务器 Id
     */
    private static String _Id;

    /**
     * 命令行
     */
    private CommandLine _cmdLn;

    /**
     * 应用程序主函数
     *
     * @param argvArray 参数数组
     */
    public static void main(String[] argvArray) {
        // 设置 log4j 属性文件
        PropertyConfigurator.configure(MainApp.class.getClassLoader().getResourceAsStream("log4j.properties"));
        (new MainApp()).init(argvArray).startUp();
    }

    /**
     * 初始化
     *
     * @param argvArray 命令行参数数组
     * @return this 指针
     */
    private MainApp init(String[] argvArray) {
        // 创建参数选项
        Options op = new Options();
        // --app_id --nacos_server_addr 选项
        op.addRequiredOption(null, "app_id", true, "应用 Id");
        op.addRequiredOption(null, "nacos_server_addr", true, "Nacos 服务器地址");

        try {
            // 创建默认解析器
            DefaultParser dp = new DefaultParser();
            // 解析命令行参数
            _cmdLn = dp.parse(op, argvArray);

            // 设置服务器 Id
            MainApp._Id = _cmdLn.getOptionValue("app_id", null);
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
            "启动数据库农民 appId = {}, 服务器版本号 = {}, 当前工作模式 = {}",
            _Id,
            Ver.CURR,
            WorkModeDef.currWorkMode()
        );

        JSONObject joParam = new JSONObject();
        joParam.put("playerUUId", "2");
        joParam.put("playerName", "MaYue");
        joParam.put("sex", 0);
        joParam.put("age", 24);
        DBFarmerLeader.getInstance().execQuery(PlayerDBFarmer.class, PlayerDBFarmer.QUERY_ID_SAVE_OR_UPDATE, joParam);

        try {
            CountDownLatch cdL = new CountDownLatch(1);
            cdL.await();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
