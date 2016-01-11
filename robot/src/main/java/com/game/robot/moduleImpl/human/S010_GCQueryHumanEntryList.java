package com.game.robot.moduleImpl.human;

import java.text.MessageFormat;

import com.game.bizModule.human.msg.CGCreateHuman;
import com.game.bizModule.human.msg.CGEnterHuman;
import com.game.bizModule.human.msg.GCQueryHumanEntryList;
import com.game.bizModule.human.msg.HumanEntryMO;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgStr;
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
public class S010_GCQueryHumanEntryList extends AbstractGCMsgHandler<GCQueryHumanEntryList> {
    @Override
    public void handleGCMsg(
        Robot robotObj, GCQueryHumanEntryList msgObj) {
        if (msgObj._humanEntryList == null ||
            msgObj._humanEntryList.isEmpty()) {
            // 如果没有任何角色,
            // 则新建角色!
            RobotLog.LOG.warn(MessageFormat.format(
                "Robot.{0} 没有角色, 需要新建!",
                robotObj._userName
            ));

            // 创建角色!
            CGCreateHuman cgMSG = new CGCreateHuman();
            cgMSG._humanName = new MsgStr(robotObj._userName);
            cgMSG._usingTmplId = new MsgInt(1);
            cgMSG._serverName = new MsgStr(robotObj._gameServerName);
            // 发送 CG 消息
            robotObj.sendMsg(cgMSG);
        } else {
            // 进入角色
            HumanEntryMO entryMo = msgObj._humanEntryList.get(0);

            if (entryMo == null) {
                // 如果角色入口为空,
                // 则直接退出!
                RobotLog.LOG.error("角色入口为空");
                return;
            }

            // 进入角色!
            CGEnterHuman cgMSG = new CGEnterHuman();
            cgMSG._humanUId = entryMo._humanUId;
            cgMSG._humanUIdStr = entryMo._humanUIdStr;
            // 发送 CG 消息
            robotObj.sendMsg(cgMSG);
        }
    }

    @Override
    protected Class<GCQueryHumanEntryList> getGCMsgClazzDef() {
        return GCQueryHumanEntryList.class;
    }
}
