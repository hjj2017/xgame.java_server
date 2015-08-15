package com.game.bizModule.chat.handler;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import com.game.bizModule.chat.ChatLog;
import com.game.bizModule.chat.bizServ.ITypist;
import com.game.bizModule.chat.msg.AbstractCGChat;
import com.game.bizModule.chat.msg.GCChat;
import com.game.bizModule.chat.msg.GGPost;
import com.game.bizModule.chat.msg.GGType;
import com.game.bizModule.human.Human;
import com.game.gameServer.msg.AbstractGGMsgHandler;
import com.game.part.msg.MsgServ;
import com.game.part.msg.type.MsgLong;
import com.game.part.msg.type.MsgStr;

/**
 * 处理聊天抄写消息
 *
 */
public class Handler_GGType extends AbstractGGMsgHandler<GGType> {
    @Override
    public void handle(GGType msgObj) {
        if (msgObj == null ||
            msgObj._h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取 CG 消息
        AbstractCGChat cgMSG = msgObj._cgMSG;

        if (cgMSG == null) {
            // 如果 CG 消息为空,
            // 则直接退出!
            ChatLog.LOG.error("CG 消息对象为空");
            return;
        }

        // 获取打字员
        ITypist<AbstractCGChat> typist = cgMSG.newTypist();

        if (typist == null) {
            // 如果打字员为空,
            // 则直接退出!
            ChatLog.LOG.error(MessageFormat.format(
                "聊天消息 {0} 打字员为空",
                msgObj.getClass().getName()
            ));
            return;
        }

        // 获取角色
        Human h = msgObj._h;
        // 创建输出参数
        List<Long> outHumanUIdList = new LinkedList<>();
        // 获取 GC 消息
        GCChat gcMSG = typist.type(
            h, cgMSG,
            outHumanUIdList
        );

        if (outHumanUIdList.contains(h._humanUId) == false) {
            // 聊天消息需要给说话人自己也发一条
            outHumanUIdList.add(h._humanUId);
        }

        if (gcMSG._fromHumanUId == null ||
            gcMSG._fromHumanUIdStr == null) {
            // 设置角色 UId
            gcMSG._fromHumanUId = new MsgLong(h._humanUId);
            gcMSG._fromHumanUIdStr = new MsgStr(String.valueOf(h._humanUId));
        }

        if (gcMSG._fromHumanName == null) {
            // 设置角色名称
            gcMSG._fromHumanName = new MsgStr(h._humanName);
        }

        // 准备递送
        GGPost ggMSG = new GGPost();
        ggMSG._gcMSG = gcMSG;
        ggMSG._humanUIdList = outHumanUIdList;
        // 提交消息对象
        MsgServ.OBJ.post(ggMSG);
    }
}
