package com.game.robot.moduleImpl.disconnect;

import java.text.MessageFormat;

import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 断开连接
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S000_Disconnect extends AbstractModuleReady {
    @Override
    public void ready(Robot robotObj) {
        if (robotObj == null) {
            // 如果参数对象为空, 
            // 则直接退出!
            return;
        }

        // 断开连接
        robotObj.disconnect();
        // 记录日志信息
        RobotLog.LOG.info(MessageFormat.format(
            "机器人 {0} 已经断开连接", 
            robotObj._userName
        ));

        // 跳转到下一模块
        robotObj.gotoNextModuleAndReady();
    }
}
