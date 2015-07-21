package com.game.bizModule.human.handler;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.HumanLog;
import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.msg.CGCreateHuman;
import com.game.bizModule.human.msg.GCCreateHuman;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgStr;

import java.text.MessageFormat;

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
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取服务器名称和角色名称
        final String serverName = msgObj._serverName.getStrVal();
        final String humanName = msgObj._humanName.getStrVal();

        // 角色全名
        final String fullName = Human.getFullName(
            serverName, humanName
        );

        if (HumanServ.OBJ._humanFullNameSet.contains(fullName)) {
            // 如果角色全名重复,
            // 则直接退出!
            HumanLog.LOG.error(MessageFormat.format(
                "角色全名 ''{0}'' 重复",
                fullName
            ));

            // 创建 GC 消息
            GCCreateHuman gcMSG = new GCCreateHuman();
            // 建角失败并说明原因
            gcMSG._success = new MsgBool(false);
            gcMSG._errorMsg = new MsgStr("角色名重复");
            // 发送 GC 消息
            this.sendMsgToClient(gcMSG);
            return;
        }

        // 异步方式创建角色
        HumanServ.OBJ.asyncCreateHuman(
            this.getPlayer(),
            serverName, humanName,
            msgObj._humanTmplId.getIntVal()
        );
    }
}
