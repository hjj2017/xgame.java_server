package com.game.bizModule.human.handler;

import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.msg.CGCreateHuman;
import com.game.bizModule.human.msg.GCCreateHuman;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgStr;
import com.game.part.util.Out;
import com.game.part.util.OutBool;

/**
 * 创建角色消息处理器
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class Handler_CGCreateHuman extends AbstractCGMsgHandler<CGCreateHuman> {
    @Override
    public void handle(CGCreateHuman msgObj) {
        // 是否有重复的角色名?
        OutBool hasDuplicateName = new OutBool();
        // 异步方式创建角色
        HumanServ.OBJ.asyncCreateHuman(
            this.getPlayer(),
            msgObj._serverName.getStrVal(),
            msgObj._humanName.getStrVal(),
            msgObj._usingTmplId.getIntVal(),
            hasDuplicateName
        );

        if (Out.optVal(hasDuplicateName, false)) {
            // 创建 GC 消息
            GCCreateHuman gcMSG = new GCCreateHuman();
            // 建角失败并说明原因
            gcMSG._success = new MsgBool(false);
            gcMSG._errorMsg = new MsgStr("角色名重复");
            // 发送 GC 消息
            this.sendMsgToClient(gcMSG);
        }
    }
}
