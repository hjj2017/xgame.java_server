package com.game.robot.kernal;

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import com.game.robot.RobotLog;

/**
 * 机器人主程序
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public final class RobotMain {
    /** JSON 配置文件 */
    public String _jsonConfFileName = "etc/robot.json";
    /** 机器人字典 */
    private final Map<String, Robot> _robotMap = new ConcurrentHashMap<>();
    
    /**
     * 开始运行机器人
     * 
     */
    public void start() {
        // 记录日志信息
        RobotLog.LOG.info("++++ 启动机器人项目 ++++");

        // 创建配置对象
        RobotLog.LOG.info(MessageFormat.format(
            "加载配置文件 : {0}", 
            this._jsonConfFileName
        ));
        final RobotConf confObj = createConfObj(this._jsonConfFileName);

        if (confObj == null) {
            // 如果配置对象为空, 
            // 则直接退出!
            RobotLog.LOG.error("创建配置对象失败");
            return;
        }

        // 事先扫描 gameServer 项目, 
        // 注册所有的 CG / GC 消息!
        RobotMsgClazzRecognizer.OBJ.scanGameBizModuleProj();

        // 设置游戏服务器 IP 地址
        ConnectToServer.OBJ._gameServerIpAddr = confObj._gameServerIpAddr;
        ConnectToServer.OBJ._gameServerPort = confObj._gameServerPort;
        // 启动心跳控制
        AllRobotHeartbeat.OBJ.startUp();

        // 构建模块链
        final FocusModule chainRoot = ModuleChain.OBJ.build(confObj);

        for (int i = confObj._startPId; i <= confObj._endPId; i++) {
            // 创建机器人对象
            Robot newRobot = AllRobotManager.OBJ.createRobot(
                String.valueOf(i),
                confObj._userPassword
            );
            
            // 将机器人添加到字典中
            this._robotMap.put(
                newRobot._userName, 
                newRobot
            );

            // 创建聚焦模块链表
            newRobot.putCurrFocusModule(chainRoot);
            // 游戏服务器配置
            newRobot._gameServerName = confObj._gameServerName;

            // 记录日志信息
            RobotLog.LOG.info(MessageFormat.format(
                "启动机器人 {0}", 
                newRobot._userName
            ));

            try {
                // 先休息一会儿
                Thread.sleep(confObj._robotStartUpInterval);
            } catch (Exception ex) {
                // 记录错误日志
                RobotLog.LOG.error(
                    ex.getMessage(), ex
                );
            }

            // 启动机器人
            newRobot.start();
        }
    }

    /**
     * 创建配置对象
     * 
     * @param jsonConfFileName 配置文件名称, etc/robot.json
     * @return 机器人配置
     * 
     */
    private static RobotConf createConfObj(String jsonConfFileName) {
        // 断言参数不为空
        assert (jsonConfFileName != null && jsonConfFileName.isEmpty()) : "null jsonConfFileName";

        try {
            // 读取配置文件获取文本字符串
            String textStr = FileUtils.readFileToString(
                new File(jsonConfFileName), 
                Charset.forName("utf8")
            );
            // 创建配置对象
            RobotConf confObj = RobotConf.create(JSONObject.fromObject(textStr));
            // 返回配置对象
            return confObj;
        } catch (Exception ex) {
            // 记录错误日志
            RobotLog.LOG.error(ex.getMessage(), ex);
            return null;
        }
    }
}
