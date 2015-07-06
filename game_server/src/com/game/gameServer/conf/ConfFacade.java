package com.game.gameServer.conf;

import java.io.File;
import java.text.MessageFormat;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.framework.GameServerConf;
import com.game.gameServer.framework.mina.SocketConf;
import com.game.gameServer.io.IoConf;

/**
 * 配置门面
 *
 * @author hjj2019
 * @since 2015/7/6
 *
 */
public final class ConfFacade {
    /** 单例对象 */
    public static final ConfFacade OBJ = new ConfFacade();

    /**
     * 类默认构造器
     *
     */
    private ConfFacade() {
    }

    /**
     * 从文件中读取配置
     *
     * @param configFileName
     *
     */
    public void readFromFile(final String configFileName) {
        if (configFileName == null ||
            configFileName.isEmpty()) {
            // 记录日志错误
            FrameworkLog.LOG.error("配置文件名称为空");
            return;
        }

        try {
            // 记录日志信息
            FrameworkLog.LOG.info(MessageFormat.format(
                "准备读取配置文件 : {0}",
                configFileName
            ));

            // 读取配置文件
            File fileObj = new File(configFileName);

            if (fileObj.exists() == false) {
                // 如果文件不存在,
                // 则直接退出!
                FrameworkLog.LOG.error(MessageFormat.format(
                    "配置文件 {0} 不存在",
                    configFileName
                ));
                return;
            }

            // 读取 JSON 文本
            String jsonText = FileUtils.readFileToString(fileObj);
            // 创建 JSON 对象
            JSONObject jsonObj = JSONObject.fromObject(jsonText);
            // 仅获取游戏服关心的配置
            JSONObject myJson = jsonObj.getJSONObject("gameServer");

            // 加载游戏服配置
            GameServerConf.OBJ.readFromJson(myJson);
            // 加载网络配置
            SocketConf.OBJ.readFromJson(myJson);
            // IO 配置
            IoConf.OBJ.readFromJson(myJson);
        } catch (Exception ex) {
            // 记录错误日志
            FrameworkLog.LOG.error(ex.getMessage(), ex);
        }
    }
}
