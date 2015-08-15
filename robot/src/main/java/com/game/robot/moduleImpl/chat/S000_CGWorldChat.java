package com.game.robot.moduleImpl.chat;

import com.game.bizModule.chat.msg.CGWorldChat;
import com.game.robot.kernal.AbstractModuleReady;
import com.game.robot.kernal.Robot;

/**
 * 世界聊天
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public class S000_CGWorldChat extends AbstractModuleReady {
    @Override
    public void ready(Robot robotObj) {
        if (robotObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 世界聊天
        robotObj.sendMsg(new CGWorldChat("hello World"));
        robotObj.gotoNextModuleAndReady();
    }
}
