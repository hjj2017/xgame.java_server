package com.game.robot.moduleImpl.human;

import com.game.bizModule.human.msg.CGQueryHumanEntryList;
import com.game.part.msg.type.MsgStr;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 获取玩家角色入口列表
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S000_CGQueryHumanEntryList extends AbstractModuleReady {
    @Override
    public void ready(Robot robotObj) {
        if (robotObj == null) {
            // 如果参数对象为空, 
            // 则直接退出!
            return;
        }

        // 创建并发送 CG 消息
        CGQueryHumanEntryList cgMSG = new CGQueryHumanEntryList();
        cgMSG._serverName = new MsgStr(robotObj._gameServerName);
        robotObj.sendCGMsg(cgMSG);
    }
}
