package com.game.robot.moduleImpl.human;

import com.game.bizModule.human.msg.GCEnterHuman;
import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S030_GCEnterHuman extends AbstractGCMsgHandler<GCEnterHuman> {
    @Override
    public void handleGCMsg(
        Robot robotObj, GCEnterHuman msgObj) {
        // 输入日志信息
        RobotLog.LOG.info("进入角色完成");
        // 进入下一模块
        robotObj.gotoNextModuleAndReady();
    }

    @Override
    protected Class<GCEnterHuman> getGCMsgClazzDef() {
        return GCEnterHuman.class;
    }
}
