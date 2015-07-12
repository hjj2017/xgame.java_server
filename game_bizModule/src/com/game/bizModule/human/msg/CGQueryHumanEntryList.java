package com.game.bizModule.human.msg;

import com.game.bizModule.global.AllMsgSerialUId;
import com.game.bizModule.human.handler.Handler_CGQueryHumanEntryList;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.type.MsgStr;

/**
 * 查询角色入口列表,
 * 在登陆验证成功之后显示可以选择的角色
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class CGQueryHumanEntryList extends AbstractCGMsgObj<Handler_CGQueryHumanEntryList> {
    /** 服务器名称 */
    public MsgStr _serverName = null;

    @Override
    public short getSerialUId() {
        return AllMsgSerialUId.CG_QUERY_HUMAN_ENTRY_LIST;
    }

    @Override
    protected Handler_CGQueryHumanEntryList newHandlerObj() {
        return new Handler_CGQueryHumanEntryList();
    }
}
